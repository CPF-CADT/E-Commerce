package utils;

import java.util.HashMap;
import java.util.Map;

public class Cart {
  static protected int idCounter = 0;
  protected int cartId;
  protected int userId;
  protected Map<String, Integer> quantities = new HashMap<>();
  protected double totalPrice = 0.0;
  
  public Cart(int userId, Map<String, Integer> quantities) {
    cartId = ++idCounter;
    this.userId = userId;
    this.quantities = quantities;
    for (Map.Entry<String, Integer> entry : quantities.entrySet()) {
      totalPrice += Product.productsById.get(entry.getKey()).price * entry.getValue();
    }
  }

}
