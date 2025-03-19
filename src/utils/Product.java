package utils;

import Database.MySQLConnection;
import User.Staff;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Product {
    private final String productId;  // Changed to String to match the database
    public String name;
    public double price;
    public int stock;
    public String categoryID;
    public String description;
    public static HashMap<String, Product> productsById = new HashMap<>();
    
    public String getName() {
        return name;
    }

    private static final String URL = "jdbc:mysql://localhost:3306/e_commerce";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public Product(String productId, String productName, double productPrice, int productStock, String productCategoryID, String productDescription) {
        this.productId = productId;
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
        String sql = "INSERT INTO Product (productId, name, price, stock, categoryId, description) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, this.productId);
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
    
    // public static List<Product> getProductsByCategory(String name) {
    //     List<Product> products = new ArrayList<>();
    //     String sql = "SELECT * FROM Product WHERE name = ?";
            
    //     try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
    //         stmt.setString(1, name);
    //         ResultSet rs = stmt.executeQuery();
            
    //         while (rs.next()) {
    //             Product product = new Product(
    //                     rs.getString("productId"),
    //                     rs.getString("name"),
    //                     rs.getDouble("price"),
    //                     rs.getInt("stock"),
    //                     rs.getString("categoryId"),
    //                     rs.getString("description")
    //             );
    //             products.add(product);
    //         }
    //     } catch (SQLException e) {
    //         System.out.println("Error retrieving products by category: " + e.getMessage());
    //     }
        
    //     return products;
    // }

    public static List<Product> getProductsByCategory(String categoryName) {
        List<Product> products = new ArrayList<>();
        try (Connection conn = MySQLConnection.getConnection()) {
            String query = "SELECT * FROM Product WHERE categoryId = (SELECT categoryId FROM Category WHERE name = ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, categoryName);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        products.add(new Product(
                            rs.getString("productId"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getInt("stock"),
                            rs.getString("categoryId"),
                            rs.getString("description")
                        ));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }


    public void updateStock(int newStock, Staff admin) {
        if (admin != null) {
            int updatedStock = this.stock + newStock;
            String sql = "UPDATE Product SET stock = ? WHERE productId = ?";
            try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, updatedStock);
                stmt.setString(2, this.productId);
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

    public static void deleteProduct(String id, Staff admin) {
        if (admin != null) {
            String sql = "DELETE FROM Product WHERE productId = ?";
            try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, id);
                stmt.executeUpdate();
                productsById.remove(Integer.valueOf(id));
                System.out.println("Product deleted successfully.");
            } catch (SQLException e) {
                System.out.println("Error deleting product: " + e.getMessage());
            }
        } else {
            System.out.println("Access denied. Only admins can delete products.");
        }
    }

    public int fetchStockFromDatabase() {
        String sql = "SELECT stock FROM Product WHERE productId = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
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

    @Override
    public String toString() {
        return "Product{" +
                "productId='" + productId + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", categoryID='" + categoryID + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
