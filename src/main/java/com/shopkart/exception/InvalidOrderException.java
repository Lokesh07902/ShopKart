package com.shopkart.exception;

/**
 * SESSION 7 — thrown when an order is checked out with zero items,
 * an unknown customer, or a negative/zero total.
 */
public class InvalidOrderException extends RuntimeException {
    public InvalidOrderException(String message) {
        super(message);
    }
}
