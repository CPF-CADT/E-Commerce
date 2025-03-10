import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Category {
    protected String ID;
    protected String name;
    static Map<String, Category> categoryList = new HashMap<>();

    public Category(String ID, String name){
        this.ID = ID;
        this.name = name;
    }
    //CRUD 

    //Create
    public static  void  addCategory(Category category, String userRole){
        if(!userRole.equalsIgnoreCase("admin")){
            System.out.println("Access denied!! Only admin can add categories");
            return;
        }
        categoryList.put(category.ID, category);
        System.out.println("Category add : "+ category);
    }

    //retrive
    public static  Category getCategory(String CategoryID){
        return  categoryList.get(CategoryID);
    }

    //Update
    public static void updateCategory(String categoryID, String newName, String userRole) {
        if (!userRole.equalsIgnoreCase("admin")) {
            System.out.println("Access denied! Only admins can update categories.");
            return;
        }
        Category category = categoryList.get(categoryID);
        if (category != null) {
            category.name = newName; // Directly modifying the protected field
            System.out.println("Category updated: " + category);
        } else {
            System.out.println("Category not found.");
        }
    }


    public static void deleteCategory(String category, String userRole){
        if(!userRole.equalsIgnoreCase("admin")){
            System.out.println("Access denied!! Only admin can delete categories");
            return;
        }
        if (categoryList.remove(category) != null) {
            System.out.println("Category deleted.");
        } else {
            System.out.println("Category not found.");
        }
    }

    @Override
    public String toString() {
        return "Category [ID=" + ID + ", name=" + name + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Category other = (Category) obj;
        if (!Objects.equals(this.ID, other.ID)) {
            return false;
        }
        return this.name.equalsIgnoreCase(other.name); // ignore comparing lower case and upper case of string
    }

    
    
}
