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

    public Payment(int paymentId, int orderId) {
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
        if (paymentId == other.paymentId) {
            if (orderId == other.orderId) {
                return true;
            }
        }
        return false;
    }

    public static Payment findPayment(Payment paid) {
        for (Payment payment : listOfPayments) {
            if (paid.equals(payment)) {
                return payment;
            }
        }
        return null;
    }

    // New method to update the status of a payment
    public static void updatePaymentStatus(int paymentId, String newStatus) {
        for (Payment payment : listOfPayments) {
            if (payment.getPaymentId() == paymentId) {
                payment.paymentStatus = newStatus;
                break;
            }
        }
    }

    // New method to remove a payment from the list
    public static void removePayment(int paymentId) {
        listOfPayments.removeIf(payment -> payment.getPaymentId() == paymentId);
    }

    // New method to list all payments
    public static void listAllPayments() {
        for (Payment payment : listOfPayments) {
            System.out.println(payment);
        }
    }

    @Override
    public String toString() {
        return " Payment ID     : " + paymentId +
               " Order ID       : " + orderId +
               " Payment Date   : " + paymentDate +
               " Payment Method : " + paymentMethod +
               " Payment Status : " + paymentStatus + 
               " Amount         : " + amount;
    }
}

// Test main
class Main {
    public static void main(String[] args) {
        // Create some payments
        Payment payment1 = new Payment(1, new Date(), "Credit Card", "Completed", 100.0);
        Payment payment2 = new Payment(2, new Date(), "PayPal", "Pending", 200.0);

        // List all payments
        Payment.listAllPayments();

        // Update payment status
        Payment.updatePaymentStatus(2, "Completed");

        // List all payments after update
        Payment.listAllPayments();

        // Remove a payment
        Payment.removePayment(1);

        // List all payments after removal
        Payment.listAllPayments();
    }
}