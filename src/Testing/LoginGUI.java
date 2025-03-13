package Testing;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.*;

public class LoginGUI extends JFrame {
    public LoginGUI() {
        // Frame setup
        setTitle("Login");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main panel with cyan background
        JPanel panel = new JPanel();
        panel.setBackground(new Color(0, 255, 255)); // Cyan background
        panel.setLayout(null); // Manual positioning

        // Title Label
        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setBounds(270, 20, 200, 60); // Adjusted position for new frame size
        titleLabel.setForeground(Color.BLACK); // Black text color for title
        panel.add(titleLabel);

        // Email Field with Placeholder (Cyan border and white background)
        JTextField emailField = new JTextField("Email");
        emailField.setBounds(250, 100, 250, 45); // Increased size
        emailField.setBackground(Color.WHITE);
        emailField.setForeground(Color.DARK_GRAY);
        emailField.setFont(new Font("Arial", Font.PLAIN, 18)); // Font size increased
        emailField.setBorder(BorderFactory.createLineBorder(Color.CYAN, 2)); // Cyan border
        panel.add(emailField);

        // Add focus listener to handle placeholder behavior
        emailField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (emailField.getText().equals("Email")) {
                    emailField.setText("");
                    emailField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (emailField.getText().isEmpty()) {
                    emailField.setText("Email");
                    emailField.setForeground(Color.DARK_GRAY);
                }
            }
        });

        // Password Field with Placeholder (Using JPasswordField instead of JTextField for password input)
        JPasswordField passwordField = new JPasswordField("Password"); // Use JPasswordField for password input
        passwordField.setBounds(250, 160, 250, 45); // Increased size
        passwordField.setBackground(Color.WHITE);
        passwordField.setForeground(Color.DARK_GRAY);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 18)); // Font size increased
        passwordField.setBorder(BorderFactory.createLineBorder(Color.CYAN, 2)); // Translucent red border
        panel.add(passwordField);

        // Add focus listener to handle placeholder behavior
        passwordField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (new String(passwordField.getPassword()).equals("Password")) {
                    passwordField.setText("");
                    passwordField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (new String(passwordField.getPassword()).isEmpty()) {
                    passwordField.setText("Password");
                    passwordField.setForeground(Color.DARK_GRAY);
                }
            }
        });

        // Login Button (Cyan background with white text)
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(250, 220, 250, 45); // Adjusted size to match new input box size
        loginButton.setBackground(new Color(0, 255, 255)); // Cyan color
        loginButton.setForeground(Color.BLACK); // Black text for the button
        loginButton.setFont(new Font("Arial", Font.BOLD, 18)); // Font size increased
        panel.add(loginButton);

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginGUI());
    }
}
