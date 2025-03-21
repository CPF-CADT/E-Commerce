package Testing;

import Database.MySQLConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CusGUI {
    private JFrame frame;
    private DefaultListModel<String> productListModel;
    private DefaultListModel<String> cartListModel;
    private JList<String> productList;
    private JList<String> cartList;
    private String userId = "C0001"; // Example User ID, replace dynamically
    private List<String> cartItems = new ArrayList<>();

    public CusGUI() {
        frame = new JFrame("Customer Shop");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(1, 2));

        JPanel productPanel = new JPanel(new BorderLayout());
        productListModel = new DefaultListModel<>();
        productList = new JList<>(productListModel);
        productPanel.add(new JLabel("Available Products"), BorderLayout.NORTH);
        productPanel.add(new JScrollPane(productList), BorderLayout.CENTER);
        JButton addToCartButton = new JButton("Add to Cart");
        productPanel.add(addToCartButton, BorderLayout.SOUTH);

        JPanel cartPanel = new JPanel(new BorderLayout());
        cartListModel = new DefaultListModel<>();
        cartList = new JList<>(cartListModel);
        cartPanel.add(new JLabel("Your Cart"), BorderLayout.NORTH);
        cartPanel.add(new JScrollPane(cartList), BorderLayout.CENTER);
        JButton checkoutButton = new JButton("Check Out");
        cartPanel.add(checkoutButton, BorderLayout.SOUTH);

        frame.add(productPanel);
        frame.add(cartPanel);

        loadProducts();

        addToCartButton.addActionListener(e -> addToCart());
        checkoutButton.addActionListener(e -> checkout());

        frame.setVisible(true);
    }

    private void loadProducts() {
        try (Connection conn = MySQLConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name, price, stock, productId FROM Product WHERE stock > 0")) {
            while (rs.next()) {
                String product = rs.getString("name") + " - $" + rs.getDouble("price") + " (Stock: " + rs.getInt("stock") + ")";
                productListModel.addElement(product);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addToCart() {
        String selectedProduct = productList.getSelectedValue();
        if (selectedProduct != null) {
            cartListModel.addElement(selectedProduct);
            cartItems.add(selectedProduct.split(" - ")[0]); // Store product name
        }
    }

    private void checkout() {
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Your cart is empty!");
            return;
        }

        String paymentMethod = JOptionPane.showInputDialog(frame, "Enter Payment Method (Credit Card / PayPal):");
        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Payment method is required!");
            return;
        }

        try (Connection conn = MySQLConnection.getConnection()) {
            conn.setAutoCommit(false);
            String orderId = "O" + System.currentTimeMillis(); // Unique Order ID

            PreparedStatement orderStmt = conn.prepareStatement("INSERT INTO `Order` (orderId, userId, orderDate, paymentMethod, status) VALUES (?, ?, CURRENT_DATE, ?, 'PENDING')");
            orderStmt.setString(1, orderId);
            orderStmt.setString(2, userId);
            orderStmt.setString(3, paymentMethod);
            orderStmt.executeUpdate();
            orderStmt.close();

            for (String productName : cartItems) {
                PreparedStatement stmt = conn.prepareStatement("SELECT productId, price, stock FROM Product WHERE name = ?");
                stmt.setString(1, productName);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String productId = rs.getString("productId");
                    double price = rs.getDouble("price");
                    int stock = rs.getInt("stock");
                    if (stock > 0) {
                        PreparedStatement orderItemStmt = conn.prepareStatement("INSERT INTO Order_Items (orderId, productId, quantity, price) VALUES (?, ?, 1, ?)");
                        orderItemStmt.setString(1, orderId);
                        orderItemStmt.setString(2, productId);
                        orderItemStmt.setDouble(3, price);
                        orderItemStmt.executeUpdate();
                        orderItemStmt.close();

                        PreparedStatement updateStockStmt = conn.prepareStatement("UPDATE Product SET stock = stock - 1 WHERE productId = ?");
                        updateStockStmt.setString(1, productId);
                        updateStockStmt.executeUpdate();
                        updateStockStmt.close();
                    }
                }
                rs.close();
                stmt.close();
            }

            PreparedStatement paymentStmt = conn.prepareStatement("INSERT INTO Payment (orderId, paymentMethod, amount, paymentStatus) VALUES (?, ?, ?, 'COMPLETED')");
            paymentStmt.setString(1, orderId);
            paymentStmt.setString(2, paymentMethod);
            paymentStmt.setDouble(3, calculateTotalAmount(conn));
            paymentStmt.executeUpdate();
            paymentStmt.close();

            conn.commit();
            JOptionPane.showMessageDialog(frame, "Checkout successful! Order ID: " + orderId);
            cartListModel.clear();
            cartItems.clear();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Transaction failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private double calculateTotalAmount(Connection conn) throws SQLException {
        double totalAmount = 0;
        for (String productName : cartItems) {
            PreparedStatement stmt = conn.prepareStatement("SELECT price FROM Product WHERE name = ?");
            stmt.setString(1, productName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                totalAmount += rs.getDouble("price");
            }
            rs.close();
            stmt.close();
        }
        return totalAmount;
    }

    public static void main(String[] args) {
        new CusGUI();
    }
}
