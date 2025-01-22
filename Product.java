public class Product {
    private long productId;
    private String name;
    private double price;
    private int stock;
    private String category;
    private String description;

   
    public Product(long productId, String productName, double productPrice, int productStock, String productCategory, String productDescription) {
        this.productId = productId;
        this.name = productName;
        this.price = productPrice;
        this.stock = productStock;
        this.category = productCategory;
        this.description = productDescription;
    }

    
    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String productName) {
        this.name = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double productPrice) {
        this.price = productPrice;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int productStock) {
        this.stock = productStock;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String productCategory) {
        this.category = productCategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String productDescription) {
        this.description = productDescription;
    }
}
