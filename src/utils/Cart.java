package utils;

import java.util.HashMap;
import java.util.Map;

public class Cart {
  static int idCounter = 0;
  public int cartId;
  int userId;
  Map<Product, Integer> quantities = new HashMap<Product, Integer>();
  double totalPrice = 0.0;
  
  public Cart(int userId, Map<Product, Integer> quantities) {
    cartId = ++idCounter;
    this.userId = userId;
    this.quantities = quantities;
    for (Map.Entry<Product,Integer> entry : quantities.entrySet()) {
      totalPrice += entry.getKey().price * entry.getValue();
    }
  }

}
