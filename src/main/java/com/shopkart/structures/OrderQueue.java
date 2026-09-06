package com.shopkart.structures;

import com.shopkart.model.Order;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * SESSION 15 — Queue Implementation & Applications
 * Two queues, two different fairness rules:
 *  - `regularQueue` is plain FIFO — normal orders are processed in the order
 *    they arrived (Queue<Order> backed by LinkedList).
 *  - `expressQueue` is a PriorityQueue (min-heap under the hood) — express
 *    orders jump ahead, always processed before regular ones regardless of
 *    arrival time.
 */
public class OrderQueue {

    private final Queue<Order> regularQueue = new LinkedList<>();
    private final PriorityQueue<Order> expressQueue =
            new PriorityQueue<>(Comparator.comparing(Order::getOrderId)); // FIFO among express orders too

    public void enqueue(Order order) {
        if (order.isExpress()) {
            expressQueue.offer(order);
        } else {
            regularQueue.offer(order);
        }
    }

    /** Express orders are always drained first. */
    public Order processNext() {
        if (!expressQueue.isEmpty()) return expressQueue.poll();
        return regularQueue.poll();
    }

    public boolean isEmpty() {
        return regularQueue.isEmpty() && expressQueue.isEmpty();
    }
}
