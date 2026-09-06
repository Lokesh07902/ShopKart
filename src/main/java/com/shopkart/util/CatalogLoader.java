package com.shopkart.util;

import com.shopkart.model.Product;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * SESSION 7 — Custom Exceptions & Java I/O
 * Reads the initial product catalog from a CSV file (data/products.csv)
 * instead of hardcoding products in Java source — the natural next step
 * once Product exists, and the seed data for every structure built after it.
 */
public class CatalogLoader {

    public static List<Product> loadFromCsv(String filePath) {
        List<Product> products = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(",");
                Product p = new Product(
                        parts[0].trim(),
                        parts[1].trim(),
                        Double.parseDouble(parts[2].trim()),
                        parts[3].trim(),
                        Integer.parseInt(parts[4].trim()),
                        Double.parseDouble(parts[5].trim()),
                        parts.length > 6 ? parts[6].trim() : null
                );
                products.add(p);
            }
        } catch (IOException e) {
            System.err.println("Could not load catalog from " + filePath + ": " + e.getMessage());
        }
        return products;
    }
}