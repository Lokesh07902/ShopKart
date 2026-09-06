package com.shopkart.structures;

import com.shopkart.model.Product;

/**
 * SESSION 13 — Circular Linked Lists & Applications
 * The "Featured Products" banner on a shopping site loops forever — after the
 * last product it goes back to the first. A circular linked list models that
 * naturally: the tail's `next` points back to `head` instead of to null, so
 * next() never has to check for the end and wrap manually.
 */
public class FeaturedCarousel {

    private static class Node {
        Product product;
        Node next;
        Node(Product product) { this.product = product; }
    }

    private Node current;
    private int size = 0;

    public void addProduct(Product product) {
        Node node = new Node(product);
        if (current == null) {
            node.next = node; // points to itself — the circle of one
            current = node;
        } else {
            // insert right before `current` to keep the circle closed
            Node temp = current;
            while (temp.next != current) temp = temp.next;
            temp.next = node;
            node.next = current;
        }
        size++;
    }

    /** O(1) — advance and wrap automatically, no modulo / bounds-check needed. */
    public Product next() {
        if (current == null) return null;
        Product p = current.product;
        current = current.next;
        return p;
    }

    public int size() { return size; }
}
