package com.musigma.engineering.paymentservice;



import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<PaymentTransaction, Long> {

    // Custom database search query automatically constructed by Spring Data JPA
    Optional<PaymentTransaction> findByIdempotencyKey(String idempotencyKey);
}
