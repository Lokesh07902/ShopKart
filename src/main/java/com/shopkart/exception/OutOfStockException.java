package com.shopkart.exception;

/**
 * SESSION 7 — Custom Exceptions & Java I/O
 * Unchecked (extends RuntimeException) because running out of stock is a normal
 * business condition the caller decides how to handle — not a fatal programming error.
 * Thrown from CartService when quantity requested > available stock.
 */
public class OutOfStockException extends RuntimeException {
    public OutOfStockException(String productName, int requested, int available) {
        super(String.format("'%s' is out of stock: requested %d, only %d available",
                productName, requested, available));
    }
}
