package com.shopkart.web;

import com.shopkart.algorithms.KnapsackSolver;
import com.shopkart.model.Product;
import com.shopkart.repository.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * SESSION 30 — Exposes the Session 25 knapsack "Best Value Cart" feature as
 * a REST endpoint: given a budget query param, return the best combination
 * of products from the whole catalog.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final ProductRepository productRepository;
    private final KnapsackSolver knapsackSolver = new KnapsackSolver();

    public OrderController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /** GET /api/orders/best-value?budget=2000 */
    @GetMapping("/best-value")
    public List<Product> bestValueForBudget(@RequestParam int budget) {
        return knapsackSolver.bestValueCart(productRepository.getAll(), budget);
    }
}
