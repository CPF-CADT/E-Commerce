package GUI;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import utils.Product;

public class PaymentGUI extends JFrame {
    private JTextArea cartTextArea;
    private JLabel totalPriceLabel;
    private JButton cashPaymentButton, qrPaymentButton;
    private double totalPrice = 0.0;
    private List<Product> cart;

    public PaymentGUI(List<Product> cart, double totalPrice) {
        this.cart = cart;
        this.totalPrice = totalPrice;

        setTitle("Payment System");
        setSize(400, 350);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.BLACK);

        cartTextArea = new JTextArea(10, 30);
        cartTextArea.setEditable(false);
        cartTextArea.setBackground(Color.BLACK);
        cartTextArea.setForeground(Color.WHITE);
        updateCartDisplay();  // Initial cart display

        totalPriceLabel = new JLabel("Total Price: $" + totalPrice, SwingConstants.CENTER);
        totalPriceLabel.setForeground(Color.WHITE);

        cashPaymentButton = new JButton("Pay by Cash");
        qrPaymentButton = new JButton("Pay by QR Code");

        add(new JScrollPane(cartTextArea), BorderLayout.CENTER);
        add(totalPriceLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1));
        buttonPanel.add(cashPaymentButton);
        buttonPanel.add(qrPaymentButton);

        add(buttonPanel, BorderLayout.SOUTH);

        cashPaymentButton.addActionListener(e -> processPayment("Cash"));
        qrPaymentButton.addActionListener(e -> generateQRCode());

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void updateCartDisplay() {
        cartTextArea.setText("");  // Clear previous content
        for (Product product : cart) {
            cartTextArea.append(product.getName() + " (Qty: " + product.getStock() + ")\n");
        }
    }

    private void processPayment(String method) {
        JOptionPane.showMessageDialog(this, "Payment of $" + String.format("%.2f", totalPrice) + " received via " + method + ".");
        showPaymentComplete();
    }

    private void generateQRCode() {
        ImageIcon qrIcon = loadImageIcon("GUI/Resources/payment_qr.png");
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
        java.net.URL imgURL = getClass().getClassLoader().getResource(path);
        if (imgURL == null) {
            System.out.println("ERROR: QR code image not found at: " + path);
            return null;
        }
        return new ImageIcon(imgURL);
    }
}
