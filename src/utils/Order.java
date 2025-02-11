package utils;

import java.util.Date;

public class Order {
  static int orderId = 0;
  public Cart cart;
  public Date orderDate = new Date();
  public String status;

  public Order(Cart cart, String status) {
    orderId++;
    this.cart = cart;
    this.status = status;
  }
  
}
