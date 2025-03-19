package utils;

import Database.MySQLConnection;
import User.Staff;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Product {
    private final String productId;  // Unique product identifier
    private String name, categoryId, description;
    private double price;
    private int stock;

    // Store products in a HashMap for quick retrieval by ID
    private static final HashMap<String, Product> productsById = new HashMap<>();

    public Product(String productId, String name, double price, int stock, String categoryId, String description) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.categoryId = categoryId;
        this.description = description;
        productsById.put(this.productId, this); // Add to cache
    }

    // Save product to database
    public void saveToDatabase() {
        String sql = "INSERT INTO Product (productId, name, price, stock, categoryId, description) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = MySQLConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, this.productId);
            stmt.setString(2, this.name);
            stmt.setDouble(3, this.price);
            stmt.setInt(4, this.stock);
            stmt.setString(5, this.categoryId);
            stmt.setString(6, this.description);
            stmt.executeUpdate();
            System.out.println("Product added: " + this);
        } catch (SQLException e) {
            System.out.println("Error adding product: " + e.getMessage());
        }
    }

    // Retrieve products by category
    public static List<Product> getProductsByCategory(String categoryName) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM Product WHERE categoryId = (SELECT categoryId FROM Category WHERE name = ?)";

        try (Connection conn = MySQLConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoryName);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Product product = new Product(
                        rs.getString("productId"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("stock"),
                        rs.getString("categoryId"),
                        rs.getString("description")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving products by category: " + e.getMessage());
        }
        return products;
    }

    // Fetch product by ID
    public static Product getProductById(String productId) {
        if (productsById.containsKey(productId)) {
            return productsById.get(productId);
        }

        String sql = "SELECT * FROM Product WHERE productId = ?";
        try (Connection conn = MySQLConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Product(
                        rs.getString("productId"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("stock"),
                        rs.getString("categoryId"),
                        rs.getString("description")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving product: " + e.getMessage());
        }
        return null;
    }

    // Update product stock (Only admins can perform this)
    public void updateStock(int newStock, Staff admin) {
        if (admin == null) {
            System.out.println("Access denied. Only admins can update stock.");
            return;
        }

        int updatedStock = this.stock + newStock;
        String sql = "UPDATE Product SET stock = ? WHERE productId = ?";

        try (Connection conn = MySQLConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, updatedStock);
            stmt.setString(2, this.productId);
            stmt.executeUpdate();
            this.stock = updatedStock;
            System.out.println("Stock updated successfully to " + updatedStock);
        } catch (SQLException e) {
            System.out.println("Error updating stock: " + e.getMessage());
        }
    }

    // Delete product from database (Only admins can perform this)
    public static void deleteProduct(String productId, Staff admin) {
        if (admin == null) {
            System.out.println("Access denied. Only admins can delete products.");
            return;
        }

        String sql = "DELETE FROM Product WHERE productId = ?";
        try (Connection conn = MySQLConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, productId);
            stmt.executeUpdate();
            productsById.remove(productId);
            System.out.println("Product deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Error deleting product: " + e.getMessage());
        }
    }

    // Fetch stock from database
    public int fetchStockFromDatabase() {
        String sql = "SELECT stock FROM Product WHERE productId = ?";
        try (Connection conn = MySQLConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, this.productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("stock");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving stock: " + e.getMessage());
        }
        return 0;
    }

    // Getters
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getProductId() { return productId; }
    public int getStock() { return stock; }
    public String getCategoryId() { return categoryId; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return "Product{" +
                "productId='" + productId + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", categoryId='" + categoryId + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
