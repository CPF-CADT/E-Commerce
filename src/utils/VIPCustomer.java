package src.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import utils.Customer;

public class VIPCustomer extends Customer {
    private double voucherBalance;
    private double discountCard; // Percentage (e.g., 20% = 0.20)
    private Date vipCardExpiry;

    public VIPCustomer(String firstname, String lastname, String email, String password, String address, String phoneNumber, Date dateOfBirth, double discountCard, Date vipCardExpiry) {
        super(firstname, lastname, email, password, address, phoneNumber, dateOfBirth);
        this.voucherBalance = 0.0;
        this.discountCard = discountCard; // Set the discount percentage
        this.vipCardExpiry = vipCardExpiry;
    }

    public boolean isVipCardValid() {
        return vipCardExpiry.after(new Date());
    }

    public void addVoucher(double amount) {
        this.voucherBalance += amount;
        System.out.println("Added $" + amount + " to voucher balance. Total: $" + this.voucherBalance);
    }

    public void purchaseProduct(double productPrice) {
        System.out.println("\nProcessing purchase of $" + productPrice + "...");

        double discountAmount = 0.0;
        if (isVipCardValid()) {
            discountAmount = productPrice * discountCard; //  Use `discountCard`
            System.out.println("VIP Card valid! Applied discount of $" + discountAmount + " (" + (discountCard * 100) + "%)");
        } else {
            System.out.println("VIP Card expired! No discount applied.");
        }

        double finalPrice = productPrice - discountAmount;

        // Use voucher balance first
        if (voucherBalance >= finalPrice) {
            voucherBalance -= finalPrice;
            System.out.println("Paid using voucher! Remaining balance: $" + voucherBalance);
        } else {
            double remainingToPay = finalPrice - voucherBalance;
            System.out.println("Used $" + voucherBalance + " from voucher.");
            System.out.println("Remaining amount $" + remainingToPay + " paid in cash.");
            voucherBalance = 0;
        }
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return super.toString() + ", Voucher Balance: $" + voucherBalance +
               ", Discount Card: " + (discountCard * 100) + "%" +
               ", VIP Card Expiry: " + sdf.format(vipCardExpiry);
    }
}
