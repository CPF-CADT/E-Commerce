import static java.lang.System.out;
import java.util.Date;
import utils.Customer;
import utils.Staff;
import utils.User;


public class App {
    public static void main(String[] args) throws Exception {
       // Checking an incorrect password
        User admin1 = new Staff("John", "Doe", "admin1@example.com", "adminpass1", "123 Admin St", "1234567890", new Date(), "Manager");
        User admin2 = new Staff("Alice", "Smith", "admin2@example.com", "adminpass2", "456 Office Rd", "0987654321", new Date(), "Supervisor");
        User admin3 = new Staff("Bob", "Johnson", "bob.johnson@example.com", "admin789", "789 Oak St", "5678901234", new Date(), "Administrator");

        // Attempting Admin Login (Correct Credentials)
        User loginAttempt = new Staff("admin1@example.com", "adminpass1");
        User loggedInAdmin = admin1.login(loginAttempt);
        
        if (loggedInAdmin != null) {
            System.out.println("Login successful! Welcome, " + loggedInAdmin.firstname);
        } else {
            System.out.println("Invalid email or password.");
        }

        // Attempting Admin Login (Incorrect Credentials)
        User wrongLoginAttempt = new Staff("admin1@example.com", "wrongpassword");
        User failedLogin = admin1.login(wrongLoginAttempt);
        
        if (failedLogin != null) {
            System.out.println("Login successful! Welcome, " + failedLogin.firstname);
        } else {
            System.out.println("Invalid email or password.");
        }

        // Display all Admin objects
        System.out.println("\nDisplaying all Admins:");
        for (User admin : User.userList.values()) {
            if (admin instanceof Staff) {
                ((Staff) admin).viewAlladmin();
            }
        }
        // Display total registered admins
        System.out.println("Total registered admins: " + Staff.getTotalAdmins());

        User c1 = new Customer("David", "Miller", "david.miller@example.com", "password123", "101 Sunset Blvd", "9876543210", new Date());
        User c2 = new Customer("Emma", "Brown", "emma.brown@example.com", "securePass456", "202 Maple Ave", "8765432109", new Date());
        User c3 = new Customer("Michael", "Williams", "michael.williams@example.com", "adminSecret789", "303 Pine St", "7654321098", new Date());

        User loginAttempt1 = new Customer("admin1@example.com", "adminpass1");
        User loggedInAdmin1 = c1.login(loginAttempt1);

        
        
        // System.out.println("\nDisplaying all Admins:");
        // for (User admin : User.userList.values()) {
        //     if (admin instanceof Customer) {
        //         ((Customer) admin).viewAlladmin();
        //     }
        // }
        if (loggedInAdmin1 != null) {
            System.out.println("Login successful! Welcome, " + loggedInAdmin1.firstname);
        } else {
            out.println("Invalid email or password.");
        }

        // Attempting Admin Login (Incorrect Credentials)
        User wrongLoginAttempt1 = new Staff("admin1@example.com", "wrongpassword");
        User failedLogin1 = admin1.login(wrongLoginAttempt1);
        
        if (failedLogin1 != null) {
            System.out.println("Login successful! Welcome, " + failedLogin1.firstname);
        } else {
            System.out.println("Invalid email or password.");
        }
    
      
        
    }
}

