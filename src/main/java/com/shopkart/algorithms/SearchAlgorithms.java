package com.shopkart.algorithms;

import com.shopkart.model.Product;

import java.util.List;

/**
 * SESSION 3 — Intro to DSA & Big-O (linearSearch — baseline for comparison)
 * SESSION 23 — Binary Search & Variants
 */
public class SearchAlgorithms {

    /** O(n) — the naive baseline every later structure improves on. */
    public static Product linearSearch(List<Product> products, String id) {
        for (Product p : products) {
            if (p.getId().equals(id)) return p;
        }
        return null;
    }

    /**
     * O(log n) — standard binary search, list MUST be sorted by price first.
     * Returns the exact match, or -1 if not found.
     */
    public static int binarySearchByPrice(List<Product> sortedByPrice, double targetPrice) {
        int low = 0, high = sortedByPrice.size() - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            double midPrice = sortedByPrice.get(mid).getPrice();
            if (Math.abs(midPrice - targetPrice) < 0.001) return mid;
            else if (midPrice < targetPrice) low = mid + 1;
            else high = mid - 1;
        }
        return -1;
    }

    /**
     * SESSION 23 — Lower-bound binary search variant ("search on answer").
     * Finds the index of the first product priced >= targetPrice.
     * Used for "show me everything from ₹X upward".
     */
    public static int lowerBound(List<Product> sortedByPrice, double targetPrice) {
        int low = 0, high = sortedByPrice.size(); // note: high = size, not size-1
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (sortedByPrice.get(mid).getPrice() < targetPrice) low = mid + 1;
            else high = mid;
        }
        return low; // insertion point / first index satisfying price >= target
    }
}
