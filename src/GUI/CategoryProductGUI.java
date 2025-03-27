package GUI;

import Database.MySQLConnection;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import utils.Product;

public class CategoryProductGUI extends JFrame {

    private static DefaultListModel<String> categoryListModel;
    private static DefaultListModel<String> productListModel;
    private static JList<String> categoryList;
    private static JList<String> productList;
    private static JTextArea productDetailsArea;
    private static String userId;

    private static List<Product> cart = new ArrayList<>();  // Cart to store the selected products
    private static double totalPrice = 0.0;  // Calculate the total price here
    private static int selectedQuantity = 1;  // Default quantity when adding to the cart

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

        // Add buttons for quantity control and adding to cart
        JButton increaseQuantityButton = new JButton("+");
        JButton decreaseQuantityButton = new JButton("-");

        JButton addToCartButton = new JButton("Add to Cart");

        buttonPanel.add(decreaseQuantityButton);
        buttonPanel.add(increaseQuantityButton);
        buttonPanel.add(new JLabel("Quantity:"));
        buttonPanel.add(addToCartButton);

        // Proceed to Payment button
        JButton proceedToPaymentButton = new JButton("Proceed to Payment");
        buttonPanel.add(proceedToPaymentButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Load categories from database
        loadCategories();

        proceedToPaymentButton.addActionListener(e -> {
            // Send the total price and cart to PaymentGUI
            PaymentGUI paymentWindow = new PaymentGUI(cart, totalPrice);
            paymentWindow.setVisible(true);  // Display the payment window
        });

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
                        selectedQuantity = 1; // Reset quantity when selecting a new product
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

        // Increase Quantity Button
        increaseQuantityButton.addActionListener(e -> {
            selectedQuantity++;
            JOptionPane.showMessageDialog(frame, "Quantity increased to " + selectedQuantity);
        });

        // Decrease Quantity Button
        decreaseQuantityButton.addActionListener(e -> {
            if (selectedQuantity > 1) {
                selectedQuantity--;
                JOptionPane.showMessageDialog(frame, "Quantity decreased to " + selectedQuantity);
            }
        });

        // Add to Cart Button
        addToCartButton.addActionListener(e -> {
            String selectedProduct = productList.getSelectedValue();
            if (selectedProduct != null) {
                addToCart(selectedProduct, selectedQuantity);
                JOptionPane.showMessageDialog(frame, "Product added to cart with quantity: " + selectedQuantity);
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a product to add to cart.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setVisible(true);
    }

    private static void loadCategories() {
        categoryListModel.clear();
        productListModel.clear();
        productDetailsArea.setText("");

        try (Connection conn = MySQLConnection.getConnection()) {
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
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error loading categories: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void loadProductsByCategory(String categoryName) {
        productListModel.clear();
        productDetailsArea.setText("");

        List<Product> products = new ArrayList<>();

        try (Connection conn = MySQLConnection.getConnection()) {
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
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error retrieving products by category: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void displayProductDetails(String productName) {
        try (Connection conn = MySQLConnection.getConnection()) {
            String sql = "SELECT * FROM Product WHERE name = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
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

                        // Display product details including stock/quantity available
                        productDetailsArea.setText("Product: " + product.getName() +
                                "\nPrice: $" + product.getPrice() +
                                "\nDescription: " + product.getDescription() +
                                "\nAvailable Quantity: " + product.getStock());
                    } else {
                        productDetailsArea.setText("No product found.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addToCart(String productName, int quantity) {
        try (Connection conn = MySQLConnection.getConnection()) {
            String sql = "SELECT * FROM Product WHERE name = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, productName);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        double price = rs.getDouble("price");
                        Product product = new Product(
                                rs.getString("productId"),
                                rs.getString("name"),
                                price,
                                quantity,
                                rs.getString("categoryId"),
                                rs.getString("description")
                        );
                        cart.add(product);
                        totalPrice += price * quantity;  // Update total price

                        // Update stock after adding to cart
                        updateStock(product.getProductId(), quantity);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateStock(String productId, int quantitySold) {
        // Update the stock in the database after purchase
        try (Connection conn = MySQLConnection.getConnection()) {
            String sql = "UPDATE Product SET stock = stock - ? WHERE productId = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, quantitySold);
                stmt.setString(2, productId);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
