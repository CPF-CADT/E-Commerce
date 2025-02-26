package utils;

import java.util.Date;

public class Order {
  static protected int orderId = 0;
  protected Date orderDate;
  protected String status;
  protected Cart cart;

  public Order(Cart cart, String status) {
    orderId++;
    this.cart = cart;
    this.status = status;
    orderDate = new Date();
  }
  
}
