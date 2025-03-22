package GUI;

import Database.MySQLConnection;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;
import javax.swing.*;
import utils.Encryption;

public class CreateAccountGUI {
    private static final HashMap<String, String> userTypes = new HashMap<>();
    private static String userId;
    static {
        userTypes.put("Customer", "C");
        userTypes.put("Staff", "S");
    }

    public static void main(String[] args) {
        userId = args[0];
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Create Account");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 750);
            frame.setLayout(new BorderLayout());
            frame.getContentPane().setBackground(new Color(255, 192, 203));

            JLabel titleLabel = new JLabel("Create Account", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
            frame.add(titleLabel, BorderLayout.NORTH);

            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBackground(new Color(255, 192, 203));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);

            JComboBox<String> userTypeComboBox = new JComboBox<>(userTypes.keySet().toArray(new String[0]));
            JTextField firstNameField = new JTextField(15);
            JTextField lastNameField = new JTextField(15);
            JTextField emailField = new JTextField(15);
            JPasswordField passwordField = new JPasswordField(15);
            JTextField addressField = new JTextField(15);
            JTextField cityField = new JTextField(15);
            JTextField stateField = new JTextField(15);
            JTextField postalCodeField = new JTextField(15);
            JTextField countryField = new JTextField(15);
            JTextField phoneField = new JTextField(15);
            JTextField dateOfBirthField = new JTextField(15);
            JTextField positionField = new JTextField(15);
            
            addLabelAndField("User Type:", userTypeComboBox, formPanel, gbc, 0);
            addLabelAndField("First Name:", firstNameField, formPanel, gbc, 1);
            addLabelAndField("Last Name:", lastNameField, formPanel, gbc, 2);
            addLabelAndField("Email:", emailField, formPanel, gbc, 3);
            addLabelAndField("Password:", passwordField, formPanel, gbc, 4);
            addLabelAndField("Address:", addressField, formPanel, gbc, 5);
            addLabelAndField("City:", cityField, formPanel, gbc, 6);
            addLabelAndField("State:", stateField, formPanel, gbc, 7);
            addLabelAndField("Postal Code:", postalCodeField, formPanel, gbc, 8);
            addLabelAndField("Country:", countryField, formPanel, gbc, 9);
            addLabelAndField("Phone Number:", phoneField, formPanel, gbc, 10);
            addLabelAndField("Date of Birth:", dateOfBirthField, formPanel, gbc, 11);
            addLabelAndField("Position (if Staff):", positionField, formPanel, gbc, 12);
            formPanel.getComponent(formPanel.getComponentCount() - 2).setVisible(false);
            positionField.setVisible(false);
            
            userTypeComboBox.addActionListener(e -> {
                positionField.setVisible("Staff".equals(userTypeComboBox.getSelectedItem()));
                formPanel.getComponent(formPanel.getComponentCount() - 3).setVisible("Staff".equals(userTypeComboBox.getSelectedItem()));
                frame.revalidate();
            });
            
            JButton submitButton = new JButton("Create Account");
            submitButton.addActionListener(e -> createAccount(frame, userTypeComboBox, firstNameField, lastNameField, emailField, passwordField, addressField, cityField, stateField, postalCodeField, countryField, phoneField, dateOfBirthField, positionField));
            
            gbc.gridy = 13;
            formPanel.add(submitButton, gbc);
            frame.add(formPanel, BorderLayout.CENTER);
            frame.setVisible(true);
        });
    }

    private static void addLabelAndField(String label, JComponent field, JPanel panel, GridBagConstraints gbc, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private static void createAccount(JFrame frame, JComboBox<String> userTypeComboBox, JTextField firstNameField, JTextField lastNameField, JTextField emailField, JPasswordField passwordField, JTextField addressField, JTextField cityField, JTextField stateField, JTextField postalCodeField, JTextField countryField, JTextField phoneField, JTextField dateOfBirthField, JTextField positionField) {
        String userType = (String) userTypeComboBox.getSelectedItem();
        String prefix = userTypes.get(userType);

        String userId = prefix + "000_" + System.currentTimeMillis() % 10000;
        String password = Encryption.hashPassword(new String(passwordField.getPassword()).trim());

        try (Connection conn = MySQLConnection.getConnection()) {
            String sqlUser = "INSERT INTO User (userId, firstname, lastname, email, password, street, city, state, postalCode, country, phoneNumber, dateOfBirth) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmtUser = conn.prepareStatement(sqlUser);
            stmtUser.setString(1, userId);
            stmtUser.setString(2, firstNameField.getText().trim());
            stmtUser.setString(3, lastNameField.getText().trim());
            stmtUser.setString(4, emailField.getText().trim());
            stmtUser.setString(5, password);
            stmtUser.setString(6, addressField.getText().trim());
            stmtUser.setString(7, cityField.getText().trim());
            stmtUser.setString(8, stateField.getText().trim());
            stmtUser.setString(9, postalCodeField.getText().trim());
            stmtUser.setString(10, countryField.getText().trim());
            stmtUser.setString(11, phoneField.getText().trim());
            stmtUser.setString(12, dateOfBirthField.getText().trim());
            stmtUser.executeUpdate();

            if (prefix.equals("S")) {
                String sqlStaff = "INSERT INTO Staff (staffId, position) VALUES (?, ?)";
                PreparedStatement stmtStaff = conn.prepareStatement(sqlStaff);
                stmtStaff.setString(1, userId);
                stmtStaff.setString(2, positionField.getText().trim());
                stmtStaff.executeUpdate();
            } else {
                String sqlCustomer = "INSERT INTO Customer (customerId) VALUES (?)";
                PreparedStatement stmtCustomer = conn.prepareStatement(sqlCustomer);
                stmtCustomer.setString(1, userId);
                stmtCustomer.executeUpdate();
            }
            JOptionPane.showMessageDialog(frame, "Account Created Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();  // Close Create Account frame
            LoginGUI.main(null);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

