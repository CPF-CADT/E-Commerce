package utils;


import java.util.Date;
import java.util.List;
import java.util.Map;

public class Order {
  static int orderId = 0;
  public int userId;
  public  List<Integer> products; // List of productID in the cart
  public Map<Integer, Integer> quantities;
  public  double totalPrice;
  public  Date orderDate;
  public  String status;


  Order(int orderId, int userId, List<Integer> products, Map<Integer, Integer> quantities, Date orderDate, String status) {
    orderId = orderId +1;
    // this.userId = userId;
    this.products = products;
    this.quantities = quantities;
    totalPrice = calculateTotalPrice();
    // this.orderDate = orderDate;
    this.status = status;
  }

  
}
