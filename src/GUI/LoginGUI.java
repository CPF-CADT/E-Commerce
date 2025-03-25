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

        JPanel emailPanel = new JPanel(new BorderLayout(10, 10));
        emailPanel.setBounds(250, 100, 250, 45);
        emailPanel.setBackground(Color.WHITE);
        emailPanel.setForeground(Color.DARK_GRAY);
        emailPanel.setFont(new Font("Arial", Font.PLAIN, 18));
        emailPanel.setBorder(BorderFactory.createLineBorder(Color.CYAN, 2));
        JLabel emailLabel = new JLabel("Email");
        JTextField emailField = new JTextField();
        emailPanel.add(emailLabel, BorderLayout.WEST);
        emailPanel.add(emailField, BorderLayout.CENTER);
        panel.add(emailPanel);

        JPanel passwordPanel = new JPanel(new BorderLayout(10, 10));
        passwordPanel.setBounds(250, 160, 250, 45);
        passwordPanel.setBackground(Color.WHITE);
        passwordPanel.setForeground(Color.DARK_GRAY);
        passwordPanel.setFont(new Font("Arial", Font.PLAIN, 18));
        passwordPanel.setBorder(BorderFactory.createLineBorder(Color.CYAN, 2));
        JLabel passwordLabel = new JLabel("Password");
        JPasswordField passwordField = new JPasswordField();
        passwordPanel.add(passwordLabel, BorderLayout.WEST);
        passwordPanel.add(passwordField, BorderLayout.CENTER);
        panel.add(passwordPanel);

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
                this.dispose();  // Close the login window immediately without success message
            } else {
                JOptionPane.showMessageDialog(this, "Invalid email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton createAccountButton = new JButton("Don't have an account? Create one");
        createAccountButton.setBounds(250, 290, 300, 45);
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
                String userId = result.getString("userId");

                if (Encryption.verifyPassword(storedHashedPassword, password)) {
                    System.out.println("Login successful!");
                    String[] args = new String[]{userId};
                    if (userId.startsWith("S")) {
                        System.out.println("User is staff.");
                        AdminGUI.main(args);  // Open Admin GUI
                    } else if (userId.startsWith("C")) {
                        System.out.println("User is customer.");
                        CategoryProductGUI.main(args);  // Open CategoryProduct GUI
                    } else {
                        System.out.println("Unknown user type.");
                    }

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
        SwingUtilities.invokeLater(LoginGUI::new);
    }
}
