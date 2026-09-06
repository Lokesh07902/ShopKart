package com.shopkart.algorithms;

import com.shopkart.model.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * SESSION 21 — Merge Sort & Quick Sort
 * SESSION 22 — Counting Sort & Other Algorithms
 * All three sort Product[] by different keys, chosen to show WHY you'd pick
 * one algorithm over another for a given kind of data.
 */
public class SortingAlgorithms {

    // ---------- MERGE SORT — by price. Stable, guaranteed O(n log n) ----------
    public static void mergeSortByPrice(Product[] arr, int left, int right) {
        if (left >= right) return;
        int mid = (left + right) / 2;
        mergeSortByPrice(arr, left, mid);
        mergeSortByPrice(arr, mid + 1, right);
        merge(arr, left, mid, right);
    }

    private static void merge(Product[] arr, int left, int mid, int right) {
        Product[] leftArr = java.util.Arrays.copyOfRange(arr, left, mid + 1);
        Product[] rightArr = java.util.Arrays.copyOfRange(arr, mid + 1, right + 1);
        int i = 0, j = 0, k = left;
        while (i < leftArr.length && j < rightArr.length) {
            arr[k++] = leftArr[i].getPrice() <= rightArr[j].getPrice() ? leftArr[i++] : rightArr[j++];
        }
        while (i < leftArr.length) arr[k++] = leftArr[i++];
        while (j < rightArr.length) arr[k++] = rightArr[j++];
    }

    // ---------- QUICK SORT — by price. In-place, avg O(n log n), no extra array ----------
    public static void quickSortByPrice(Product[] arr, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(arr, low, high);
            quickSortByPrice(arr, low, pivotIndex - 1);
            quickSortByPrice(arr, pivotIndex + 1, high);
        }
    }

    private static int partition(Product[] arr, int low, int high) {
        double pivot = arr[high].getPrice();
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (arr[j].getPrice() < pivot) {
                i++;
                Product temp = arr[i]; arr[i] = arr[j]; arr[j] = temp;
            }
        }
        Product temp = arr[i + 1]; arr[i + 1] = arr[high]; arr[high] = temp;
        return i + 1;
    }

    /**
     * COUNTING SORT — by rating. Ratings are bounded to a small, known range
     * (1-5 stars), which is exactly when Counting Sort beats comparison
     * sorts: O(n + k) instead of O(n log n), with k = 5 here.
     * NOT usable for price sorting — price isn't a small bounded integer range.
     */
    public static List<Product> countingSortByRating(List<Product> products) {
        int maxStars = 5;
        List<List<Product>> buckets = new ArrayList<>();
        for (int i = 0; i <= maxStars; i++) buckets.add(new ArrayList<>());

        for (Product p : products) {
            int bucket = (int) Math.round(p.getRating());
            buckets.get(Math.min(bucket, maxStars)).add(p);
        }

        List<Product> sorted = new ArrayList<>();
        for (int star = maxStars; star >= 0; star--) { // highest rated first
            sorted.addAll(buckets.get(star));
        }
        return sorted;
    }
}
