package utils;
<<<<<<< HEAD
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
=======

import java.sql.*;
>>>>>>> main
import java.util.ArrayList;
import java.util.List;

public class Category {
    protected String categoryId;
    protected String name;
    protected String description;
    protected int numberOfProducts;

<<<<<<< HEAD
    // Database connection details
    private static final String URL = "jdbc:mysql://localhost:3306/";
=======
    private static final String URL = "jdbc:mysql://localhost:3306/e_commerce";
>>>>>>> main
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public Category(String categoryId, String name, String description, int numberOfProducts) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.numberOfProducts = numberOfProducts;
    }

    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void saveToDatabase(String userRole) {
        if (!userRole.equalsIgnoreCase("admin")) {
            System.out.println("Access denied! Only admins can add categories.");
            return;
        }
        String sql = "INSERT INTO Category (categoryId, name, description) VALUES (?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, this.categoryId);
            stmt.setString(2, this.name);
            stmt.setString(3, this.description);
            stmt.executeUpdate();
            System.out.println("Category added: " + this);
        } catch (SQLException e) {
            System.out.println("Error adding category: " + e.getMessage());
        }
    }

    public static Category getCategoryById(String categoryId) {
        String sql = "SELECT * FROM Category WHERE categoryId = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoryId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Category(
                        rs.getString("categoryId"),
                        rs.getString("name"),
                        rs.getString("description"),
                        0 
                );
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving category: " + e.getMessage());
        }
        return null;
    }

    public static List<Product> getProductsByCategory(String categoryId) {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT * FROM Product WHERE categoryId = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoryId);
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
                productList.add(product);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving products: " + e.getMessage());
        }
        return productList;
    }

    public void updateCategory(String newName, String newDescription, String userRole) {
        if (!userRole.equalsIgnoreCase("admin")) {
            System.out.println("Access denied! Only admins can update categories.");
            return;
        }
        String sql = "UPDATE Category SET name = ?, description = ? WHERE categoryId = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newName);
            stmt.setString(2, newDescription);
            stmt.setString(3, this.categoryId);
            int updatedRows = stmt.executeUpdate();
            if (updatedRows > 0) {
                System.out.println("Category updated.");
            } else {
                System.out.println("Category not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating category: " + e.getMessage());
        }
    }

    public static void deleteCategory(String categoryId, String userRole) {
        if (!userRole.equalsIgnoreCase("admin")) {
            System.out.println("Access denied! Only admins can delete categories.");
            return;
        }
        String sql = "DELETE FROM Category WHERE categoryId = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoryId);
            int deletedRows = stmt.executeUpdate();
            if (deletedRows > 0) {
                System.out.println("Category deleted.");
            } else {
                System.out.println("Category not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting category: " + e.getMessage());
        }
    }

    public static void listAllCategories() {
        String sql = "SELECT * FROM Category";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            System.out.println("Category List:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getString("categoryId") + ", Name: " + rs.getString("name") + ", Description: " + rs.getString("description"));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving categories: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryId='" + categoryId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
