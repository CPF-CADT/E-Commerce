import java.util.Date;

public class Category {
    private int categoryId;
    private String categoryName;
    private String description;
    private int parentCategoryId;
    private boolean isActive;
    private String imageUrl;
    private Date createdDate;
    private Date updatedDate;

    // Constructor
    public Category(int id, String name, String desc, int parentId, boolean active, String image, Date created, Date updated) {
        this.categoryId = id;
        this.categoryName = name;
        this.description = desc;
        this.parentCategoryId = parentId;
        this.isActive = active;
        this.imageUrl = image;
        this.createdDate = created;
        this.updatedDate = updated;
    }

    
}
