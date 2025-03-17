package Testing;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;

public class CustomerGUI {
    private JFrame frame;
    private DefaultListModel<String> productListModel;
    private DefaultListModel<String> cartListModel;
    private JList<String> productList;
    private JList<String> cartList;

    private static final String URL = "jdbc:mysql://localhost:3306/category_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public CustomerGUI() {
        frame = new JFrame("Customer Shop");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(1, 2));

        // Product List Panel
        JPanel productPanel = new JPanel(new BorderLayout());
        productListModel = new DefaultListModel<>();
        productList = new JList<>(productListModel);
        productPanel.add(new JLabel("Available Products"), BorderLayout.NORTH);
        productPanel.add(new JScrollPane(productList), BorderLayout.CENTER);
        JButton addToCartButton = new JButton("Add to Cart");
        productPanel.add(addToCartButton, BorderLayout.SOUTH);

        // Cart Panel
        JPanel cartPanel = new JPanel(new BorderLayout());
        cartListModel = new DefaultListModel<>();
        cartList = new JList<>(cartListModel);
        cartPanel.add(new JLabel("Your Cart"), BorderLayout.NORTH);
        cartPanel.add(new JScrollPane(cartList), BorderLayout.CENTER);
        JButton checkoutButton = new JButton("Checkout");
        cartPanel.add(checkoutButton, BorderLayout.SOUTH);

        frame.add(productPanel);
        frame.add(cartPanel);

        // Load products from the database
        loadProductsFromDatabase();

        // Add to Cart Action
        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedProduct = productList.getSelectedValue();
                if (selectedProduct != null) {
                    cartListModel.addElement(selectedProduct);
                }
            }
        });

        // Checkout Action
        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cartListModel.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Your cart is empty!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Checkout successful! Thank you for shopping.");
                    cartListModel.clear();
                }
            }
        });

        frame.setVisible(true);
    }

    private void loadProductsFromDatabase() {
        String sql = "SELECT name, price FROM products";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String productName = rs.getString("name");
                double price = rs.getDouble("price");
                productListModel.addElement(productName + " - $" + price);
            }

        } catch (SQLException e) {
            System.out.println("Error loading products: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new CustomerGUI();
    }
}