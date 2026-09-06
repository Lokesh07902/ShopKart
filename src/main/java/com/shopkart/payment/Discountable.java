package com.shopkart.payment;

/**
 * SESSION 6 — Abstract Classes, Interfaces & Exception Hierarchy
 * A pure contract (no state) — any customer type that can have a discount
 * applied to it implements this. Contrast with PaymentMethod below, which is
 * an abstract class because it shares actual state/behaviour across subclasses.
 */
public interface Discountable {
    double applyDiscount(double amount);
}
