package utils;

import java.util.ArrayList;
import java.util.Date;

public class Payment {
    static int counter = 1;
    private int paymentId; //PK
    public int orderId; //FK
    public Date paymentDate;
    public String paymentMethod;
    public String paymentStatus;
    public double amount;

    static ArrayList<Payment> listOfPayments = new ArrayList<Payment>();
    
    
    // Constructor
    public Payment(int orderId, Date paymentDate, String paymentMethod, String paymentStatus, double amount) {
        this.paymentId = counter++;
        this.orderId = orderId;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.amount = amount;
        listOfPayments.add(this);
    }

    public Payment(int paymentId,int orderId){
        this.paymentId = paymentId;
        this.orderId = orderId;
    }

    // Getter for paymentId
    public int getPaymentId() {
        return paymentId;
    }

    @Override
    public boolean equals(Object obj) {
        Payment other = (Payment) obj;
        if (paymentId == other.paymentId){
            if(orderId == other.orderId){
                return true;
            }
        }
        return false;
    }

    public static Payment findPayment(Payment paid){
        for(Payment payment : listOfPayments){
            if(paid.equals(payment)){
                return payment;
            }
        }
        return null;
    }
    @Override
    public String toString() {
        return 
        " Payment ID     : " + paymentId +
        " Order ID       : " + orderId +
        " Payment Date   : " + paymentDate +
        " Payment Method : " + paymentMethod +
        " Payment Status : " + paymentStatus + 
        " Amount         : " + amount;
    }
}
