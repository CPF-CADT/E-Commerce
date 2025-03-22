package GUI;

import Database.MySQLConnection;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import utils.Product;

public class PaymentGUI extends JFrame {
    private JComboBox<String> productComboBox;
    protected JLabel totalPriceLabel;
    private JButton cashPaymentButton, qrPaymentButton;
    protected double totalPrice = 0.0;
    private java.util.List<Product> cart = new java.util.ArrayList<>();
    
    private static final String QR_IMAGE_PATH = "/Resource/payment_qr.png"; // Ensure correct path

    // Constructor
    public PaymentGUI(java.util.List<Product> cart, double totalPrice) {
        this.cart = cart;
        this.totalPrice = totalPrice;
        
        setTitle("Payment System");
        setSize(400, 350);
        setLayout(new GridLayout(4, 1));
        getContentPane().setBackground(Color.BLACK);

        productComboBox = new JComboBox<>();
        loadProducts(); // Fetch products from DB

        totalPriceLabel = new JLabel("Total Price: $" + totalPrice, SwingConstants.CENTER);
        totalPriceLabel.setForeground(Color.WHITE);

        cashPaymentButton = new JButton("Pay by Cash");
        qrPaymentButton = new JButton("Pay by QR Code");

        add(productComboBox);
        add(totalPriceLabel);
        add(cashPaymentButton);
        add(qrPaymentButton);

        productComboBox.addActionListener(e -> updateTotalPrice());

        // Cash payment action
        cashPaymentButton.addActionListener(e -> processPayment("Cash"));

        // QR payment action
        qrPaymentButton.addActionListener(e -> generateQRCode());

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    // Load products from database into ComboBox
    private void loadProducts() {
        String sql = "SELECT name FROM Product";
        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                productComboBox.addItem(rs.getString("name"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading products: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Update total price when product is selected
    private void updateTotalPrice() {
        String selectedProduct = (String) productComboBox.getSelectedItem();
        if (selectedProduct != null) {
            try (Connection conn = MySQLConnection.getConnection()) {
                String sql = "SELECT price FROM Product WHERE name = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, selectedProduct);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            totalPrice = rs.getDouble("price");
                            totalPriceLabel.setText("Total Price: $" + String.format("%.2f", totalPrice));
                        } else {
                            JOptionPane.showMessageDialog(this, "Product not found in database!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error fetching product price: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Process payment based on method (Cash or QR)
    private void processPayment(String method) {
        JOptionPane.showMessageDialog(this, "Payment of $" + String.format("%.2f", totalPrice) + " received via " + method + ".");
        showPaymentComplete();
    }

    // Generate QR code for payment
    private void generateQRCode() {
        ImageIcon qrIcon = loadImageIcon(QR_IMAGE_PATH);
        if (qrIcon == null) {
            JOptionPane.showMessageDialog(this, "QR code image not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JLabel qrLabel = new JLabel(qrIcon);
        int result = JOptionPane.showConfirmDialog(this, qrLabel, "Scan QR Code to Pay", JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            processPayment("QR Code");
        }
    }

    // Display payment success message
    private void showPaymentComplete() {
        getContentPane().removeAll();
        JLabel successLabel = new JLabel("Payment Successful!", SwingConstants.CENTER);
        successLabel.setForeground(Color.GREEN);
        successLabel.setFont(new Font("Arial", Font.BOLD, 16));

        add(successLabel);

        revalidate();
        repaint();
    }

    // Load image icon for QR code
    private ImageIcon loadImageIcon(String path) {
        java.net.URL imgURL = getClass().getResource(path);
        return (imgURL != null) ? new ImageIcon(imgURL) : null;
    }

    // Main method to test PaymentGUI
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Dummy data to test PaymentGUI
            java.util.List<Product> cart = new java.util.ArrayList<>();
            cart.add(new Product("1", "Product 1", 20.0, 2, "1", "Description of Product 1"));
            cart.add(new Product("2", "Product 2", 30.0, 1, "1", "Description of Product 2"));
            
            double totalPrice = 0.0;
            for (Product product : cart) {
                totalPrice += product.getPrice();
            }
            
            new PaymentGUI(cart, totalPrice);
        });
    }
}
