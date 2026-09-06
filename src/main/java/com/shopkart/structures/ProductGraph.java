package com.shopkart.structures;

import com.shopkart.model.Product;

import java.util.*;

/**
 * SESSION 19 — Graph Terminology & Representations
 * SESSION 20 — Graph Traversals: BFS & DFS
 *
 * Models "frequently bought together": each product is a vertex, and an
 * undirected edge means two products appeared in the same order. Stored as
 * an ADJACENCY LIST (Map<Product, List<Product>>) rather than an adjacency
 * MATRIX because the graph is sparse — most product pairs are never bought
 * together, so a matrix would waste O(n^2) space on mostly-empty cells.
 */
public class ProductGraph {

    private final Map<Product, List<Product>> adjacency = new HashMap<>();

    public void addEdge(Product a, Product b) {
        adjacency.computeIfAbsent(a, k -> new ArrayList<>()).add(b);
        adjacency.computeIfAbsent(b, k -> new ArrayList<>()).add(a);
    }

    /**
     * SESSION 20 — BFS.
     * "Customers who bought X also bought…" up to `maxHops` away. BFS is the
     * right choice over DFS here because it explores level-by-level, so hop
     * distance == recommendation relevance (closer = bought together more directly).
     */
    public List<Product> recommendBFS(Product start, int maxHops) {
        List<Product> result = new ArrayList<>();
        Set<Product> visited = new HashSet<>();
        Queue<Product> queue = new LinkedList<>();
        Map<Product, Integer> hopCount = new HashMap<>();

        queue.offer(start);
        visited.add(start);
        hopCount.put(start, 0);

        while (!queue.isEmpty()) {
            Product current = queue.poll();
            int hops = hopCount.get(current);
            if (hops >= maxHops) continue;

            for (Product neighbor : adjacency.getOrDefault(current, Collections.emptyList())) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    hopCount.put(neighbor, hops + 1);
                    queue.offer(neighbor);
                    if (!neighbor.equals(start)) result.add(neighbor);
                }
            }
        }
        return result;
    }

    /**
     * SESSION 20 — DFS.
     * Used here as a data-integrity check: the category tree (Session 16)
     * should never accidentally contain a cycle. DFS with a visited set
     * detects a cycle if we ever reach an already-visited node via a
     * different path.
     */
    public boolean hasCycleDFS(Product start) {
        Set<Product> visited = new HashSet<>();
        return dfsCycleCheck(start, null, visited);
    }

    private boolean dfsCycleCheck(Product current, Product parent, Set<Product> visited) {
        visited.add(current);
        for (Product neighbor : adjacency.getOrDefault(current, Collections.emptyList())) {
            if (!visited.contains(neighbor)) {
                if (dfsCycleCheck(neighbor, current, visited)) return true;
            } else if (!neighbor.equals(parent)) {
                return true; // reached a visited node that isn't our immediate parent -> cycle
            }
        }
        return false;
    }
}
