package GUI;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class CategoryProductGUI {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                System.out.println("Connection successful!");
            } else {
                System.out.println("Connection failed!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    
        SwingUtilities.invokeLater(CategoryProductGUI::createGUI);
    }

    private static void createGUI() {
        JFrame frame = new JFrame("E-Commerce System");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Select a Category", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        frame.add(titleLabel, BorderLayout.NORTH);

        DefaultListModel<String> categoryListModel = new DefaultListModel<>();
        JList<String> categoryList = new JList<>(categoryListModel);
        frame.add(new JScrollPane(categoryList), BorderLayout.WEST);

        DefaultListModel<String> productListModel = new DefaultListModel<>();
        JList<String> productList = new JList<>(productListModel);
        frame.add(new JScrollPane(productList), BorderLayout.CENTER);

        JButton viewProductsButton = new JButton("View Products");
        frame.add(viewProductsButton, BorderLayout.SOUTH);

        List<String> categories = getCategories();
        for (String category : categories) {
            categoryListModel.addElement(category);
        }

        viewProductsButton.addActionListener(e -> {
            String selectedCategory = categoryList.getSelectedValue();
            if (selectedCategory != null) {
                productListModel.clear();
                List<String> products = getProducts(selectedCategory);
                for (String product : products) {
                    productListModel.addElement(product);
                }
            }
        });

        frame.setVisible(true);
    }

    private static List<String> getCategories() {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT name FROM categories";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                categories.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    private static List<String> getProducts(String categoryName) {
        List<String> products = new ArrayList<>();
        String sql = "SELECT name FROM products WHERE categoryID = (SELECT ID FROM categories WHERE name = ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoryName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                products.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
}

class DatabaseConnection {
    private static Connection connection = null;
    private static final String HOST = "mysql-436bbed-student-f997.h.aivencloud.com";
    private static final String PORT = "22721";
    private static final String DATABASE_NAME = "e_commerce";
    private static final String USERNAME = "avnadmin";
    private static final String PASSWORD = "AVNS_ikPHseBNRutTggiBZ6w";

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE_NAME + "?sslmode=require", USERNAME, PASSWORD);
                System.out.println("Connected to MySQL successfully!");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("No driver found: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("ERROR: Could not connect to the server. Please check your network connection. " + e.getMessage());
        }
        return connection;
    }
}
