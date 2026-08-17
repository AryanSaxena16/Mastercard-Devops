package com.musigma.engineering.paymentservice;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    // Recommended Constructor Injection to avoid warnings and keep code clean
    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<String> processPayment(@RequestBody PaymentRequest request) {
        // Enforce basic header/payload requirement checks
        if (request.getIdempotencyKey() == null || request.getIdempotencyKey().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing Idempotency Key");
        }

        // Send payment down to the enterprise service logic layer
        String result = paymentService.processPayment(request);

        // Map service responses to accurate HTTP Status codes for frontend consumers
        if (result.contains("DUPLICATE")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
        } else if (result.contains("REJECTED")) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(result);
        } else if (result.contains("TIMEOUT")) {
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(result);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
