package utils;

import java.util.HashMap;
import java.util.Map;

public class Cart {
  static int idCounter = 0;
  public int cartId;
  int userId;
  Map<Integer, Integer> quantities = new HashMap<>();
  double totalPrice = 0.0;
  
  public Cart(int userId, Map<Integer, Integer> quantities) {
    cartId = ++idCounter;
    this.userId = userId;
    this.quantities = quantities;
    for (Map.Entry<Integer, Integer> entry : quantities.entrySet()) {
      totalPrice += Product.productsById.get(entry.getKey()).price * entry.getValue();
    }
  }

}
