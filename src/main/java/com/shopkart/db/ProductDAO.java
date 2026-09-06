package com.shopkart.db;

import com.shopkart.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SESSION 29 — Data Access Object (DAO) pattern.
 * Every method opens its own connection in try-with-resources, so
 * connections are always closed even if an exception is thrown — the JDBC
 * equivalent of the try/catch discipline from Session 7.
 * PreparedStatement is used everywhere (not string-concatenated SQL) to
 * prevent SQL injection and let the JDBC driver handle type conversion.
 */
public class ProductDAO {

    public void save(Product p) {
        String sql = "MERGE INTO products KEY(id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getId());
            ps.setString(2, p.getName());
            ps.setDouble(3, p.getPrice());
            ps.setString(4, p.getCategory());
            ps.setInt(5, p.getStock());
            ps.setDouble(6, p.getRating());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Save failed: " + e.getMessage());
        }
    }

    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                products.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Query failed: " + e.getMessage());
        }
        return products;
    }

    public Product findById(String id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            System.err.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    private Product mapRow(ResultSet rs) throws SQLException {
        return new Product(
                rs.getString("id"),
                rs.getString("name"),
                rs.getDouble("price"),
                rs.getString("category"),
                rs.getInt("stock"),
                rs.getDouble("rating")
        );
    }
}
