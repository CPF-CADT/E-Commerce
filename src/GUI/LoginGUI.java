package GUI;

import Database.MySQLConnection;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import utils.Encryption;

public class LoginGUI extends JFrame {
    private static String userId;
    
    public LoginGUI() {
        setTitle("Login");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(0, 255, 255));
        panel.setLayout(null);

        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setBounds(270, 20, 200, 60);
        titleLabel.setForeground(Color.BLACK);
        panel.add(titleLabel);

        JTextField emailField = new JTextField("Email");
        emailField.setBounds(250, 100, 250, 45);
        emailField.setBackground(Color.WHITE);
        emailField.setForeground(Color.DARK_GRAY);
        emailField.setFont(new Font("Arial", Font.PLAIN, 18));
        emailField.setBorder(BorderFactory.createLineBorder(Color.CYAN, 2));
        panel.add(emailField);

        JPasswordField passwordField = new JPasswordField("Password");
        passwordField.setBounds(250, 160, 250, 45);
        passwordField.setBackground(Color.WHITE);
        passwordField.setForeground(Color.DARK_GRAY);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 18));
        passwordField.setBorder(BorderFactory.createLineBorder(Color.CYAN, 2));
        panel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(250, 220, 250, 45);
        loginButton.setBackground(new Color(0, 255, 255));
        loginButton.setForeground(Color.BLACK);
        loginButton.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(loginButton);

        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            boolean loginSuccess = validateLogin(email, password);
            if (loginSuccess) {
                JOptionPane.showMessageDialog(this, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();  // Close login window
                
                // Pass userId to CategoryProductGUI
                String[] args = new String[]{userId};
                CategoryProductGUI.main(args);  // Open CategoryProductGUI
            } else {
                JOptionPane.showMessageDialog(this, "Invalid email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton createAccountButton = new JButton("Don't have an account? Create one");
        createAccountButton.setBounds(250, 290, 250, 45);
        createAccountButton.setBackground(new Color(0, 255, 255));
        createAccountButton.setForeground(Color.BLACK);
        createAccountButton.setFont(new Font("Arial", Font.BOLD, 16));
        createAccountButton.setBorder(BorderFactory.createLineBorder(Color.CYAN, 2));
        createAccountButton.addActionListener(e -> {
            dispose();  // Close login window
            String[] args = new String[]{userId};
            CreateAccountGUI.main(args);  // Open the create account screen
        });
        panel.add(createAccountButton);

        add(panel);
        setVisible(true);
    }

    // Validate login credentials
    private boolean validateLogin(String email, String password) {
        String query = "SELECT userId, password FROM User WHERE email = ?";
        Connection conn = MySQLConnection.getConnection();

        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Database connection failed. Please try again later.", "Connection Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                String storedHashedPassword = result.getString("password");
                userId = result.getString("userId");

                if (Encryption.verifyPassword(storedHashedPassword, password)) {
                    System.out.println("Login successful!");
                    return true;
                } else {
                    System.out.println("Incorrect password!");
                }
            } else {
                System.out.println("No user found with email: " + email);
            }

        } catch (SQLException e) {
            System.out.println("Database query error: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Database query failed. Please try again later.", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("Failed to close connection: " + e.getMessage());
            }
        }
        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginGUI::new);  // Run the login GUI on the Event Dispatch Thread
    }
}
