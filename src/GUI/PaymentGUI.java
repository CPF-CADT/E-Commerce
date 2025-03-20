package GUI;

import Database.MySQLConnection;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;

public class PaymentGUI extends JFrame {
    private JComboBox<String> productComboBox;
    private JLabel totalPriceLabel;
    private JButton cashPaymentButton, qrPaymentButton;
    private double totalPrice = 0.0;
    
    private static final String QR_IMAGE_PATH = "/Resource/payment_qr.png";  

    public PaymentGUI() {
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

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

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

    private void updateTotalPrice() {
        String selectedProduct = (String) productComboBox.getSelectedItem();
        if (selectedProduct != null) {
            Product product = Product.getProductsByCategory(selectedProduct).get(0);
            totalPrice = product.getPrice();
            totalPriceLabel.setText("Total Price: $" + totalPrice);
        }
    }

    private void processPayment(String method) {
        JOptionPane.showMessageDialog(this, "Payment of $" + totalPrice + " received via " + method + ".");
        showPaymentComplete();
    }

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

    private void showPaymentComplete() {
        getContentPane().removeAll();
        JLabel successLabel = new JLabel("Payment Successful!", SwingConstants.CENTER);
        successLabel.setForeground(Color.GREEN);
        successLabel.setFont(new Font("Arial", Font.BOLD, 16));

        add(successLabel);

        revalidate();
        repaint();
    }

    private ImageIcon loadImageIcon(String path) {
        java.net.URL imgURL = getClass().getResource(path);
        return (imgURL != null) ? new ImageIcon(imgURL) : null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PaymentGUI::new);
    }
}
