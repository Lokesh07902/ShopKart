package com.shopkart.repository;

import com.shopkart.model.Product;

import java.util.*;
import java.util.stream.Collectors;


public class ProductRepository extends Repository<Product> {

    public ProductRepository() {
        super(Product::getId);
    }

    
    public Set<String> getAllCategories() {
        Set<String> categories = new HashSet<>();
        for (Product p : getAll()) categories.add(p.getCategory());
        return categories;
    }

    
    public Optional<Product[]> findPairWithExactTotal(List<Product> sortedByPrice, double target) {
        int left = 0, right = sortedByPrice.size() - 1;
        while (left < right) {
            double sum = sortedByPrice.get(left).getPrice() + sortedByPrice.get(right).getPrice();
            if (Math.abs(sum - target) < 0.001) {
                return Optional.of(new Product[]{sortedByPrice.get(left), sortedByPrice.get(right)});
            } else if (sum < target) {
                left++;
            } else {
                right--;
            }
        }
        return Optional.empty();
    }

    /**
     * SESSION 11 — Sliding Window.
     * Find the cheapest contiguous run of `windowSize` products from a list
     * already sorted by price. O(n) — slide the window instead of
     * recomputing the sum of every possible window from scratch.
     */
    public List<Product> cheapestBundle(List<Product> sortedByPrice, int windowSize) {
        if (sortedByPrice.size() < windowSize) return Collections.emptyList();

        double windowSum = 0;
        for (int i = 0; i < windowSize; i++) windowSum += sortedByPrice.get(i).getPrice();

        double minSum = windowSum;
        int minStart = 0;

        for (int start = 1; start <= sortedByPrice.size() - windowSize; start++) {
            windowSum = windowSum - sortedByPrice.get(start - 1).getPrice()
                    + sortedByPrice.get(start + windowSize - 1).getPrice();
            if (windowSum < minSum) {
                minSum = windowSum;
                minStart = start;
            }
        }
        return sortedByPrice.subList(minStart, minStart + windowSize);
    }

    /** SESSION 27 preview — Stream API used here to keep this method short and declarative. */
    public List<Product> filterByCategory(String category) {
        return getAll().stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }
}
