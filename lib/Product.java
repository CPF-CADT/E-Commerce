public class Product {
    static  int productId;
    public String name;
    public  double price;
    public  int stock;
    public  String category;
    public String description;

   
    public Product(int productId, String productName, double productPrice, int productStock, String productCategory, String productDescription) {
        productId = productId +1;
        this.name = productName;
        this.price = productPrice;
        this.stock = productStock;
        this.category = productCategory;
        this.description = productDescription;
    }

    
    
}
