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

    public static void addCategory(Category category, Map<String, Category> categoryMap) {
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

}
//test main
class Main {
    public static void main(String[] args) {
        // Create a map to store categories
        Map<String, Category> categoryMap = new HashMap<>();

        // Create some categories
        Category electronics = new Category("1", "Electronics");

        Category.addCategory(electronics, categoryMap);

        String[] searchIds = {"1"}; 
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

