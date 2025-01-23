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

    // Getters and Setters
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }


    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
}
