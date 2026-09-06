package com.shopkart.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a placed order. Used by OrderQueue (Session 15),
 * OrderHistory linked list (Session 12), and the DB layer (Session 29).
 */
public class Order {
    private final String orderId;
    private final Customer customer;
    private final List<Product> items = new ArrayList<>();
    private boolean express; // true -> goes into the priority queue (Session 15)
    private double total;

    public Order(String orderId, Customer customer, boolean express) {
        this.orderId = orderId;
        this.customer = customer;
        this.express = express;
    }

    public void addItem(Product p) { items.add(p); }
    public String getOrderId() { return orderId; }
    public Customer getCustomer() { return customer; }
    public List<Product> getItems() { return items; }
    public boolean isExpress() { return express; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    @Override
    public String toString() {
        return String.format("Order#%s by %s | items:%d | total:₹%.2f | express:%b",
                orderId, customer.getName(), items.size(), total, express);
    }
}
