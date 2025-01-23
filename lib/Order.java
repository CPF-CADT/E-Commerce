import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

public class Order {
  private int orderId;
  private int userId;
  private List<Integer> products; // List of productID in the cart
  private Map<Integer, Integer> quantities;
  private double totalPrice;
  private Date orderDate;
  private String status;


  Order(int orderId, int userId, List<Integer> products, Map<Integer, Integer> quantities, Date orderDate, String status) {
    this.orderId = orderId;
    this.userId = userId;
    this.products = products;
    this.quantities = quantities;
    totalPrice = calculateTotalPrice();
    this.orderDate = orderDate;
    this.status = status;
  }

  private double calculateTotalPrice() {
    
  }

  public int getOrderId() {
    return orderId;
  }

  public int getUserId() {
    return userId;
  }

  public List<Integer> getProducts() {
    return products;
  }

  public Map<Integer, Integer> getQuantities() {
    return quantities;
  }

  public Date getOrderDate() {
    return orderDate;
  }

  public String getStatus() {
    return status;
  }
}
