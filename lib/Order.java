import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

public class Order {
  private long orderId;
  private long userId;
  private List<Long> products; // List of productID in the cart
  private Map<Long, Integer> quantities;
  private double totalPrice;
  private Date orderDate;
  private String status;


  Order(long orderId, long userId, List<Long> products, Map<Long, Integer> quantities, Date orderDate, String status) {
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

  public long getOrderId() {
    return orderId;
  }

  public long getUserId() {
    return userId;
  }

  public List<Long> getProducts() {
    return products;
  }

  public Map<Long, Integer> getQuantities() {
    return quantities;
  }

  public Date getOrderDate() {
    return orderDate;
  }

  public String getStatus() {
    return status;
  }
}
