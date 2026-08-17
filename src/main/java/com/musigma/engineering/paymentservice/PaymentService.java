package com.musigma.engineering.paymentservice;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class PaymentService {

    // Removed @Autowired from here. Variable is now marked final for safety.
    private final PaymentRepository paymentRepository;

    // Recommended Constructor Injection pattern (Clears IntelliJ warning)
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
        paymentRepository.save(transaction);

        // 3. Risk Mitigation: Catch invalid business rules
        if (request.getAmount() <= 0) {
            transaction.setStatus("FAILED");
            paymentRepository.save(transaction);
            return "REJECTED: Amount must be greater than zero.";
        }

        // 4. Simulate Third-Party Gateway Integration Handshake
        try {
            // Production Simulation: Value 999 triggers an intentional laggy bank timeout
            if (request.getAmount() == 999) {
                Thread.sleep(4000);
            } else {
                Thread.sleep(500); // Normal network latency simulation
            }

            // Update transaction record to final successful state
            transaction.setStatus("SUCCESS");
            paymentRepository.save(transaction);
            return "SUCCESS: Transaction completed.";

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            transaction.setStatus("FAILED");
            paymentRepository.save(transaction);
            return "TIMEOUT: Gateway dropped the connection.";
        }
    }
}
