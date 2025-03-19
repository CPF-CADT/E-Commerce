package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        ImageIcon qrIcon = loadImageIcon(QR_IMAGE_PATH);
        JLabel qrLabel = new JLabel(qrIcon);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(qrLabel, BorderLayout.CENTER);
        
        // Add a checkmark after payment is confirmed
        int result = JOptionPane.showConfirmDialog(this, "Scan this QR to complete the payment.", "Confirm Payment", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            ImageIcon checkIcon = loadImageIcon(CHECK_IMAGE_PATH);
            JLabel checkLabel = new JLabel(checkIcon);
            panel.add(checkLabel, BorderLayout.SOUTH);
        }
        
        getContentPane().removeAll();
        getContentPane().add(panel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private ImageIcon loadImageIcon(String path) {
        return new ImageIcon(path);
    }
}
