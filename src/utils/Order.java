package utils;

import java.util.Date;

public class Order {
  static int orderId = 0;
  public Date orderDate;
  public String status;
  Cart cart;

  public Order(Cart cart, String status) {
    orderId++;
    this.cart = cart;
    this.status = status;
    orderDate = new Date();
  }
  
}
