
import Database.MySQLConnection;
import User.Staff;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
;

public class Login {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        // System.out.print("Email address : ");
        // String email = input.next();
        // System.out.print("Password      : ");
        // String password = input.next();

        // Use prepared statements to prevent SQL injection
        String query = "SELECT * FROM Staff";
        try {
            // PreparedStatement preparedStatement = MySQLConnection.getConnection().prepareStatement(query);
            // preparedStatement.setString(1, email);
            // preparedStatement.setString(2, password);
            // ResultSet result = preparedStatement.executeQuery();
            ResultSet result = MySQLConnection.executeQuery(query);
            if (result != null) {
                if (result.next()) {
                    String userId = result.getString("staffId");
                    String userEmail = result.getString("email");
                    String phone = result.getString("phoneNumber");
                    String userPassword = result.getString("password");
                    String firstName = result.getString("firstname");
                    String lastName = result.getString("lastname");
                    String address = result.getString("city");
                    String position = result.getString("position"); // Retrieve the position

                    // Create a Staff object
                    Staff staff = new Staff(userId, firstName, lastName, address, phone, userEmail, userPassword, position);
                    System.out.println("Hello " + staff);
                    System.out.println("Login Success");
                } else {
                    System.out.println("Invalid email or password.");
                }
            } else {
                System.out.println("Fail");
            }
        } catch (SQLException s) {
            System.out.println("Database error: " + s.getMessage());
        } finally {
            input.close(); // Close the scanner
        }
    }
}