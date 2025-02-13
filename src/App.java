import java.util.Date;
import utils.*;

public class App {
    public static void main(String[] args) throws Exception {
       // Checking an incorrect password
        Staff admin1 = new Staff("John", "Doe", "admin1@example.com", "adminpass1", "123 Admin St", "1234567890", new Date(), "Manager");
        Staff admin2 = new Staff("Alice", "Smith", "admin2@example.com", "adminpass2", "456 Office Rd", "0987654321", new Date(), "Supervisor");
        Staff admin3 = new Staff("Bob", "Johnson", "bob.johnson@example.com", "admin789", "789 Oak St", "5678901234", new Date(), "Administrator");

        // Attempting Admin Login (Correct Credentials)
        Staff loginAttempt = new Staff("admin1@example.com", "adminpass1");
        Staff loggedInAdmin = admin1.login(loginAttempt);
        
        if (loggedInAdmin != null) {
            System.out.println("Login successful! Welcome, " + loggedInAdmin.firstname);
        } else {
            System.out.println("Invalid email or password.");
        }

        // Attempting Admin Login (Incorrect Credentials)
        Staff wrongLoginAttempt = new Staff("admin1@example.com", "wrongpassword");
        Staff failedLogin = admin1.login(wrongLoginAttempt);
        
        if (failedLogin != null) {
            System.out.println("Login successful! Welcome, " + failedLogin.firstname);
        } else {
            System.out.println("Invalid email or password.");
        }

        // Display all Admin objects
        System.out.println("\nDisplaying all Admins:");
        for (Staff admin : Staff.adminsById.values()) {
            admin.viewAlladmin();
        }
        // Display total registered admins
        System.out.println("Total registered admins: " + Staff.getTotalAdmins());

        Customer c1 = new Customer("John", "Doe", "admin1@example.com", "adminpass1", "123 Admin St", "1234567890", new Date());
        Customer c2 = new Customer("Alice", "Smith", "admin2@example.com", "adminpass2", "456 Office Rd", "0987654321", new Date());
        Customer c3 = new Customer("Bob", "Johnson", "bob.johnson@example.com", "admin789", "789 Oak St", "5678901234", new Date());
        Customer loginAttempt1 = new Customer("admin1@example.com", "adminpass1");
        Customer loggedInAdmin1 = c1.login(loginAttempt1);
        
        if (loggedInAdmin1 != null) {
            System.out.println("Login successful! Welcome, " + loggedInAdmin1.firstname);
        } else {
            System.out.println("Invalid email or password.");
        }

        // Attempting Admin Login (Incorrect Credentials)
        Staff wrongLoginAttempt1 = new Staff("admin1@example.com", "wrongpassword");
        Staff failedLogin1 = admin1.login(wrongLoginAttempt1);
        
        if (failedLogin1 != null) {
            System.out.println("Login successful! Welcome, " + failedLogin1.firstname);
        } else {
            System.out.println("Invalid email or password.");
        }

        
    }
}

