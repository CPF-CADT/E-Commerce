import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart {
  private long cartId;
  private long userId;
  private List<Long> products; // List of productID in the cart
  private Map<Long, Integer> quantities;
  private double totalPrice;

  Cart(long cartId, long userId, List<Long> products, Map<Long, Integer> quantities) {
    this.cartId = cartId;
    this.userId = userId;
    this.products = products;
    this.quantities = quantities;
    totalPrice = calculateTotalPrice();
  }

  private double calculateTotalPrice() {
    
  }

  public long getCartId() {
    return cartId;
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
}
