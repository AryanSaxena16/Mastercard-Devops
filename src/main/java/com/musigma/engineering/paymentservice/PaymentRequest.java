package com.musigma.engineering.paymentservice;

// Suppress warnings because Spring Boot maps these setters automatically via JSON reflection
@SuppressWarnings("unused")
public class PaymentRequest {
    private String idempotencyKey;
    private double amount;
    private String currency;

    // Default constructor for Spring Boot to process JSON
    public PaymentRequest() {}

    // Getters and Setters
    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public void setIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
