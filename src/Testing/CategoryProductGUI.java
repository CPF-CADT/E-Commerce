package Testing;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class CategoryProductGUI {
    private static final String URL = "jdbc:mysql://localhost:3306/category_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

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
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
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
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
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