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
        // this.productId = id > 0 ? id : ++productIdCounter;
        this.productId =  ++productIdCounter;
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
        String sql = "INSERT INTO products (productId, name, price, stock, categoryID, description) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, this.productId);
            stmt.setString(2, this.name);
            stmt.setDouble(3, this.price);
            stmt.setInt(4, this.stock);
            stmt.setString(5, this.categoryID);
            stmt.setString(6, this.description);
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
                return new Product(
                        // rs.getInt("productId"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("stock"),
                        rs.getString("categoryID"),
                        rs.getString("description")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving product: " + e.getMessage());
        }
        return null;
    }

    public void updateStock(int newStock, Staff admin) {
        if (admin != null) {
            int currentStock = getStock(this.productId);
            int updatedStock = currentStock + newStock;

            String sql = "UPDATE products SET stock = ? WHERE productId = ?";
            try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, updatedStock);
                stmt.setInt(2, this.productId);
                stmt.executeUpdate();
                this.stock = updatedStock;
                System.out.println("Stock updated successfully to " + updatedStock);
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

    public int getStock(int productId) {
        int stock = 0;
        String sql = "SELECT stock FROM products WHERE productId = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            ResultSet r = stmt.executeQuery();
            if (r.next()) {
                stock = r.getInt("stock");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving stock: " + e.getMessage());
        }
        return stock;
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
