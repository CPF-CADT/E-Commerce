package Testing;

import Database.MySQLConnection;
import User.Staff;
import User.User;

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
     public User loadData(String userID,User user){
        String query = " ";
        if(user instanceof Admin){
            if(userID.contains("S")){
                query = "SELECT u.id, u.first_name, u.last_name, u.dob, u.address, u.email, u.phone_number, u.password, s.status FROM User AS u JOIN Students AS s ON u.id = s.user_id WHERE u.id = '"+userID+"';"; //NOT READY 
            }else if(userID.contains("T")){
                query = "SELECT u.id, u.first_name, u.last_name, u.dob, u.address, u.email, u.phone_number, u.password, t.role_major,t.status FROM User AS u JOIN Teachers AS t ON u.id = t.user_id WHERE u.id = '"+userID+"';"; //NOT READY 
            }
            return getUsr(query);
        }
        return null;
    }
}
