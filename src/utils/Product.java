package utils;
import java.util.HashMap;

public class Product {
    private static int productIdCounter = 0;
    private int productId;
    public String name;
    public double price;
    private int stock;
    public String category;
    public String description;
    public static HashMap<Integer, Product> productsById = new HashMap<>();

    public Product(String productName, double productPrice, int productStock, String productCategory, String productDescription) {
        this.productId = ++productIdCounter;
        this.name = productName;
        this.price = productPrice;
        this.stock = productStock;
        this.category = productCategory;
        this.description = productDescription;
        productsById.put(this.productId, this);
    }

    // Getter for stock
    // public int getStock(Admin admin, String email, String password) {
    //     if (admin != null && admin.login(email, password)) {
    //         return stock;
    //     } else {
    //         System.out.println("Access denied. Invalid admin credentials.");
    //         return -1;  // Returns -1 to indicate access denial
    //     }
    // }

    // Setter for stock - Only Admins can update stock
    // public void setStock(int newStock, Admin admin, String email, String password) {
    //     if (admin != null && admin.login(email, password)) {
    //         this.stock = newStock;
    //         System.out.println("Stock updated successfully to " + newStock);
    //     } else {
    //         System.out.println("Access denied. Invalid admin credentials.");
    //     }
    // }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
