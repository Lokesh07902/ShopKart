package com.shopkart.repository;

import java.util.*;
import java.util.function.Function;

/**
 * SESSION 8 — Collections Framework & Generics
 * <T> means this class works for ANY entity type without rewriting it —
 * ProductRepository and CustomerRepository both extend this. The internal
 * storage is a List (Session 9) but lookups go through a HashMap keyed by id
 * (Session 10) for O(1) access instead of O(n) scanning.
 */
public class Repository<T> {
    private final List<T> items = new ArrayList<>();
    private final Map<String, T> byId = new HashMap<>();
    private final Function<T, String> idExtractor;

    public Repository(Function<T, String> idExtractor) {
        this.idExtractor = idExtractor;
    }

    public void add(T item) {
        items.add(item);
        byId.put(idExtractor.apply(item), item);
    }

    public void remove(String id) {
        T item = byId.remove(id);
        if (item != null) items.remove(item);
    }

    /** O(1) lookup via HashMap — see Session 10 comment in ProductRepository. */
    public Optional<T> findById(String id) {
        return Optional.ofNullable(byId.get(id)); // Session 28: Optional instead of null
    }

    public List<T> getAll() {
        return Collections.unmodifiableList(items);
    }

    public int size() {
        return items.size();
    }
}
