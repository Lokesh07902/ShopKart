package com.shopkart.web;

import com.shopkart.exception.OutOfStockException;
import com.shopkart.model.Product;
import com.shopkart.repository.ProductRepository;
import com.shopkart.structures.CartActionStack;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * SESSION 30 — A tiny in-memory single-session cart, backed by the
 * CartActionStack from Session 14, exposed over HTTP.
 */
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final ProductRepository productRepository;
    private final List<Product> cartItems = new ArrayList<>();
    private final CartActionStack actionStack = new CartActionStack();

    public CartController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public List<Product> viewCart() {
        return cartItems;
    }

    /** POST /api/cart/add/{productId} */
    @PostMapping("/add/{productId}")
    public List<Product> addToCart(@PathVariable String productId) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) return cartItems;

        Product product = productOpt.get();
        if (product.getStock() <= 0) {
            throw new OutOfStockException(product.getName(), 1, product.getStock());
        }
        cartItems.add(product);
        actionStack.push(CartActionStack.ActionType.ADD, product);
        return cartItems;
    }

    /** POST /api/cart/undo */
    @PostMapping("/undo")
    public List<Product> undoLastAction() {
        CartActionStack.CartAction reversed = actionStack.undo();
        if (reversed != null && reversed.type() == CartActionStack.ActionType.REMOVE) {
            cartItems.remove(reversed.product());
        }
        return cartItems;
    }
}
