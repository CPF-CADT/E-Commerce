package Testing;

import Database.MySQLConnection;
import User.Staff;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Login {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        // System.out.print("Email address: ");
        // String email = input.next();
        String email = "john.doe@example.com";

        // System.out.print("Password: ");
        // String password = input.next();
        String password = "password123";

        // Query to fetch user details by email
        String query = "SELECT * FROM User AS u JOIN Staff AS s ON s.staffId = u.userId WHERE email = ? ;";
        try {
            PreparedStatement preparedStatement = MySQLConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                String userId = result.getString("userId");
                String userEmail = result.getString("email");
                String phone = result.getString("phoneNumber");
                String storedHashedPassword = result.getString("password"); // Hashed password from DB
                String firstName = result.getString("firstname");
                String lastName = result.getString("lastname");
                String address = result.getString("city");
                String position = result.getString("position");

                // Verify the hashed password
                if (storedHashedPassword.equals(password)) {
                    Staff staff = new Staff(userId, firstName, lastName, address, phone, userEmail, storedHashedPassword, position);
                    System.out.println("Hello " + staff);
                    System.out.println("Login Success");
                } else {
                    System.out.println("Invalid email or password.");
                }
                
                // if (Encryption.verifyPassword(storedHashedPassword, password)) {
                //     Staff staff = new Staff(userId, firstName, lastName, address, phone, userEmail, storedHashedPassword, position);
                //     System.out.println("Hello " + staff);
                //     System.out.println("Login Success");
                // } else {
                //     System.out.println("Invalid email or password.");
                // }
            } else {
                System.out.println("Invalid email or password.");
            }
        } catch (SQLException s) {
            System.out.println("Database error: " + s.getMessage());
        } finally {
            input.close(); // Close the scanner
        }
    }
}
