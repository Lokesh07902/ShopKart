package com.shopkart.model;

import com.shopkart.payment.Discountable;

/**
 * SESSION 5 — Encapsulation, Inheritance & Polymorphism
 * Abstract base for customer types. getDiscountRate() is overridden
 * differently by RegularCustomer and PremiumCustomer (runtime polymorphism).
 */
public abstract class Customer implements Discountable {

    private final String id;
    private String name;
    private String email;

    public Customer(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    /** Overridden per subclass — this IS the polymorphism the module asks for. */
    public abstract double getDiscountRate();

    @Override
    public double applyDiscount(double amount) {
        return amount - (amount * getDiscountRate());
    }

    @Override
    public String toString() {
        return String.format("%s (%s) [%s]", name, email, getClass().getSimpleName());
    }
}
