package GUI;

import Database.MySQLConnection;
import User.Staff;
import utils.Product;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryProductGUI extends JFrame {

    private static DefaultListModel<String> categoryListModel;
    private static DefaultListModel<String> productListModel;
    private static JList<String> categoryList;
    private static JList<String> productList;
    private static JTextArea productDetailsArea;
    private static String userId;

    private static List<Product> cart = new ArrayList<>();  // Cart to store the selected products

    public static void main(String[] args) {
        if (args != null && args.length > 0) {
            userId = args[0];
        }
        SwingUtilities.invokeLater(CategoryProductGUI::createGUI);
    }

    public static void createGUI() {
        JFrame frame = new JFrame("E-Commerce System");
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        // Title panel
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("E-Commerce Product Browser", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel);
        frame.add(titlePanel, BorderLayout.NORTH);

        // Main content panel with categories and products
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(250);

        // Category panel
        JPanel categoryPanel = new JPanel(new BorderLayout());
        JLabel categoryLabel = new JLabel("Categories", SwingConstants.CENTER);
        categoryLabel.setFont(new Font("Arial", Font.BOLD, 16));
        categoryPanel.add(categoryLabel, BorderLayout.NORTH);

        categoryListModel = new DefaultListModel<>();
        categoryList = new JList<>(categoryListModel);
        categoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        categoryPanel.add(new JScrollPane(categoryList), BorderLayout.CENTER);

        // Product panel
        JPanel productPanel = new JPanel(new BorderLayout());
        JLabel productLabel = new JLabel("Products", SwingConstants.CENTER);
        productLabel.setFont(new Font("Arial", Font.BOLD, 16));
        productPanel.add(productLabel, BorderLayout.NORTH);

        productListModel = new DefaultListModel<>();
        productList = new JList<>(productListModel);
        productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productPanel.add(new JScrollPane(productList), BorderLayout.CENTER);

        // Product details panel
        JPanel detailsPanel = new JPanel(new BorderLayout());
        JLabel detailsLabel = new JLabel("Product Details", SwingConstants.CENTER);
        detailsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        detailsPanel.add(detailsLabel, BorderLayout.NORTH);

        productDetailsArea = new JTextArea();
        productDetailsArea.setEditable(false);
        productDetailsArea.setLineWrap(true);
        productDetailsArea.setWrapStyleWord(true);
        detailsPanel.add(new JScrollPane(productDetailsArea), BorderLayout.CENTER);

        // Right side split pane for products and details
        JSplitPane rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        rightSplitPane.setDividerLocation(250);
        rightSplitPane.setTopComponent(productPanel);
        rightSplitPane.setBottomComponent(detailsPanel);

        splitPane.setLeftComponent(categoryPanel);
        splitPane.setRightComponent(rightSplitPane);

        frame.add(splitPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton refreshButton = new JButton("Refresh Data");
        buttonPanel.add(refreshButton);

        JButton exitButton = new JButton("Exit");
        buttonPanel.add(exitButton);
        
        // Add "Buy" and "View Cart" buttons
        JButton buyButton = new JButton("Buy Product");
        buttonPanel.add(buyButton);

        JButton viewCartButton = new JButton("View Cart");
        buttonPanel.add(viewCartButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Load categories from database
        loadCategories();

        // Add event listeners
        categoryList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedCategory = categoryList.getSelectedValue();
                    if (selectedCategory != null) {
                        loadProductsByCategory(selectedCategory);
                    }
                }
            }
        });

        productList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedProduct = productList.getSelectedValue();
                    if (selectedProduct != null) {
                        displayProductDetails(selectedProduct);
                    }
                }
            }
        });

        refreshButton.addActionListener(e -> loadCategories());

        exitButton.addActionListener(e -> {
            MySQLConnection.closeConnection();
            System.exit(0);
        });

        // Action listener for Buy Product button
        buyButton.addActionListener(e -> {
            String selectedProduct = productList.getSelectedValue();
            if (selectedProduct != null) {
                addToCart(selectedProduct);
                // After adding to cart, open the payment window
                PaymentGUI paymentGUI = new PaymentGUI();
                paymentGUI.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a product to buy.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Action listener for View Cart button
        viewCartButton.addActionListener(e -> viewCart());

        frame.setVisible(true);
    }

    private static void loadCategories() {
        categoryListModel.clear();
        productListModel.clear();
        productDetailsArea.setText("");

        try (Connection conn = MySQLConnection.getConnection()) {
            // Check if connection is valid
            if (conn == null || conn.isClosed()) {
                JOptionPane.showMessageDialog(null, 
                    "Error: Unable to establish a database connection.",
                    "Database Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Get all categories from the database
            String sql = "SELECT name FROM Category";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    categoryListModel.addElement(rs.getString("name"));
                }
            }

            if (categoryListModel.isEmpty()) {
                categoryListModel.addElement("No categories found");
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Detailed SQL error
            JOptionPane.showMessageDialog(null, 
                "Error loading categories: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void loadProductsByCategory(String categoryName) {
        productListModel.clear();
        productDetailsArea.setText("");

        // Fetch products from the database using the Product class
        List<Product> products = new ArrayList<>();
        
        try (Connection conn = MySQLConnection.getConnection()) {
            // Check if connection is valid
            if (conn == null || conn.isClosed()) {
                JOptionPane.showMessageDialog(null, 
                    "Error: Unable to establish a database connection.",
                    "Database Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Get products for the selected category
            String sql = "SELECT * FROM Product WHERE categoryId = (SELECT categoryId FROM Category WHERE name = ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, categoryName);
                try (ResultSet rs = stmt.executeQuery()) {
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
                }
            }

            if (products.isEmpty()) {
                productListModel.addElement("No products found");
            } else {
                for (Product product : products) {
                    productListModel.addElement(product.getName());
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();  // Detailed SQL error
            JOptionPane.showMessageDialog(null, 
                "Error retrieving products by category: " + e.getMessage(),
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void displayProductDetails(String productName) {
        try (Connection conn = MySQLConnection.getConnection()) {
            String productQuery = "SELECT * FROM Product WHERE name = ?";
            try (PreparedStatement stmt = conn.prepareStatement(productQuery)) {
                stmt.setString(1, productName);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        StringBuilder details = new StringBuilder();
                        details.append("Product ID: ").append(rs.getString("productId")).append("\n\n");
                        details.append("Name: ").append(rs.getString("name")).append("\n\n");
                        details.append("Price: $").append(rs.getDouble("price")).append("\n\n");
                        details.append("Stock: ").append(rs.getInt("stock")).append("\n\n");
                        details.append("Description: ").append(rs.getString("description")).append("\n");

                        productDetailsArea.setText(details.toString());
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Detailed SQL error
            JOptionPane.showMessageDialog(null, 
                "Error displaying product details: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void addToCart(String productName) {
        try (Connection conn = MySQLConnection.getConnection()) {
            String productQuery = "SELECT * FROM Product WHERE name = ?";
            try (PreparedStatement stmt = conn.prepareStatement(productQuery)) {
                stmt.setString(1, productName);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        Product product = new Product(
                            rs.getString("productId"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getInt("stock"),
                            rs.getString("categoryId"),
                            rs.getString("description")
                        );
                        cart.add(product);
                        JOptionPane.showMessageDialog(null, "Product added to cart!");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Error adding product to cart: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void viewCart() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Your cart is empty.", "Cart", JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder cartDetails = new StringBuilder();
            double total = 0;

            for (Product product : cart) {
                cartDetails.append(product.getName()).append(" - $")
                        .append(product.price).append("\n");
                total += product.price;
            }

            cartDetails.append("\nTotal: $").append(total);
            JOptionPane.showMessageDialog(null, cartDetails.toString(), "Your Cart", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
