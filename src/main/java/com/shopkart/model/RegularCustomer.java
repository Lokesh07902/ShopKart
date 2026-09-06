package com.shopkart.model;

/** SESSION 5 — no discount, the "default" tier. */
public class RegularCustomer extends Customer {
    public RegularCustomer(String id, String name, String email) {
        super(id, name, email);
    }

    @Override
    public double getDiscountRate() {
        return 0.0;
    }
}
