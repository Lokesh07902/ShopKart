package com.shopkart.web;

import com.shopkart.model.Product;
import com.shopkart.repository.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * SESSION 30 — REST controller. Each @GetMapping/@PostMapping exposes a
 * method over HTTP. Notice this class contains almost NO logic of its own —
 * it just calls into ProductRepository (Sessions 8-11), which is exactly
 * the point: Spring Boot is a thin HTTP layer on top of the DSA work
 * already done, not a replacement for it.
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /** GET /api/products?category=Electronics */
    @GetMapping
    public List<Product> getProducts(@RequestParam(required = false) String category) {
        if (category != null) return productRepository.filterByCategory(category);
        return productRepository.getAll();
    }

    /** GET /api/products/categories */
    @GetMapping("/categories")
    public List<String> getCategories() {
        return productRepository.getAllCategories().stream().sorted().toList();
    }

    /** GET /api/products/{id} */
    @GetMapping("/{id}")
    public Product getProduct(@PathVariable String id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElse(null); // Spring returns 200+null; a ResponseEntity 404 is a nice extra-credit improvement
    }

    /** POST /api/products */
    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        productRepository.add(product);
        return product;
    }
}
