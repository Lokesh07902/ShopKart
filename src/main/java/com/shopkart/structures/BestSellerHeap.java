package com.shopkart.structures;

import com.shopkart.model.Product;

import java.util.*;

/**
 * SESSION 18 — Heaps: Min-Heap, Max-Heap & Heap Sort
 * "Top 5 Best Sellers" needs the K largest values from a stream of products
 * by unitsSold. A max-heap gives O(log n) insert and O(1) peek-max, so we
 * never have to fully sort the whole catalog just to answer "what's on top".
 * Java's PriorityQueue is a MIN-heap by default, so we flip it with a
 * reversed comparator to get max-heap behaviour.
 */
public class BestSellerHeap {

    private final PriorityQueue<Product> maxHeap =
            new PriorityQueue<>(Comparator.comparingInt(Product::getUnitsSold).reversed());

    public void addOrUpdate(Product product) {
        maxHeap.remove(product); // no-op if not present
        maxHeap.offer(product);
    }

    /** O(k log n) — pop the top k without disturbing the rest of the heap's invariant. */
    public List<Product> topN(int n) {
        PriorityQueue<Product> copy = new PriorityQueue<>(maxHeap);
        List<Product> result = new ArrayList<>();
        for (int i = 0; i < n && !copy.isEmpty(); i++) {
            result.add(copy.poll());
        }
        return result;
    }

    /**
     * SESSION 18 — Heap Sort, implemented by hand (not via PriorityQueue) to
     * show the algorithm itself: build a max-heap in the array, then
     * repeatedly swap the root (largest) to the end and shrink the heap.
     */
    public static void heapSortByRating(Product[] products) {
        int n = products.length;
        for (int i = n / 2 - 1; i >= 0; i--) heapify(products, n, i);
        for (int i = n - 1; i > 0; i--) {
            Product temp = products[0];
            products[0] = products[i];
            products[i] = temp;
            heapify(products, i, 0);
        }
    }

    private static void heapify(Product[] arr, int size, int rootIdx) {
        int largest = rootIdx;
        int left = 2 * rootIdx + 1, right = 2 * rootIdx + 2;
        if (left < size && arr[left].getRating() > arr[largest].getRating()) largest = left;
        if (right < size && arr[right].getRating() > arr[largest].getRating()) largest = right;
        if (largest != rootIdx) {
            Product temp = arr[rootIdx];
            arr[rootIdx] = arr[largest];
            arr[largest] = temp;
            heapify(arr, size, largest);
        }
    }
}
