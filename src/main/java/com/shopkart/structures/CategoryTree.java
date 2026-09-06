package com.shopkart.structures;

import java.util.ArrayList;
import java.util.List;

/**
 * SESSION 16 — Binary Trees: Representation & Traversals
 * Models a (simplified, binary) category hierarchy, e.g.
 *          Electronics
 *          /         \
 *      Phones      Laptops
 *        /
 *  Accessories
 * A general catalog would be an n-ary tree, but the module scope is binary
 * trees, so each node has at most left/right — enough to demonstrate
 * Inorder / Preorder / Postorder traversal correctly.
 */
public class CategoryTree {

    static class Node {
        String category;
        Node left, right;
        Node(String category) { this.category = category; }
    }

    private Node root;

    public CategoryTree(String rootCategory) {
        root = new Node(rootCategory);
    }

    public void insertLeft(String parent, String child) {
        Node p = find(root, parent);
        if (p != null) p.left = new Node(child);
    }

    public void insertRight(String parent, String child) {
        Node p = find(root, parent);
        if (p != null) p.right = new Node(child);
    }

    private Node find(Node node, String category) {
        if (node == null) return null;
        if (node.category.equals(category)) return node;
        Node left = find(node.left, category);
        return left != null ? left : find(node.right, category);
    }

    public List<String> inorder() {
        List<String> out = new ArrayList<>();
        inorder(root, out);
        return out;
    }
    private void inorder(Node n, List<String> out) {
        if (n == null) return;
        inorder(n.left, out);
        out.add(n.category);
        inorder(n.right, out);
    }

    public List<String> preorder() {
        List<String> out = new ArrayList<>();
        preorder(root, out);
        return out;
    }
    private void preorder(Node n, List<String> out) {
        if (n == null) return;
        out.add(n.category);
        preorder(n.left, out);
        preorder(n.right, out);
    }

    public List<String> postorder() {
        List<String> out = new ArrayList<>();
        postorder(root, out);
        return out;
    }
    private void postorder(Node n, List<String> out) {
        if (n == null) return;
        postorder(n.left, out);
        postorder(n.right, out);
        out.add(n.category);
    }
}
