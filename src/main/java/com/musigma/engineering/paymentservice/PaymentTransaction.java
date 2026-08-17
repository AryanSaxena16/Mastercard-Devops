package com.musigma.engineering.paymentservice;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
// Suppress the table warning since H2 spins up dynamically inside memory at runtime
@Table(name = "payments")
@SuppressWarnings("unused")
public class PaymentTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String idempotencyKey;

    private double amount;
    private String currency;
    private String status; // PENDING, SUCCESS, FAILED
    private LocalDateTime createdAt;

    // Default constructor needed by JPA
    public PaymentTransaction() {}

    public PaymentTransaction(String idempotencyKey, double amount, String currency, String status) {
        this.idempotencyKey = idempotencyKey;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }

    public String getIdempotencyKey() { return idempotencyKey; }

    public double getAmount() { return amount; }

    public String getCurrency() { return currency; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}