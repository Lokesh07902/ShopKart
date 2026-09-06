package com.shopkart.concurrency;

import com.shopkart.model.Product;

import java.util.concurrent.CountDownLatch;

/**
 * SESSION 26 — Thread Concepts & Synchronization
 * Demonstrates a real race condition, then fixes it. Multiple checkout
 * threads decrement `stock` on the SAME product at once — without
 * synchronization, two threads can both read stock=1, both decide "still in
 * stock", and both sell it (stock goes negative). `synchronized` on the
 * decrement method forces one thread at a time through the critical section.
 */
public class StockUpdateDemo {

    /** Deliberately UNSAFE — used first to show the bug. */
    static void unsafeDecrement(Product product) {
        int current = product.getStock();
        // simulate work between read and write, widening the race window
        try { Thread.sleep(1); } catch (InterruptedException ignored) {}
        product.setStock(current - 1);
    }

    /** SAFE — synchronized guarantees only one thread executes this at a time per lock object. */
    static synchronized void safeDecrement(Product product) {
        int current = product.getStock();
        product.setStock(current - 1);
    }

    public static void runRaceConditionDemo(Product product, boolean useSafeVersion) throws InterruptedException {
        int threadCount = 10;
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                if (useSafeVersion) safeDecrement(product);
                else unsafeDecrement(product);
                latch.countDown();
            }).start();
        }
        latch.await(); // wait for all 10 threads to finish

        System.out.println((useSafeVersion ? "SAFE" : "UNSAFE") +
                " run -> final stock: " + product.getStock() +
                " (expected: started_stock - 10)");
    }
}
