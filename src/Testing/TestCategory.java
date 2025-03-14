package Testing;    
import java.util.List;
import utils.Category;
import utils.Product;

public class TestCategory {
    public static void main(String[] args) {
        // Define the user role (simulate admin actions)
        String adminRole = "admin";
        String userRole = "user"; // Non-admin role

        // Test 1: Add a Category (Admin only)
        System.out.println("=== Test: Add Category ===");
        Category newCategory = new Category("C001", "Desktop", "Devices and gadgets", 0);
        newCategory.saveToDatabase(adminRole); // Should succeed

        // Test 2: Retrieve Category by ID
        System.out.println("\n=== Test: Retrieve Category ===");
        Category retrievedCategory = Category.getCategoryById("C001");
        if (retrievedCategory != null) {
            System.out.println("Retrieved Category: " + retrievedCategory);
        } else {
            System.out.println("Category not found.");
        }

        // Test 3: List All Categories
        System.out.println("\n=== Test: List All Categories ===");
        Category.listAllCategories();

        // Test 4: Update Category (Admin only)
        System.out.println("\n=== Test: Update Category ===");
        if (retrievedCategory != null) {
            retrievedCategory.updateCategory("Updated Electronics", "All electronic devices", adminRole);
            System.out.println("Updated Category: " + Category.getCategoryById("C001"));
        }

        // Test 5: Retrieve Products by Category
        System.out.println("\n=== Test: Retrieve Products in Category ===");
        List<Product> products = Category.getProductsByCategory("C001");
        if (products.isEmpty()) {
            System.out.println("No products found in this category.");
        } else {
            for (Product p : products) {
                System.out.println(p);
            }
        }

        // Test 6: Delete Category (Admin only)
        // System.out.println("\n=== Test: Delete Category ===");
        // Category.deleteCategory("C01", adminRole); // Should succeed

        // // Verify deletion
        // if (Category.getCategoryById("C01") == null) {
        //     System.out.println("Category successfully deleted.");
        // } else {
        //     System.out.println("Failed to delete category.");
        // }
    }
}
