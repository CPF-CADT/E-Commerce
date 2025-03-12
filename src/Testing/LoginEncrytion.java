package Testing;

import Database.MySQLConnection;
import User.Staff;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import utils.Encryption;


public class LoginEncrytion {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        
        System.out.print("Email address : "); 
        String email = input.next();
        
        System.out.print("Password      : ");
        String password = input.next();
        
        // Hash the password before checking in the database
        String hashedPassword = Encryption.hashPassword(password);
        
        // Corrected query with placeholders
        String query = "SELECT * FROM User join Staff WHERE email = ? " + "AND password = ?";
        
        try {
            PreparedStatement preparedStatement = MySQLConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, hashedPassword);
            
            ResultSet result = preparedStatement.executeQuery();
            
            if (result.next()) {
                // Retrieve staff details
                String userId = result.getString("staffId");
                String userEmail = result.getString("email");
                String phone = result.getString("phoneNumber");
                String userPassword = result.getString("password");
                String firstName = result.getString("firstname");
                String lastName = result.getString("lastname");
                String address = result.getString("city");
                String position = result.getString("position"); 
                

                // Create a Staff object
                Staff staff = new Staff(userId, firstName, lastName, address, phone, userEmail, userPassword, position);
                System.out.println("Hello " + staff);
                System.out.println("Login Success");
            } else {
                System.out.println("Invalid email or password.");
            }
        } catch (SQLException s) {
            System.out.println("Database error: " + s.getMessage());
        } finally {
            input.close();
        }
    }
}
