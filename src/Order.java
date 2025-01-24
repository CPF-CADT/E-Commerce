import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

public class Order {
  private Cart cart;
  private int orderId;
  private List<Integer> products; // List of productID in the cart
  private Map<Integer, Boolean> isOrder;
  private double totalPrice;
  private Date orderDate;
  private String status;


  Order(int orderId, List<Integer> products, Map<Integer, Boolean> isOrder, Date orderDate, String status) {
    this.orderId = orderId;
    this.products = products;
    this.isOrder = new HashMap<Integer, Boolean>(isOrder);
    totalPrice = calculateTotalPrice();
    this.orderDate = orderDate;
    this.status = status;
  }

  private double calculateTotalPrice() {
    
  }

  public int getOrderId() {
    return orderId;
  }

  public List<Integer> getProducts() {
    return products;
  }

  public Map<Integer, Boolean> isOrder() {
    return isOrder;
  }

  public Date getOrderDate() {
    return orderDate;
  }

  public String getStatus() {
    return status;
  }
}
