package com.shopkart.payment;

public class UPIPayment extends PaymentMethod {
    private final String upiId;

    public UPIPayment(String upiId) {
        this.upiId = upiId;
    }

    @Override
    public boolean pay(double amount) {
        System.out.println(receipt(amount, "UPI (" + upiId + ")"));
        return true;
    }
}
