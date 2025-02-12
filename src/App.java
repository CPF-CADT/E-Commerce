import java.util.Date;
import utils.Admin;
//import utils.Customer;
public class App {
    public static void main(String[] args) throws Exception {
        Checking an incorrect password
        Admin admin1 = new Admin("John", "Doe", "admin1@example.com", "adminpass1", "123 Admin St", "1234567890", new Date(), "Manager");
        Admin admin2 = new Admin("Alice", "Smith", "admin2@example.com", "adminpass2", "456 Office Rd", "0987654321", new Date(), "Supervisor");
        Admin admin3 = new Admin("Bob", "Johnson", "bob.johnson@example.com", "admin789", "789 Oak St", "5678901234", new Date(), "Administrator");

        // Attempting Admin Login (Correct Credentials)
        Admin loginAttempt = new Admin("admin1@example.com", "adminpass1");
        Admin loggedInAdmin = admin1.login(loginAttempt);
        
        if (loggedInAdmin != null) {
            System.out.println("Login successful! Welcome, " + loggedInAdmin.firstname);
        } else {
            System.out.println("Invalid email or password.");
        }

        // Attempting Admin Login (Incorrect Credentials)
        Admin wrongLoginAttempt = new Admin("admin1@example.com", "wrongpassword");
        Admin failedLogin = admin1.login(wrongLoginAttempt);
        
        if (failedLogin != null) {
            System.out.println("Login successful! Welcome, " + failedLogin.firstname);
        } else {
            System.out.println("Invalid email or password.");
        }

        // Display all Admin objects
        System.out.println("\nDisplaying all Admins:");
        for (Admin admin : Admin.adminsById.values()) {
            admin.viewAlladmin();
        }

        // Display total registered admins
        System.out.println("Total registered admins: " + Admin.getTotalAdmins());
        // Display total registered admins
        System.out.println("Total registered admins: " + Admin.getTotalAdmins());
        
        // Customer related code
        
    }
}

