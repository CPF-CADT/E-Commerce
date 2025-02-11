package utils;

public class Payment {
    static  int paymentId;
    public  int orderId;
    public  int paymentDate;
    public  String paymentMethod;
    public  String paymentStatus;

    
    public Payment(int paymentId, int order, int date, String method, String status) {
        paymentId = paymentId +1;
        this.orderId = order;
        this.paymentDate = date;
        this.paymentMethod = method;
        this.paymentStatus = status;
    }

    
 
}
