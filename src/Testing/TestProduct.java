package Testing;

import User.Staff;
import utils.Product;

public class TestProduct {
    public static void main(String[] args) {
        // Simulating an admin staff
        Staff admin = new Staff("Admin", "admin");

        // Test 1: Add Product
        System.out.println("=== Test: Add Product ===");
        Product newProduct = new Product("P001", "Gaming Laptop", 1500.0, 10, "C001", "High-performance laptop for gaming");
        newProduct.saveToDatabase();

        // Test 2: Retrieve Product by ID
        System.out.println("\n=== Test: Retrieve Product ===");
        Product retrievedProduct = Product.getProductById("P001");
        if (retrievedProduct != null) {
            System.out.println("Retrieved Product: " + retrievedProduct);
        } else {
            System.out.println("Product not found.");
        }

        // Test 3: Update Product Stock (Admin only)
        System.out.println("\n=== Test: Update Product Stock ===");
        if (retrievedProduct != null) {
            retrievedProduct.updateStock(10, admin); // Increase stock by 5
            System.out.println("Updated Stock: " + retrievedProduct.getStock());
        }

        // Test 4: Delete Product (Admin only)
        // System.out.println("\n=== Test: Delete Product ===");
        // Product.deleteProduct("P001", admin);

        // // Verify Deletion
        // if (Product.getProductById("P001") == null) {
        //     System.out.println("Product successfully deleted.");
        // } else {
        //     System.out.println("Failed to delete product.");
        // }
    }
}
