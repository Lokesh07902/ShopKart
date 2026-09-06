package com.shopkart.algorithms;

import com.shopkart.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SESSION 24 — Introduction to DP: Memoization vs Tabulation
 * SESSION 25 — Classic DP Problems
 */
public class KnapsackSolver {

    /**
     * SESSION 24 — Memoization warm-up.
     * Repeated discount calculations for the SAME cart total are cached
     * instead of recomputed. Trivial here, but demonstrates the core DP idea:
     * "have I already solved this exact subproblem?" before recomputing.
     */
    private final Map<Double, Double> discountCache = new HashMap<>();

    public double memoizedDiscount(double cartTotal, double rate) {
        return discountCache.computeIfAbsent(cartTotal, total -> total - (total * rate));
    }

    /**
     * SESSION 25 — "Best Value Cart": classic 0/1 Knapsack.
     * Given a budget, pick the subset of products that fits within budget
     * while maximizing total combined RATING (as a proxy for "value").
     * Each product can be picked at most once (0/1, not fractional).
     *
     * dp[i][b] = best achievable value using the first i products with budget b.
     * Tabulation (bottom-up table) is used here — O(n * budget) time & space,
     * avoiding the recursion-call overhead memoization would add.
     */
    public List<Product> bestValueCart(List<Product> products, int budget) {
        int n = products.size();
        double[][] dp = new double[n + 1][budget + 1];

        for (int i = 1; i <= n; i++) {
            Product p = products.get(i - 1);
            int cost = (int) Math.round(p.getPrice());
            for (int b = 0; b <= budget; b++) {
                if (cost > b) {
                    dp[i][b] = dp[i - 1][b]; // can't afford it — skip
                } else {
                    // max(skip this product, take this product + best of remaining budget)
                    dp[i][b] = Math.max(dp[i - 1][b], dp[i - 1][b - cost] + p.getRating());
                }
            }
        }

        // Backtrack through the table to find WHICH products were chosen
        List<Product> chosen = new ArrayList<>();
        int remainingBudget = budget;
        for (int i = n; i >= 1; i--) {
            if (dp[i][remainingBudget] != dp[i - 1][remainingBudget]) {
                Product p = products.get(i - 1);
                chosen.add(p);
                remainingBudget -= (int) Math.round(p.getPrice());
            }
        }
        return chosen;
    }
}
