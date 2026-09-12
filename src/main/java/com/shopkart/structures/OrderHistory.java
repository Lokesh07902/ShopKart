package com.shopkart.structures;

import com.shopkart.model.Order;

/**
 * Singly & Doubly Linked Lists
 *  A doubly linked list is the right structure here because we need
 * to walk order history BOTH forward (oldest -> newest) and backward
 * (newest -> oldest, "show my last order") in O(1) per step, and
 * insertion at the tail must be O(1) — an ArrayList would need to shift
 * elements for head-insertion, a singly linked list can't go backward.
 */
public class OrderHistory {

    private static class Node {
        Order order;
        Node prev, next;
        Node(Order order) { this.order = order; }
    }

    private Node head;
    private Node tail;
    private int size = 0;

    /** O(1) — append at tail. */
    public void addOrder(Order order) {
        Node node = new Node(order);
        if (tail == null) {
            head = tail = node;
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
        size++;
    }

    /** O(1) — the whole reason we track `tail` and `prev` pointers. */
    public Order getMostRecent() {
        return tail == null ? null : tail.order;
    }

    public void printForward() {
        System.out.print("Order history (oldest -> newest): ");
        Node cur = head;
        while (cur != null) {
            System.out.print(cur.order.getOrderId() + " -> ");
            cur = cur.next;
        }
        System.out.println("null");
    }

    public void printBackward() {
        System.out.print("Order history (newest -> oldest): ");
        Node cur = tail;
        while (cur != null) {
            System.out.print(cur.order.getOrderId() + " -> ");
            cur = cur.prev;
        }
        System.out.println("null");
    }

    public int size() { return size; }
}
