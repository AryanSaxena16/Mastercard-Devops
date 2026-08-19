package com.musigma.engineering.paymentservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public String processPayment(PaymentRequest request) {
        String key = request.getIdempotencyKey();

        Optional<PaymentTransaction> existingTx = paymentRepository.findByIdempotencyKey(key);
        if (existingTx.isPresent()) {
            return "DUPLICATE: Already processed. Status: " + existingTx.get().getStatus();
        }

        PaymentTransaction transaction = new PaymentTransaction(
                key, request.getAmount(), request.getCurrency(), "PENDING"
        );
        paymentRepository.saveAndFlush(transaction);

        if (request.getAmount() <= 0) {
            transaction.setStatus("FAILED");
            paymentRepository.save(transaction);
            return "REJECTED: Amount must be greater than zero.";
        }

        try {
            long maxTimeoutAllowedMs = 2000; // 2s Production Constraint Boundary

            // DYNAMIC SIMULATION: Use values explicitly requested by the client script
            if (request.getSimulateDelayMs() > 0) {
                Thread.sleep(request.getSimulateDelayMs());
            } else {
                Thread.sleep(10); // Standard micro-latency under high load
            }

            // Client-requested explicit failure simulation or threshold timeout trigger
            if (request.isSimulateFailure() || request.getSimulateDelayMs() > maxTimeoutAllowedMs) {
                throw new java.util.concurrent.TimeoutException("Simulated network/gateway failure occurred.");
            }

            transaction.setStatus("SUCCESS");
            paymentRepository.save(transaction);
            return "SUCCESS: Transaction completed.";

        } catch (java.util.concurrent.TimeoutException | InterruptedException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            transaction.setStatus("FAILED");
            paymentRepository.save(transaction);
            return "TIMEOUT: Gateway failure or high latency detected.";
        }
    }
}
