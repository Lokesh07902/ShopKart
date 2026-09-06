package com.shopkart.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * SESSION 4 — OOP: Classes, Objects & Access Modifiers
 * Core entity of the whole project. Fields are private (encapsulation) and
 * exposed only through getters/setters. Every data structure built later
 * (HashMap, BST, Heap, Graph, arrays) stores objects of THIS class.
 */
public class Product {

    private final String id;
    private String name;
    private double price;
    private String category;
    private int stock;
    private double rating; // 1.0 - 5.0, used in Session 22 (Counting Sort)
    private int unitsSold; // used in Session 18 (Best-seller Heap)
    private String imageUrl; // display image for the frontend product card

    // @JsonCreator tells Spring/Jackson how to build a Product from a JSON POST
    // body (Session 30) since `id` is final and there's no no-arg constructor.
    // imageUrl is optional — if the JSON body omits it, Jackson leaves it null.
    @JsonCreator
    public Product(@JsonProperty("id") String id, @JsonProperty("name") String name,
                    @JsonProperty("price") double price, @JsonProperty("category") String category,
                    @JsonProperty("stock") int stock, @JsonProperty("rating") double rating,
                    @JsonProperty("imageUrl") String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.stock = stock;
        this.rating = rating;
        this.unitsSold = 0;
        this.imageUrl = imageUrl;
    }

    // Backward-compatible overload for existing call sites (Main.java demo,
    // ProductDAO) that don't supply an image — falls back to a generic
    // placeholder so nothing else in the codebase needs to change.
    public Product(String id, String name, double price, String category, int stock, double rating) {
        this(id, name, price, category, stock, rating,
                "https://placehold.co/300x200/2D5A45/FFFFFF?text=" + name.replace(" ", "+"));
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    public int getUnitsSold() { return unitsSold; }
    public void incrementUnitsSold(int qty) { this.unitsSold += qty; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    /** Used by Session 21/22 sorting and Session 23 binary search comparisons. */
    public int compareByPrice(Product other) {
        return Double.compare(this.price, other.price);
    }

    @Override
    public String toString() {
        return String.format("[%s] %-20s ₹%-8.2f stock:%-3d rating:%.1f cat:%s",
                id, name, price, stock, rating, category);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        return id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}