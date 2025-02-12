package utils;

import java.util.HashMap;
import java.util.Map;

public class Cart {
  static int cartId = 0;
  int userId;
  Map<Integer, Integer> quantities = new HashMap<Integer, Integer>();
  double totalPrice;
  
  public Cart(int userId, Map<Integer, Integer> quantities) {
    cartId++;
    this.userId = userId;
    this.quantities = quantities;
    // totalPrice = calculateTotalPrice();
  }

}
