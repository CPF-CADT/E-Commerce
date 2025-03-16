package Resources;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class PaymentGUI extends JFrame {
    private JButton cashPaymentButton, qrPaymentButton;

    public PaymentGUI() {
        setTitle("Payment Methods");
        setSize(400, 300);
        setLayout(new GridLayout(2, 1));
        getContentPane().setBackground(Color.BLACK);

        cashPaymentButton = new JButton("Pay by Cash");
        qrPaymentButton = new JButton("Pay by QR Code");
        
        add(cashPaymentButton);
        add(qrPaymentButton);

        cashPaymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Please pay the cashier directly.");
            }
        });

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
        // Simulating QR Code Generation Process
        ImageIcon qrIcon = new ImageIcon("path_to_qr_code_image.png"); // Replace with real QR generation
        JOptionPane.showMessageDialog(null, "Scan this QR code to complete your payment.", "QR Payment", JOptionPane.INFORMATION_MESSAGE, qrIcon);
        
        // Simulating QR scan success
        int result = JOptionPane.showConfirmDialog(null, "Did you complete the payment?", "Confirm Payment", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            ImageIcon checkIcon = new ImageIcon("path_to_checkmark_image.png"); // Replace with real checkmark image
            JOptionPane.showMessageDialog(null, "Payment Successful!", "Success", JOptionPane.INFORMATION_MESSAGE, checkIcon);
        } else {
            JOptionPane.showMessageDialog(null, "Payment not completed. Try again.");
        }
    }
    
    public static void main(String[] args) {
        new PaymentGUI();
    }
}
