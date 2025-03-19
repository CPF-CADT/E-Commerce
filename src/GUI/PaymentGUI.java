package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class PaymentGUI extends JFrame {
    private JButton cashPaymentButton, qrPaymentButton;
    private static final String QR_IMAGE_PATH = "payment_qr.png";
    private static final String CHECK_IMAGE_PATH = "checkmark.png";

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
        cashPaymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Please pay the cashier directly.");
            }
        });

        // QR payment action
        qrPaymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateQRCode();
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void generateQRCode() {
        ImageIcon qrIcon = loadImage(QR_IMAGE_PATH );
        if (qrIcon != null) {
            JOptionPane.showMessageDialog(null, "Scan this QR code to complete your payment.", "QR Payment", JOptionPane.INFORMATION_MESSAGE, qrIcon);

            int result = JOptionPane.showConfirmDialog(null, "Did you complete the payment?", "Confirm Payment", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                ImageIcon checkIcon = loadImage(CHECK_IMAGE_PATH);
                if (checkIcon != null) {
                    
                    JOptionPane.showMessageDialog(null, "Payment Successful!", "Success", JOptionPane.INFORMATION_MESSAGE, checkIcon);
                } else {
                    JOptionPane.showMessageDialog(null, "Payment Successful, but checkmark image not found.", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Payment not completed. Try again.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "QR code image not found. Please try again later.");
        }
    }

    private ImageIcon loadImage(String path) {
        ImageIcon icon = new ImageIcon(path);
        if (icon.getIconWidth() == -1) {
            return null; // Image not found
        }
        return icon;
    }


    public static void main(String[] args) {
        new PaymentGUI();
    }
}
