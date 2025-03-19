package GUI;

import java.awt.*;
import javax.swing.*;

public class PaymentGUI extends JFrame {
    private JButton cashPaymentButton, qrPaymentButton;
    
    // Correct paths for images inside `resources` folder
    private static final String QR_IMAGE_PATH = "Resource/payment_qr.png";  
    private static final String CHECK_IMAGE_PATH = "/checkmark.png";  

    public PaymentGUI() {
        setTitle("Payment Methods");
        setSize(400, 300);
        setLayout(new GridLayout(2, 1));
        getContentPane().setBackground(Color.BLACK);

        cashPaymentButton = new JButton("Pay by Cash");
        qrPaymentButton = new JButton("Pay by QR Code");

        add(cashPaymentButton);
        add(qrPaymentButton);

        // Cash payment action
        cashPaymentButton.addActionListener(e -> 
            JOptionPane.showMessageDialog(null, "Please pay the cashier directly.")
        );

        // QR payment action
        qrPaymentButton.addActionListener(e -> generateQRCode());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // Method to display QR code for payment
    private void generateQRCode() {
        ImageIcon qrIcon = loadImageIcon(QR_IMAGE_PATH);

        if (qrIcon == null) {
            JOptionPane.showMessageDialog(this, "QR code image not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JLabel qrLabel = new JLabel(qrIcon);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(qrLabel, BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(this, "Scan this QR to complete the payment.", 
            "Confirm Payment", JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            ImageIcon checkIcon = loadImageIcon(CHECK_IMAGE_PATH);
            if (checkIcon != null) {
                JLabel checkLabel = new JLabel(checkIcon);
                panel.add(checkLabel, BorderLayout.SOUTH);
            }
        }

        getContentPane().removeAll();
        getContentPane().add(panel);
        revalidate();
        repaint();
    }

    // Improved image loading method
    private ImageIcon loadImageIcon(String path) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PaymentGUI::new);
    }
}
