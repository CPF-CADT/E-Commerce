package Testing;

import Database.MySQLConnection;
import GUI.CreateAccountGUI;
import GUI.LoginGUI;

import org.mindrot.jbcrypt.BCrypt; // For password hashing
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginGUI extends JFrame {

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
                JOptionPane.showMessageDialog(this, "🎉 Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "❌ Invalid email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton createAccountButton = new JButton("Don't have an account? Create one");
        createAccountButton.setBounds(250, 290, 250, 45);
        createAccountButton.setBackground(new Color(0, 255, 255));
        createAccountButton.setForeground(Color.BLACK);
        createAccountButton.setFont(new Font("Arial", Font.BOLD, 16));
        createAccountButton.setBorder(BorderFactory.createLineBorder(Color.CYAN, 2));
        createAccountButton.addActionListener(e -> {
            dispose();
            new CreateAccountGUI();
        });
        panel.add(createAccountButton);

        add(panel);
        setVisible(true);
    }

    // Validate login credentials
    private boolean validateLogin(String email, String password) {
        String query = "SELECT password FROM User WHERE email = ?";
        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setString(1, email);
            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                String storedHashedPassword = result.getString("password");
                System.out.println("🔍 Checking password for user: " + email);

                // Compare hashed password using BCrypt
                if (BCrypt.checkpw(password, storedHashedPassword)) {
                    System.out.println("✅ Login successful!");
                    return true;
                } else {
                    System.out.println("❌ Incorrect password!");
                }
            } else {
                System.out.println("❌ No user found with email: " + email);
            }
        } catch (SQLException e) {
            System.out.println("❌ Database error: " + e.getMessage());
        }
        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginGUI::new);
    }
}
