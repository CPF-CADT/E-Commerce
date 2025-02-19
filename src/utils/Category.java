package utils;

import java.util.HashMap;
import java.util.Map;

public class Category {
    private String id;   
    public String name; 
    static Map<String, Category> categoryList = new HashMap<>();
    
    // Constructor to initialize Category
    public Category(String id, String name) {
        this.id = id;
        this.name = name;
        categoryList.put(id, this);
    }

    public String getId() {
        if (id == null || id.isEmpty()) {
            return "Invalid ID";
        }
        return id;
    }

    public static void addCategory(Category category, Map<String, Category> categoryMap, String userRole) {
        if (!userRole.equals("admin")) {
            System.out.println("Access denied: Only admin can add categories.");
            return;
        }
        if (category == null || category.getId() == null || category.getId().isEmpty()) {
            return;
        }
        categoryMap.put(category.getId(), category);
    }

    public static Category searchCategoryById(String id, Map<String, Category> categoryMap) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        return categoryList.get(id);
    }

    public static void updateCategoryName(String id, String newName, String userRole) {
        if (!userRole.equals("admin")) {
            System.out.println("Access denied: Only admin can update category names.");
            return;
        }
        Category category = categoryList.get(id);
        if (category != null) {
            category.name = newName;
        }
    }

    public static void removeCategory(String id, String userRole) {
        if (!userRole.equals("admin")) {
            System.out.println("Access denied: Only admin can remove categories.");
            return;
        }
        categoryList.remove(id);
    }

    public static void listAllCategories() {
        for (Map.Entry<String, Category> entry : categoryList.entrySet()) {
            System.out.println("ID: " + entry.getKey() + ", Name: " + entry.getValue().name);
        }
    }
}

class Main {
    public static void main(String[] args) {
        // Create a map to store categories
        Map<String, Category> categoryMap = new HashMap<>();

        // Create some categories
        Category electronics = new Category("1", "Electronics");
        Category clothing = new Category("2", "Clothing");

        // User role
        String userRole = "admin";

        // Add categories
        Category.addCategory(electronics, categoryMap, userRole);
        Category.addCategory(clothing, categoryMap, userRole);

        // Update category name
        Category.updateCategoryName("1", "Consumer Electronics", userRole);

        // List all categories
        Category.listAllCategories();

        // Remove a category
        Category.removeCategory("2", userRole);

        // List all categories after removal
        Category.listAllCategories();

        // Search for categories
        String[] searchIds = {"1", "2"}; 
        for (String id : searchIds) {
            Category foundCategory = Category.searchCategoryById(id, categoryMap);
            if (foundCategory != null) {
                System.out.println("Found Category: " + foundCategory.name);
            } else {
                System.out.println("Category with ID " + id + " not found.");
            }
        }
    }
}