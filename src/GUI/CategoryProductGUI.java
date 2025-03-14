package GUI;

import Database.MySQLConnection;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class CategoryProductGUI {
    public static void main(String[] args) {
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

        // Load categories from database
        List<String> categories = getCategories();
        if (categories.isEmpty()) {
            categoryListModel.addElement("No Categories Found");
        } else {
            for (String category : categories) {
                categoryListModel.addElement(category);
            }
        }

        // Button action: load products when a category is selected
        viewProductsButton.addActionListener(e -> {
            String selectedCategory = categoryList.getSelectedValue();
            if (selectedCategory != null && !selectedCategory.equals("No Categories Found")) {
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
        String sql = "SELECT name FROM Category";

        Connection conn = MySQLConnection.getConnection();
        if (conn == null) {
            System.out.println("Database connection is null. Cannot fetch categories.");
            return categories;
        }

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                categories.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching categories: " + e.getMessage());
        }
        return categories;
    }

    private static List<String> getProducts(String categoryName) {
        List<String> products = new ArrayList<>();
        String sql = "SELECT name FROM Product WHERE category = ?";

        Connection conn = MySQLConnection.getConnection();
        if (conn == null) {
            System.out.println("Database connection is null. Cannot fetch products.");
            return products;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoryName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                products.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching products: " + e.getMessage());
        }
        return products;
    }
}
