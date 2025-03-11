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

    // Main Cart (ArrayList) to hold products
    private static List<CartItem> cart = new ArrayList<>();

    public static void main(String[] args) {
        // Example user data
        String orderId = "order123";
        String userId = "user001";
        // Adding products to cart
        addProductToCart("product123", 2);
        addProductToCart("product124", 1);

        // Placing the order with payment details
        String paymentMethod = "Credit Card";
        String shippingAddress = "123 Main St, City, Country";
        String carrier = "FedEx";
        String trackingNumber = "TRACK12345";

        try (Connection connection = MySQLConnection.getConnection()) {
            // Process the purchase
            processPurchase(userId, paymentMethod, shippingAddress, carrier, trackingNumber, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to add products to cart
    public static void addProductToCart(String productId, int quantity) {
        // Check if product already exists in the cart
        for (CartItem item : cart) {
            if (item.getProductId().equals(productId)) {
                // If exists, increase quantity
                item = new CartItem(item.getProductId(), item.getQuantity() + quantity);
                return;
            }
        }
        // If not in the cart, add new CartItem
        cart.add(new CartItem(productId, quantity));
    }

    // Step 1: Create Order
    public static int createOrder(String userId, Connection connection) throws SQLException {
        String query = "INSERT INTO `Order` (userId, orderDate, status) VALUES (?, CURRENT_DATE, 'PENDING')";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, userId);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // Return the generated orderId
            }
        }
        return -1;  // Error if no order is created
    }

    // Step 2: Add Products to Order_Items Table
    public static void addProductsToOrder(int orderId, List<CartItem> cartItems, Connection connection) throws SQLException {
        String query = "INSERT INTO Order_Items (orderId, productId, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            for (CartItem item : cartItems) {
                stmt.setInt(1, orderId);
                stmt.setString(2, item.getProductId());
                stmt.setInt(3, item.getQuantity());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    // Step 3: Process Payment
    public static boolean processPayment(int orderId, String paymentMethod, double amount, Connection connection) throws SQLException {
        String query = "INSERT INTO Payment (orderId, paymentMethod, amount, paymentStatus) VALUES (?, ?, ?, 'PENDING')";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            stmt.setString(2, paymentMethod);
            stmt.setDouble(3, amount);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;  // Return true if payment is successfully added
        }
    }

    // Step 4: Update Product Stock
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

    // Step 5: Ship the Order
    public static boolean shipOrder(int orderId, String shippingAddress, String carrier, String trackingNumber, Connection connection) throws SQLException {
        String query = "INSERT INTO Shipping (orderId, shippingDate, shippingAddress, carrier, trackingNumber, status) VALUES (?, CURRENT_DATE, ?, ?, ?, 'PENDING')";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            stmt.setString(2, shippingAddress);
            stmt.setString(3, carrier);
            stmt.setString(4, trackingNumber);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;  // Return true if shipping is initiated
        }
    }

    // Step 6: Complete the Order
    public static void completeOrder(int orderId, Connection connection) throws SQLException {
        String query = "UPDATE `Order` SET status = 'COMPLETED' WHERE orderId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            stmt.executeUpdate();
        }
    }

    // Step 7: Process the entire purchase
    public static void processPurchase(String userId, String paymentMethod, String shippingAddress, String carrier, String trackingNumber, Connection connection) throws SQLException {
        // 1. Create Order
        int orderId = createOrder(userId, connection);
        if (orderId == -1) {
            System.out.println("Failed to create order.");
            return;
        }

        // 2. Add Products to Order
        addProductsToOrder(orderId, cart, connection);

        // 3. Calculate Total Amount
        double totalAmount = calculateTotalAmount(cart, connection);

        // 4. Process Payment
        if (!processPayment(orderId, paymentMethod, totalAmount, connection)) {
            System.out.println("Payment processing failed.");
            return;
        }

        // 5. Update Product Stock
        updateProductStock(cart, connection);

        // 6. Ship the Order
        if (!shipOrder(orderId, shippingAddress, carrier, trackingNumber, connection)) {
            System.out.println("Shipping failed.");
            return;
        }

        // 7. Complete the Order
        completeOrder(orderId, connection);

        // Clear the cart after successful purchase
        cart.clear();

        System.out.println("Purchase completed successfully!");
    }

    // Helper method to calculate total amount for the order
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
}
