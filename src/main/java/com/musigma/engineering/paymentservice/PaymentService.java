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

        // 1. Production Architecture Step: Check Idempotency Database Cache
        Optional<PaymentTransaction> existingTx = paymentRepository.findByIdempotencyKey(key);
        if (existingTx.isPresent()) {
            return "DUPLICATE: Already processed. Status: " + existingTx.get().getStatus();
        }

        // 2. Production Architecture Step: Record "PENDING" state before executing payment
        PaymentTransaction transaction = new PaymentTransaction(
                key,
                request.getAmount(),
                request.getCurrency(),
                "PENDING"
        );

        // FIXED: Forcing an immediate database flush ensures uncommitted reads are viewable in H2
        paymentRepository.saveAndFlush(transaction);

        // 3. Risk Mitigation: Catch invalid business rules
        if (request.getAmount() <= 0) {
            transaction.setStatus("FAILED");
            paymentRepository.save(transaction);
            return "REJECTED: Amount must be greater than zero.";
        }

        // 4. Production Resiliency Step: Enforce strict connection timeout boundaries
        try {
            long maxTimeoutAllowedMs = 2000; // 2 Seconds maximum limit

            // SPECIAL PRODUCTION TRICK: Amount 555 creates a 30s freeze to monitor PENDING live
            long actualGatewayLatencyMs = 500;
            if (request.getAmount() == 999) {
                actualGatewayLatencyMs = 4000;
            } else if (request.getAmount() == 555) {
                actualGatewayLatencyMs = 30000;
            }

            // Simulate waiting for the external banking network response
            Thread.sleep(actualGatewayLatencyMs);

            // High-Availability Check: Did the bank response exceed our production threshold limit?
            if (actualGatewayLatencyMs > maxTimeoutAllowedMs) {
                throw new java.util.concurrent.TimeoutException("External gateway took too long to respond.");
            }

            // Transaction completed safely within time bounds
            transaction.setStatus("SUCCESS");
            paymentRepository.save(transaction);
            return "SUCCESS: Transaction completed.";

        } catch (java.util.concurrent.TimeoutException | InterruptedException e) {
            // Re-verify system states and mark as FAILED to prevent thread exhaustion
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            transaction.setStatus("FAILED");
            paymentRepository.save(transaction);
            return "TIMEOUT: Gateway dropped the connection due to high latency.";
        }
    }
}
