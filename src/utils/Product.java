package utils;
public class Product {
    private static int productId;
    public String name;
    public  double price;
    private int stock;
    public  String category;
    public String description;

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

    public int getStock() {
        return stock;
    }
    
    public void setStock(int stock, Admin admin) {
        if (admin.getPassword() != null && Admin.isValidPassword(admin.getPassword())) {
            this.stock = stock;
            System.out.println("Stock updated successfully.");
        } else {
            System.out.println("Access denied. Incorrect password.");
        }
    }
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
