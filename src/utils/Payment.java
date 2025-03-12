package utils;

import java.util.ArrayList;
import java.util.Date;

public class Payment {
    static int counter = 1;
    private int paymentId; //PK
    protected  int orderId; //FK
    protected  Date paymentDate;
    protected  String paymentMethod;
    protected  String paymentStatus;
    protected  double amount;
    
    // Constructor for new payment
    public Payment(int orderId, String paymentMethod, double amount) {
        this.paymentId = counter++;
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.paymentStatus = "PENDING";
        this.paymentDate = new Date();
    }
    
    // Getter for paymentId (since it's private)
    public int getPaymentId() {
        return paymentId;
    }
    
    // Process payment
    public boolean processPayment() {
        try {
            // Simulate payment processing
            boolean success = validatePayment();
            
            if (success) {
                // Update payment status
                this.paymentStatus = "COMPLETED";
                return true;
            } else {
                this.paymentStatus = "FAILED";
                return false;
            }
        } catch (Exception e) {
            this.paymentStatus = "ERROR";
            System.err.println("Error processing payment: " + e.getMessage());
            return false;
        }
    }
    
    // Validate payment details
    public boolean validatePayment() {
        if (this.orderId <= 0) {
            return false;
        }
        
        if (this.amount <= 0) {
            return false;
        }
        
        if (this.paymentMethod == null || this.paymentMethod.trim().isEmpty()) {
            return false;
        }
        
        return true;
    }
    
    // Refund payment
    public boolean refundPayment() {
        if ("COMPLETED".equals(this.paymentStatus)) {
            this.paymentStatus = "REFUNDED";
            return true;
        } else {
            return false;
        }
    }
    
    // Cancel pending payment
    public boolean cancelPayment() {
        if ("PENDING".equals(this.paymentStatus)) {
            this.paymentStatus = "CANCELLED";
            return true;
        } else {
            return false;
        }
    }
    

    
    // Display payment information
    public String getPaymentSummary() {
        return "Payment ID: " + this.paymentId +
               "\nOrder ID: " + this.orderId +
               "\nAmount: $" + String.format("%.2f", this.amount) +
               "\nMethod: " + this.paymentMethod +
               "\nStatus: " + this.paymentStatus +
               "\nDate: " + this.paymentDate;
    }
    
    // Static method to find payment by ID (would connect to a storage system in real app)
    public static Payment findPaymentById(ArrayList<Payment> payments, int id) {
        for (Payment payment : payments) {
            if (payment.getPaymentId() == id) {
                return payment;
            }
        }
        return null;
    }
    
    // Update payment method
    public void updatePaymentMethod(String newMethod) {
        if ("PENDING".equals(this.paymentStatus)) {
            this.paymentMethod = newMethod;
        }
    }
    
    // Update payment amount
    public boolean updateAmount(double newAmount) {
        if ("PENDING".equals(this.paymentStatus) && newAmount > 0) {
            this.amount = newAmount;
            return true;
        }
        return false;
    }
}    // // Check if payment is successful
    // public boolean isSuccessful() {
    //     return "COMPLETED".equals(this.paymentStatus);
    // }
    
    // // Calculate tax amount (assuming tax rate passed as parameter)
    // public double calculateTax(double taxRate) {
    //     return this.amount * taxRate;
    // }
    
    // // Calculate total with tax
    // public double calculateTotalWithTax(double taxRate) {
    //     return this.amount + calculateTax(taxRate);
    // }