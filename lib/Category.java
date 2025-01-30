import java.util.Date;

public class Category {
    private int categoryId;
    private String categoryName;
    private String description;
    private boolean isActive;
    private Date createdDate;
    private Date updatedDate;

    // Constructor
    public Category(int id, String name, String desc, int parentId, boolean active, String image, Date created, Date updated) {
        this.categoryId = id;
        this.categoryName = name;
        this.description = desc;
        this.isActive = active;
        this.createdDate = created;
        this.updatedDate = updated;
    }

    
}
