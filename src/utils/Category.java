package utils;

import java.sql.*;

public class Category {
    protected String ID;
    protected String name;

    // Database connection details
    private static final String URL = "jdbc:mysql://localhost:3306/category_db";
    private static final String USER = "root"; // Change if needed
    private static final String PASSWORD = ""; // Change if needed

    public Category(String ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    // Method to connect to the database
    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // CREATE: Add category to the database
    public static void addCategory(Category category, String userRole) {
        if (!userRole.equalsIgnoreCase("admin")) {
            System.out.println("Access denied! Only admins can add categories.");
            return;
        }

        String sql = "INSERT INTO categories (ID, name) VALUES (?, ?)";

        try (Connection conn = connect();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category.ID);
            stmt.setString(2, category.name);
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
                return new Category(rs.getString("ID"), rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving category: " + e.getMessage());
        }
        return null;
    }

    // UPDATE: Modify category name
    public static void updateCategory(String categoryID, String newName, String userRole) {
        if (!userRole.equalsIgnoreCase("admin")) {
            System.out.println("Access denied! Only admins can update categories.");
            return;
        }

        String sql = "UPDATE categories SET name = ? WHERE ID = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newName);
            stmt.setString(2, categoryID);

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
            System.out.println("Access denied! Only admins can delete categories.");
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
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("Categories List:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getString("ID") + ", Name: " + rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving categories: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Category [ID=" + ID + ", name=" + name + "]";
    }

    public static void main(String[] args) {
        // Sample Usage
        Category cat1 = new Category("001", "Electronics");
        Category cat2 = new Category("002", "Groceries");

        addCategory(cat1, "admin");
        addCategory(cat2, "admin");

        System.out.println(getCategory("001"));

        updateCategory("001", "Consumer Electronics", "admin");

        listAllCategories();

        deleteCategory("002", "admin");
    }
}
