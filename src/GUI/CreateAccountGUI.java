package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import Database.MySQLConnection;

public class CreateAccountGUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Create Account");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(500, 700);
                frame.setLayout(new BorderLayout());

                // Set background color to pink
                frame.getContentPane().setBackground(new Color(255, 192, 203)); // Light pink

                // Title Label
                JLabel titleLabel = new JLabel("Create Account", SwingConstants.CENTER);
                titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
                titleLabel.setForeground(new Color(50, 50, 50)); 
                frame.add(titleLabel, BorderLayout.NORTH);

                // Panel for the form
                JPanel formPanel = new JPanel(new GridBagLayout());
                formPanel.setBackground(new Color(255, 192, 203)); // Light pink
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5, 5, 5, 5); // Reduce spacing

                // Fields
                JTextField firstNameField = createTextField();
                JTextField lastNameField = createTextField();
                JTextField emailField = createTextField();
                JPasswordField passwordField = createPasswordField();
                JTextField streetField = createTextField();
                JTextField cityField = createTextField();
                JTextField stateField = createTextField();
                JTextField postalCodeField = createTextField();
                JTextField countryField = createTextField();
                JTextField phoneNumberField = createTextField();
                JTextField dateOfBirthField = createTextField(); 

                JButton submitButton = new JButton("Create Account");
                styleButton(submitButton);

                // Adding Fields to Form Panel
                addLabelAndField("First Name:", firstNameField, formPanel, gbc, 0);
                addLabelAndField("Last Name:", lastNameField, formPanel, gbc, 1);
                addLabelAndField("Email:", emailField, formPanel, gbc, 2);
                addLabelAndField("Password:", passwordField, formPanel, gbc, 3);
                addLabelAndField("Street:", streetField, formPanel, gbc, 4);
                addLabelAndField("City:", cityField, formPanel, gbc, 5);
                addLabelAndField("State:", stateField, formPanel, gbc, 6);
                addLabelAndField("Postal Code:", postalCodeField, formPanel, gbc, 7);
                addLabelAndField("Country:", countryField, formPanel, gbc, 8);
                addLabelAndField("Phone Number:", phoneNumberField, formPanel, gbc, 9);
                addLabelAndField("Date of Birth (YYYY-MM-DD):", dateOfBirthField, formPanel, gbc, 10);

                // Submit Button
                gbc.gridx = 0;
                gbc.gridy = 11;
                gbc.gridwidth = 2;
                gbc.anchor = GridBagConstraints.CENTER;
                formPanel.add(submitButton, gbc);

                frame.add(formPanel, BorderLayout.CENTER);

                // Login Button
                JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                loginPanel.setBackground(new Color(255, 192, 203)); 
                JButton loginButton = new JButton("Already have an account? Login");
                styleLoginButton(loginButton, frame);
                loginPanel.add(loginButton);
                frame.add(loginPanel, BorderLayout.SOUTH);

                // Submit Button Action
                submitButton.addActionListener(e -> {
                    String firstName = firstNameField.getText().trim();
                    String lastName = lastNameField.getText().trim();
                    String email = emailField.getText().trim();
                    String password = new String(passwordField.getPassword()).trim();
                    String street = streetField.getText().trim();
                    String city = cityField.getText().trim();
                    String state = stateField.getText().trim();
                    String postalCode = postalCodeField.getText().trim();
                    String country = countryField.getText().trim();
                    String phoneNumber = phoneNumberField.getText().trim();
                    String dateOfBirth = dateOfBirthField.getText().trim();

                    if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() ||
                            street.isEmpty() || city.isEmpty() || postalCode.isEmpty() || country.isEmpty() || dateOfBirth.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Please fill in all required fields!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (!dateOfBirth.matches("\\d{4}-\\d{2}-\\d{2}")) {
                        JOptionPane.showMessageDialog(frame, "Invalid Date Format! Use YYYY-MM-DD", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    try {
                        Connection conn = MySQLConnection.getConnection();
                        String userId = generateUserId(conn);
                        String encryptedPassword = hashPassword(password);

                        String sqlUser = "INSERT INTO User (userId, firstname, lastname, email, password, street, city, state, postalCode, country, phoneNumber, dateOfBirth) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        PreparedStatement stmtUser = conn.prepareStatement(sqlUser);
                        stmtUser.setString(1, userId);
                        stmtUser.setString(2, firstName);
                        stmtUser.setString(3, lastName);
                        stmtUser.setString(4, email);
                        stmtUser.setString(5, encryptedPassword);
                        stmtUser.setString(6, street);
                        stmtUser.setString(7, city);
                        stmtUser.setString(8, state);
                        stmtUser.setString(9, postalCode);
                        stmtUser.setString(10, country);
                        stmtUser.setString(11, phoneNumber);
                        stmtUser.setString(12, dateOfBirth);
                        stmtUser.executeUpdate();

                        JOptionPane.showMessageDialog(frame, "Account Created Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        conn.close();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                    }
                });

                frame.setVisible(true);
            }
        });
    }

    private static void addLabelAndField(String label, JTextField field, JPanel panel, GridBagConstraints gbc, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(field, gbc);
    }

    private static JTextField createTextField() {
        JTextField textField = new JTextField(15);
        return textField;
    }

    private static JPasswordField createPasswordField() {
        return new JPasswordField(15);
    }

    private static void styleButton(JButton button) {
        button.setBackground(new Color(0, 122, 204));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    private static void styleLoginButton(JButton button, JFrame frame) {
        button.setForeground(new Color(0, 122, 204));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Redirecting to login page...", "Login", JOptionPane.INFORMATION_MESSAGE));
    }

    private static String generateUserId(Connection conn) throws SQLException {
        String sql = "SELECT userId FROM User ORDER BY userId DESC LIMIT 1";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if (rs.next()) {
            String lastUserId = rs.getString("userId");
            // Check if the userId is in the expected format
            if (lastUserId.length() >= 9) {  // "C000_0001" length is 9
                int lastNumber = Integer.parseInt(lastUserId.substring(5));  // Get the number part after "C000_"
                return "C000_" + String.format("%04d", lastNumber + 1);
            }
        }
        // Return the first userId when no previous ones are found
        return "C000_0002";
    }

    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) hexString.append(String.format("%02x", b));
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            return password;
        }
    }
}
