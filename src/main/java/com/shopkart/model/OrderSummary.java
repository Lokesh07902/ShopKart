package com.shopkart.model;

/**
 * SESSION 28 — Optional Class & Modern Java Features
 * A `record` is a Java 16+ feature: an immutable data carrier that auto-generates
 * constructor, getters (orderId(), total()), equals(), hashCode(), toString().
 * Used to send a lightweight, read-only view of an Order over the REST API (Session 30)
 * without exposing the full mutable Order object.
 */
public record OrderSummary(String orderId, String customerName, double total, int itemCount) {
}
