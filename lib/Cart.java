import java.util.List;
import java.util.Map;

public class Cart {
  private int cartId;
  private int userId;
  private List<Integer> products; // List of productID in the cart
  private Map<Integer, Integer> quantities;
  private double totalPrice;

  Cart(int cartId, int userId, List<Integer> products, Map<Integer, Integer> quantities) {
    this.cartId = cartId;
    this.userId = userId;
    this.products = products;
    this.quantities = quantities;
    totalPrice = calculateTotalPrice();
  }

}
