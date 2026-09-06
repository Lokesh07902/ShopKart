package com.shopkart.structures;

import com.shopkart.model.Product;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * SESSION 14 — Stack Implementation & Applications
 * Undo is a textbook stack use case: the most recent action is the first one
 * to be reversed (LIFO). We use ArrayDeque as the underlying stack (Java's
 * recommended stack implementation — faster than the legacy Stack class)
 * but wrap it so cart code just calls push()/undo() without knowing that.
 */
public class CartActionStack {

    public enum ActionType { ADD, REMOVE }

    public record CartAction(ActionType type, Product product) {}

    private final Deque<CartAction> actions = new ArrayDeque<>();

    public void push(ActionType type, Product product) {
        actions.push(new CartAction(type, product));
    }

    /** Pops the last action and returns its OPPOSITE so the caller can reverse it. */
    public CartAction undo() {
        if (actions.isEmpty()) return null;
        CartAction last = actions.pop();
        ActionType opposite = last.type() == ActionType.ADD ? ActionType.REMOVE : ActionType.ADD;
        return new CartAction(opposite, last.product());
    }

    public boolean isEmpty() { return actions.isEmpty(); }
}
