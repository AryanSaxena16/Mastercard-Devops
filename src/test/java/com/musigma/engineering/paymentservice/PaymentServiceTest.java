package com.musigma.engineering.paymentservice;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Optional;

// Explicitly importing static testing utilities to clear IDE mapping confusion
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;

@SuppressWarnings("unused")
public class PaymentServiceTest {

    private PaymentRepository paymentRepository;
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        paymentRepository = mock(PaymentRepository.class);
        paymentService = new PaymentService(paymentRepository);
    }

    @Test
    void testProcessPayment_Success() {
        PaymentRequest request = new PaymentRequest();
        request.setIdempotencyKey("test-key-123");
        request.setAmount(150.00);
        request.setCurrency("INR");

        when(paymentRepository.findByIdempotencyKey("test-key-123")).thenReturn(Optional.empty());

        String result = paymentService.processPayment(request);

        assertTrue(result.contains("SUCCESS"));
        verify(paymentRepository, atLeastOnce()).save(any(PaymentTransaction.class));
    }

    @Test
    void testProcessPayment_IdempotencyTriggered() {
        PaymentRequest request = new PaymentRequest();
        request.setIdempotencyKey("duplicate-key-456");
        request.setAmount(150.00);
        request.setCurrency("INR");

        PaymentTransaction existingTx = new PaymentTransaction("duplicate-key-456", 150.00, "INR", "SUCCESS");
        when(paymentRepository.findByIdempotencyKey("duplicate-key-456")).thenReturn(Optional.of(existingTx));

        String result = paymentService.processPayment(request);

        assertTrue(result.contains("DUPLICATE"));
        verify(paymentRepository, times(0)).save(any(PaymentTransaction.class));
    }
}
