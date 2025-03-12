package Testing;

import Database.MySQLConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class CartItem {
    private String productId;
    private int quantity;

    public CartItem(String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}

public class ECommercePurchase {
    private static List<CartItem> cart = new ArrayList<>();

    public static void main(String[] args) {
        String userId = "C006"; // Ensure this user exists in the database!

        addProductToCart("P001", 2); // Use actual productId from your DB
        addProductToCart("P002", 1); // Use actual productId from your DB

        String paymentMethod = "Credit Card";
        String shippingAddress = "123 Main St, City, Country";
        String carrier = "FedEx";
        String trackingNumber = "TRACK12345";

        try (Connection connection = MySQLConnection.getConnection()) {
            processPurchase(userId, paymentMethod, shippingAddress, carrier, trackingNumber, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addProductToCart(String productId, int quantity) {
        cart.add(new CartItem(productId, quantity));
    }

    // Generate orderId with a safe length
    public static String generateOrderId(Connection connection) throws SQLException {
        String query = "SELECT COUNT(*) FROM `Order`"; 
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                int nextId = rs.getInt(1) + 1;
                return "ORD" + nextId;  // Shorter orderId to avoid DB issues
            }
        }
        return "ORD1";
    }

    // Create order safely
    public static String createOrder(String userId, Connection connection) throws SQLException {
        if (!doesUserExist(userId, connection)) {
            System.out.println("Error: User ID " + userId + " does not exist.");
            return null;
        }

        String orderId = generateOrderId(connection);
        String query = "INSERT INTO `Order` (orderId, userId, orderDate, status) VALUES (?, ?, CURRENT_DATE, 'PENDING')";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, orderId);
            stmt.setString(2, userId);
            stmt.executeUpdate();
        }
        return orderId;
    }

    public static void addProductsToOrder(String orderId, List<CartItem> cartItems, Connection connection) throws SQLException {
        String query = "INSERT INTO Order_Items (orderId, productId, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            for (CartItem item : cartItems) {
                if (!doesProductExist(item.getProductId(), connection)) {
                    System.out.println("Error: Product ID " + item.getProductId() + " does not exist.");
                    continue; // Skip invalid product
                }
                stmt.setString(1, orderId);
                stmt.setString(2, item.getProductId());
                stmt.setInt(3, item.getQuantity());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    // Process payment with paymentId auto-incremented by MySQL
    public static boolean processPayment(String orderId, String paymentMethod, double amount, Connection connection) throws SQLException {
        String query = "INSERT INTO Payment (orderId, paymentMethod, amount, paymentStatus) VALUES (?, ?, ?, 'PENDING')";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, orderId);
            stmt.setString(2, paymentMethod);
            stmt.setDouble(3, amount);
            return stmt.executeUpdate() > 0;
        }
    }

    public static void updateProductStock(List<CartItem> cartItems, Connection connection) throws SQLException {
        String query = "UPDATE Product SET stock = stock - ? WHERE productId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            for (CartItem item : cartItems) {
                stmt.setInt(1, item.getQuantity());
                stmt.setString(2, item.getProductId());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    public static boolean shipOrder(String orderId, String shippingAddress, String carrier, String trackingNumber, Connection connection) throws SQLException {
        String query = "INSERT INTO Shipping (orderId, shippingDate, shippingAddress, carrier, trackingNumber, status) VALUES (?, CURRENT_DATE, ?, ?, ?, 'PENDING')";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, orderId);
            stmt.setString(2, shippingAddress);
            stmt.setString(3, carrier);
            stmt.setString(4, trackingNumber);
            return stmt.executeUpdate() > 0;
        }
    }

    public static void completeOrder(String orderId, Connection connection) throws SQLException {
        String query = "UPDATE `Order` SET status = 'COMPLETED' WHERE orderId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, orderId);
            stmt.executeUpdate();
        }
    }

    public static void processPurchase(String userId, String paymentMethod, String shippingAddress, String carrier, String trackingNumber, Connection connection) throws SQLException {
        String orderId = createOrder(userId, connection);
        if (orderId == null) {
            System.out.println("Failed to create order.");
            return;
        }

        addProductsToOrder(orderId, cart, connection);
        double totalAmount = calculateTotalAmount(cart, connection);

        if (!processPayment(orderId, paymentMethod, totalAmount, connection)) {
            System.out.println("Payment processing failed.");
            return;
        }

        updateProductStock(cart, connection);

        if (!shipOrder(orderId, shippingAddress, carrier, trackingNumber, connection)) {
            System.out.println("Shipping failed.");
            return;
        }

        completeOrder(orderId, connection);
        cart.clear();
        System.out.println("Purchase completed successfully! Order ID: " + orderId);
    }

    public static double calculateTotalAmount(List<CartItem> cartItems, Connection connection) throws SQLException {
        double totalAmount = 0;
        String query = "SELECT price FROM Product WHERE productId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            for (CartItem item : cartItems) {
                stmt.setString(1, item.getProductId());
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    double price = rs.getDouble("price");
                    totalAmount += price * item.getQuantity();
                }
            }
        }
        return totalAmount;
    }

    // Check if user exists before creating order
    private static boolean doesUserExist(String userId, Connection connection) throws SQLException {
        String query = "SELECT userId FROM User WHERE userId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    // Check if product exists before adding to order
    private static boolean doesProductExist(String productId, Connection connection) throws SQLException {
        String query = "SELECT productId FROM Product WHERE productId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, productId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }
}
