package utils;

import User.Staff;
import java.sql.*;
import java.util.HashMap;

public class Product {
    private static int productIdCounter = 0;
    private int productId;
    protected String name;
    protected double price;
    protected int stock;
    protected String categoryID;
    protected String description;
    protected static HashMap<Integer, Product> productsById = new HashMap<>();

    private static final String URL = "jdbc:mysql://localhost:3306/category_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public Product(String productName, double productPrice, int productStock, String productCategoryID, String productDescription) {
        this.productId = ++productIdCounter;
        this.name = productName;
        this.price = productPrice;
        this.stock = productStock;
        this.categoryID = productCategoryID;
        this.description = productDescription;
        productsById.put(this.productId, this);
    }

    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void saveToDatabase() {
        String sql = "INSERT INTO products (name, price, stock, categoryID, description) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, this.name);
            stmt.setDouble(2, this.price);
            stmt.setInt(3, this.stock);
            stmt.setString(4, this.categoryID);
            stmt.setString(5, this.description);
            stmt.executeUpdate();
            System.out.println("Product added: " + this);
        } catch (SQLException e) {
            System.out.println("Error adding product: " + e.getMessage());
        }
    }

    public static Product getProductById(int id) {
        String sql = "SELECT * FROM products WHERE productId = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Product(rs.getString("name"), rs.getDouble("price"), rs.getInt("stock"), rs.getString("categoryID"), rs.getString("description"));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving product: " + e.getMessage());
        }
        return null;
    }

    public void updateStock(int newStock, Staff admin) {
        if (admin != null) {
            this.stock = newStock;
            String sql = "UPDATE products SET stock = ? WHERE name = ?";
            try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, newStock);
                stmt.setString(2, this.name);
                stmt.executeUpdate();
                System.out.println("Stock updated successfully to " + newStock);
            } catch (SQLException e) {
                System.out.println("Error updating stock: " + e.getMessage());
            }
        } else {
            System.out.println("Access denied. Only admins can update stock.");
        }
    }

    public static void deleteProduct(int id, Staff admin) {
        if (admin != null) {
            String sql = "DELETE FROM products WHERE productId = ?";
            try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
                productsById.remove(id);
                System.out.println("Product deleted successfully.");
            } catch (SQLException e) {
                System.out.println("Error deleting product: " + e.getMessage());
            }
        } else {
            System.out.println("Access denied. Only admins can delete products.");
        }
    }

    public int getStock(Staff admin) {
        if (admin != null) {
            return stock;
        } else {
            System.out.println("Access denied. Only admins can view stock.");
            return -1;
        }
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", categoryID='" + categoryID + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
