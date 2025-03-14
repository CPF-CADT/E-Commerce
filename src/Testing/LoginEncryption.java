package Testing;

import Database.MySQLConnection;
import User.Staff;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import utils.Encryption;

public class LoginEncryption {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        
        System.out.print("Email address : "); 
        String email = input.next();
        
        System.out.print("Password      : ");
        String password = input.next();
        
        // Hash the password before checking in the database
        String hashedPassword = Encryption.hashPassword(password);
        
        // Corrected query with placeholders
        String query = "SELECT * FROM User join Staff WHERE email = ? ";
        
        try {
            PreparedStatement preparedStatement = MySQLConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, email);
            
            ResultSet result = preparedStatement.executeQuery();
            
            if (result.next()) {
                // Retrieve staff details
                String userId = result.getString("staffId");
                String userEmail = result.getString("email");
                String phone = result.getString("phoneNumber");
                String storedPassword = result.getString("password"); // The stored hashed password
                String firstName = result.getString("firstname");
                String lastName = result.getString("lastname");
                String address = result.getString("city");
                String position = result.getString("position"); 
                
                // Use the Encryption.verifyPassword method to compare the hashes
                if (Encryption.verifyPassword(storedPassword, hashedPassword)) {
                    // Create a Staff object
                    Staff staff = new Staff(userId, firstName, lastName, address, phone, userEmail, storedPassword, position);
                    System.out.println("Hello " + staff);
                    System.out.println("Login Success");
                } else {
                    System.out.println("Invalid email or password.");
                }
            } else {
                System.out.println("No user found with that email.");
            }
        } catch (SQLException s) {
            System.out.println("Database error: " + s.getMessage());
        } finally {
            input.close();
        }
    }
}
