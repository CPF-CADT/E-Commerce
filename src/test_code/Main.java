package test_code;
import java.util.Scanner;
import utils.PhoneNumberHandleFormat;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter phone number: ");
        String phoneNumber = scanner.nextLine();
        
        try {
            PhoneNumberHandleFormat.validatePhoneNumber(phoneNumber);
            System.out.println("Valid phone number!");
        } catch (PhoneNumberHandleFormat e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        scanner.close();
    }
}
