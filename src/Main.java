
import Exception.InvalidTextException;
import Exception.PhoneNumberHandleFormat;
import java.util.Scanner;
import utils.Product;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter phone number: ");
        String phoneNumber = scanner.nextLine();
        // PhoneNumberHandleFormat q = new PhoneNumberHandleFormat(message:"Invalid Number");
        
        try {
            PhoneNumberHandleFormat l = new PhoneNumberHandleFormat(phoneNumber,"^\\+?[0-9]+$" );
            System.out.println("Valid phone number!");
        } catch (PhoneNumberHandleFormat e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        try { 
            System.out.print("Enter product ID: ");
            String productId = scanner.nextLine();
            System.out.print("Enter product name (exactly two words): ");
            String name = scanner.nextLine();
            InvalidTextException l = new InvalidTextException(name, "^[a-zA-Z0-9]+( [a-zA-Z0-9]+)*$");
            
            System.out.print("Enter product price: ");
            double price = scanner.nextDouble();
            
            System.out.print("Enter product stock: ");
            int stock = scanner.nextInt();
            
            scanner.nextLine(); // Consume newline
            
            System.out.print("Enter product category: ");
            String category = scanner.nextLine();
            
            System.out.print("Enter product description: ");
            String description = scanner.nextLine();
            
        
        
            Product product = new Product(productId,name, price, stock, category, description);
            System.out.println("Product created successfully: " + product);
            
        } catch (InvalidTextException e) {
            System.out.println("An unexpected error occurred.");
        } finally {
            scanner.close();
        }
    }
}

