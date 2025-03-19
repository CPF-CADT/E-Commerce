package GUI;

import Database.MySQLConnection;
import utils.Product;
import utils.Category;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import java.sql.SQLException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class CategoryProductGUI {
    private static DefaultListModel<String> categoryListModel;
    private static DefaultListModel<String> productListModel;
    private static JList<String> categoryList;
    private static JList<String> productList;
    private static JTextArea productDetailsArea;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(CategoryProductGUI::createGUI);
    }

    public static void createGUI() {
        JFrame frame = new JFrame("E-Commerce System");
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        // Title panel
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("E-Commerce Product Browser", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel);
        frame.add(titlePanel, BorderLayout.NORTH);

        // Main content panel with categories and products
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(250);
        
        // Category panel
        JPanel categoryPanel = new JPanel(new BorderLayout());
        JLabel categoryLabel = new JLabel("Categories", SwingConstants.CENTER);
        categoryLabel.setFont(new Font("Arial", Font.BOLD, 16));
        categoryPanel.add(categoryLabel, BorderLayout.NORTH);
        
        categoryListModel = new DefaultListModel<>();
        categoryList = new JList<>(categoryListModel);
        categoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        categoryPanel.add(new JScrollPane(categoryList), BorderLayout.CENTER);
        
        // Product panel
        JPanel productPanel = new JPanel(new BorderLayout());
        JLabel productLabel = new JLabel("Products", SwingConstants.CENTER);
        productLabel.setFont(new Font("Arial", Font.BOLD, 16));
        productPanel.add(productLabel, BorderLayout.NORTH);
        
        productListModel = new DefaultListModel<>();
        productList = new JList<>(productListModel);
        productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productPanel.add(new JScrollPane(productList), BorderLayout.CENTER);
        
        // Product details panel
        JPanel detailsPanel = new JPanel(new BorderLayout());
        JLabel detailsLabel = new JLabel("Product Details", SwingConstants.CENTER);
        detailsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        detailsPanel.add(detailsLabel, BorderLayout.NORTH);
        
        productDetailsArea = new JTextArea();
        productDetailsArea.setEditable(false);
        productDetailsArea.setLineWrap(true);
        productDetailsArea.setWrapStyleWord(true);
        detailsPanel.add(new JScrollPane(productDetailsArea), BorderLayout.CENTER);
        
        // Right side split pane for products and details
        JSplitPane rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        rightSplitPane.setDividerLocation(250);
        rightSplitPane.setTopComponent(productPanel);
        rightSplitPane.setBottomComponent(detailsPanel);
        
        splitPane.setLeftComponent(categoryPanel);
        splitPane.setRightComponent(rightSplitPane);
        
        frame.add(splitPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton refreshButton = new JButton("Refresh Data");
        buttonPanel.add(refreshButton);
        
        JButton exitButton = new JButton("Exit");
        buttonPanel.add(exitButton);
        
        frame.add(buttonPanel, BorderLayout.SOUTH);
        
        // Load categories from database
        loadCategories();
        
        // Add event listeners
        categoryList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedCategory = categoryList.getSelectedValue();
                    if (selectedCategory != null) {
                        loadProductsByCategory(selectedCategory);
                    }
                }
            }
        });
        
        productList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedProduct = productList.getSelectedValue();
                    if (selectedProduct != null) {
                        displayProductDetails(selectedProduct);
                    }
                }
            }
        });
        
        refreshButton.addActionListener(e -> loadCategories());
        
        exitButton.addActionListener(e -> {
            MySQLConnection.closeConnection();
            System.exit(0);
        });
        
        frame.setVisible(true);
    }

    private static void loadCategories() {
        // categoryListModel.clear();
        // productListModel.clear();
        // productDetailsArea.setText("");
        
        try {
            // Get all categories from the database
            String sql = "SELECT categoryId, name FROM Category";
            try (var conn = MySQLConnection.getConnection();
                 var stmt = conn.createStatement();
                 var rs = stmt.executeQuery(sql)) {
                
                while (rs.next()) {
                    categoryListModel.addElement(rs.getString("name"));
                }
            }
            
            if (categoryListModel.isEmpty()) {
                categoryListModel.addElement("No categories found");
            }
        } catch (SQLException | NullPointerException e) {
            JOptionPane.showMessageDialog(null, 
                "Error loading categories: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static void loadProductsByCategory(String name) {
        // productListModel.clear();
        // productDetailsArea.setText("");
        
        try {
            // First get the categoryId
            String categoryIdQuery = "SELECT * FROM Product WHERE name = ?";
            String categoryId = null;
            
            try (var conn = MySQLConnection.getConnection();
                 var stmt = conn.prepareStatement(categoryIdQuery)) {
                
                stmt.setString(1, name);
                var rs = stmt.executeQuery();
                
                if (rs.next()) {
                    categoryId = rs.getString("categoryId");
                }
            }
            
            if (categoryId != null) {
                // Now get all products for this category
                List<Product> products = Category.getProductsByCategory(categoryId);
                
                if (products.isEmpty()) {
                    productListModel.addElement("No products found");
                } else {
                    for (Product product : products) {
                        productListModel.addElement(product.getName());
                    }
                }
            }
        } catch (SQLException | NullPointerException e) {
            JOptionPane.showMessageDialog(null, 
                "Error loading products: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // private static void loadProductsByCategory(String name) {
    //     productListModel.clear();
    //     productDetailsArea.setText("");
        
    //     try {
    //         // First get the categoryId
    //         String categoryIdQuery = "SELECT categoryId FROM Category WHERE name = ?";
    //         String categoryId = null;
            
    //         try (var conn = MySQLConnection.getConnection();
    //              var stmt = conn.prepareStatement(categoryIdQuery)) {
                
    //             stmt.setString(1, name);
    //             var rs = stmt.executeQuery();
                
    //             if (rs.next()) {
    //                 categoryId = rs.getString("categoryId");
    //             }
    //         }
            
    //         if (categoryId != null) {
    //             // Now get all products for this category
    //             List<Product> products = Product.getProductsByCategory(categoryId); // Updated to use the new method
                
    //             if (products.isEmpty()) {
    //                 productListModel.addElement("No products found");
    //             } else {
    //                 for (Product product : products) {
    //                     productListModel.addElement(product.getName());
    //                 }
    //             }
    //         }
    //     } catch (SQLException | NullPointerException e) {
    //         JOptionPane.showMessageDialog(null, 
    //             "Error loading products: " + e.getMessage(), 
    //             "Database Error", 
    //             JOptionPane.ERROR_MESSAGE);
    //     }
    // }
    
    private static void displayProductDetails(String productName) {
        try {
            // Get the selected category name
            String categoryName = categoryList.getSelectedValue();
            if (categoryName == null) return;
            
            // Get categoryId
            String categoryIdQuery = "SELECT categoryId FROM Category WHERE name = ?";
            String categoryId = null;
            
            try (var conn = MySQLConnection.getConnection();
                 var stmt = conn.prepareStatement(categoryIdQuery)) {
                
                stmt.setString(1, categoryName);
                var rs = stmt.executeQuery();
                
                if (rs.next()) {
                    categoryId = rs.getString("categoryId");
                }
            }
            
            if (categoryId != null) {
                // Get product details
                String productQuery = "SELECT * FROM Product WHERE name = ? AND categoryId = ?";
                
                try (var conn = MySQLConnection.getConnection();
                     var stmt = conn.prepareStatement(productQuery)) {
                    
                    stmt.setString(1, productName);
                    stmt.setString(2, categoryId);
                    var rs = stmt.executeQuery();
                    
                    if (rs.next()) {
                        StringBuilder details = new StringBuilder();
                        details.append("Product ID: ").append(rs.getString("productId")).append("\n\n");
                        details.append("Name: ").append(rs.getString("name")).append("\n\n");
                        details.append("Price: $").append(rs.getDouble("price")).append("\n\n");
                        details.append("Stock: ").append(rs.getInt("stock")).append("\n\n");
                        details.append("Description: ").append(rs.getString("description")).append("\n");
                        
                        productDetailsArea.setText(details.toString());
                    }
                }
            }
        } catch (SQLException | NullPointerException e) {
            JOptionPane.showMessageDialog(null, 
                "Error displaying product details: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}