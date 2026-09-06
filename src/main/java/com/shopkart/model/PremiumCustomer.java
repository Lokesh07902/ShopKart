package com.shopkart.model;

/** SESSION 5 — flat 10% discount, demonstrates overriding the same method differently. */
public class PremiumCustomer extends Customer {
    private static final double DISCOUNT_RATE = 0.10;

    public PremiumCustomer(String id, String name, String email) {
        super(id, name, email);
    }

    @Override
    public double getDiscountRate() {
        return DISCOUNT_RATE;
    }
}
