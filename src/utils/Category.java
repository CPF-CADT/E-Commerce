package utils;
import utils.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utils.Product;

public class Category {
    protected String ID;
    protected String name;
    protected String description;
    protected int numberOfProducts;

    // Database connection details
    private static final String URL = "jdbc:mysql://localhost:3306/category_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public Category(String ID, String name, String description, int numberOfProducts) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.numberOfProducts = numberOfProducts;
    }

    // Method to connect to the database
    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // CREATE: Add category to the database
    public static void addCategory(Category category, String userRole) {
        if (!userRole.equalsIgnoreCase("admin")) {
            System.out.println("Access denied! Only admins can add Category.");
            return;
        }

        String sql = "INSERT INTO categories (ID, name, description, numberOfProducts) VALUES (?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category.ID);
            stmt.setString(2, category.name);
            stmt.setString(3, category.description);
            stmt.setInt(4, category.numberOfProducts);
            stmt.executeUpdate();
            System.out.println("Category added: " + category);
        } catch (SQLException e) {
            System.out.println("Error adding category: " + e.getMessage());
        }
    }

    // RETRIEVE: Get category by ID
    public static Category getCategory(String categoryID) {
        String sql = "SELECT * FROM categories WHERE ID = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoryID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Category(rs.getString("ID"), rs.getString("name"), rs.getString("description"), rs.getInt("numberOfProducts"));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving category: " + e.getMessage());
        }
        return null;
    }

    // Retrieve products by category ID
    public static List<Product> getProductByCategory(String categoryID) {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE categoryID = ?";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoryID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Product product = new Product(
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("stock"),
                    rs.getString("categoryID"),
                    rs.getString("description")
                );
                productList.add(product);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving products: " + e.getMessage());
        }
        return productList;
    }

    // UPDATE: Modify category name and description
    public static void updateCategory(String categoryID, String newName, String newDescription, String userRole) {
        if (!userRole.equalsIgnoreCase("admin")) {
            System.out.println("Access denied! Only admins can update Category.");
            return;
        }

        String sql = "UPDATE categories SET name = ?, description = ? WHERE ID = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newName);
            stmt.setString(2, newDescription);
            stmt.setString(3, categoryID);

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

    // DELETE: Remove category
    public static void deleteCategory(String categoryID, String userRole) {
        if (!userRole.equalsIgnoreCase("admin")) {
            System.out.println("Access denied! Only admins can delete Category.");
            return;
        }

        String sql = "DELETE FROM categories WHERE ID = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoryID);

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

    // LIST: Retrieve all categories
    public static void listAllCategories() {
        String sql = "SELECT * FROM categories";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("Category List:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getString("ID") + ", Name: " + rs.getString("name") +
                    ", Description: " + rs.getString("description") + ", Products: " + rs.getInt("numberOfProducts"));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving categories: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Category [ID=" + ID + ", name=" + name + ", description=" + description + ", numberOfProducts="
                + numberOfProducts + "]";
    }
}
