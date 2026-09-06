package com.shopkart.payment;

/**
 * SESSION 6 — Abstract class chosen over an interface here because every
 * payment method shares real fields/behaviour (a transaction log message
 * format), not just a method signature. pay() is abstract because HOW money
 * moves genuinely differs per method.
 */
public abstract class PaymentMethod {

    public abstract boolean pay(double amount);

    protected String receipt(double amount, String method) {
        return String.format("Paid ₹%.2f via %s — SUCCESS", amount, method);
    }
}
