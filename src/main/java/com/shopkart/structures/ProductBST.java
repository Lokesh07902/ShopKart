package com.shopkart.structures;

import com.shopkart.model.Product;

import java.util.ArrayList;
import java.util.List;

/**
 *  Binary Search Trees & AVL Trees
 * A SECOND way to store products, keyed by price instead of id (that's the
 * HashMap's job — see ProductRepository). A BST is worth the extra structure
 * because it answers a query the HashMap can't do efficiently: "all products
 * priced between X and Y" (range query) in O(k + log n) instead of O(n).
 *
 * NOTE: this is a plain (unbalanced) BST. If prices are inserted in already-
 * sorted order it degrades to a linked list — O(n) instead of O(log n).
 * That's exactly why AVL trees exist (self-balancing via rotations); it's
 * called out here rather than implemented, per the module's scope.
 */
public class ProductBST {

    static class Node {
        Product product;
        Node left, right;
        Node(Product product) { this.product = product; }
    }

    private Node root;

    public void insert(Product product) {
        root = insert(root, product);
    }
    private Node insert(Node node, Product product) {
        if (node == null) return new Node(product);
        if (product.getPrice() < node.product.getPrice()) node.left = insert(node.left, product);
        else node.right = insert(node.right, product);
        return node;
    }

    public Product search(double price) {
        Node cur = root;
        while (cur != null) {
            if (price == cur.product.getPrice()) return cur.product;
            cur = price < cur.product.getPrice() ? cur.left : cur.right;
        }
        return null;
    }

    /** Range query: every product priced in [minPrice, maxPrice], in sorted order. */
    public List<Product> rangeQuery(double minPrice, double maxPrice) {
        List<Product> result = new ArrayList<>();
        rangeQuery(root, minPrice, maxPrice, result);
        return result;
    }
    private void rangeQuery(Node node, double min, double max, List<Product> result) {
        if (node == null) return;
        if (node.product.getPrice() > min) rangeQuery(node.left, min, max, result);
        if (node.product.getPrice() >= min && node.product.getPrice() <= max) result.add(node.product);
        if (node.product.getPrice() < max) rangeQuery(node.right, min, max, result);
    }
}
