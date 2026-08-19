package com.musigma.engineering.paymentservice;

@SuppressWarnings("unused")
public class PaymentRequest {
    private String idempotencyKey;
    private double amount;
    private String currency; // Supports INR, USD, JPY, RUB, etc.

    // Automation Testing Parameters
    private long simulateDelayMs;
    private boolean simulateFailure;

    public PaymentRequest() {}

    // Getters and Setters
    public String getIdempotencyKey() { return idempotencyKey; }
    public void setIdempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public long getSimulateDelayMs() { return simulateDelayMs; }
    public void setSimulateDelayMs(long simulateDelayMs) { this.simulateDelayMs = simulateDelayMs; }

    public boolean isSimulateFailure() { return simulateFailure; }
    public void setSimulateFailure(boolean simulateFailure) { this.simulateFailure = simulateFailure; }
}
