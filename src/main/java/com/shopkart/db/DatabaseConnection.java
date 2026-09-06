package com.shopkart.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * SESSION 29 — JDBC & Servlets: Database Connectivity
 * Uses H2 (file-based, zero-install database — perfect for a college
 * project / viva demo, no MySQL server setup needed). Swap the URL for a
 * real MySQL/PostgreSQL connection string in production; the JDBC code
 * below doesn't change either way — that's the whole point of JDBC as an
 * abstraction over the specific database engine.
 *
 * Requires the H2 dependency in pom.xml (already added — see Maven build).
 */
public class DatabaseConnection {

    private static final String URL = "jdbc:h2:./data/shopkartdb";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /** Creates the products table if it doesn't exist yet. Run once at startup. */
    public static void initSchema() {
        String sql = """
            CREATE TABLE IF NOT EXISTS products (
                id VARCHAR(10) PRIMARY KEY,
                name VARCHAR(100) NOT NULL,
                price DOUBLE NOT NULL,
                category VARCHAR(50),
                stock INT NOT NULL,
                rating DOUBLE
            )
            """;
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Schema init failed: " + e.getMessage());
        }
    }
}
