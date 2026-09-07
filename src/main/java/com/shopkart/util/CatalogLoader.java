package com.shopkart.util;

import com.shopkart.model.Product;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * SESSION 7 — Custom Exceptions & Java I/O
 * Reads the product catalog from the classpath (src/main/resources/data/products.csv)
 * instead of a raw filesystem path. This matters for deployment: a relative
 * filesystem path like "data/products.csv" only works if the app's current
 * working directory happens to match the project layout — which breaks on
 * most cloud hosts (Railway, Render, etc.) where the working directory at
 * runtime is unpredictable. A classpath resource, by contrast, is bundled
 * INSIDE the jar itself at build time, so it's found identically whether
 * running locally, from an IDE, or deployed on any server.
 */
public class CatalogLoader {

    public static List<Product> loadFromCsv(String classpathFile) {
        List<Product> products = new ArrayList<>();
        try (InputStream is = CatalogLoader.class.getClassLoader().getResourceAsStream(classpathFile)) {
            if (is == null) {
                System.err.println("Could not find classpath resource: " + classpathFile);
                return products;
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
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
            }
        } catch (Exception e) {
            System.err.println("Could not load catalog from " + classpathFile + ": " + e.getMessage());
        }
        return products;
    }
}