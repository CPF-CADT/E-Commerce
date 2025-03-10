import Database.MySQLConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Register {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Registration Menu =====");
            System.out.println("1. Register Staff");
            System.out.println("2. Register Customer");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = input.nextInt();
            input.nextLine(); // Consume the newline

            try {
                switch (choice) {
                    case 1:
                        registerStaff(input);
                        break;
                    case 2:
                        registerCustomer(input);
                        break;
                    case 3:
                        System.out.println("Exiting program...");
                        input.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // Check if user exists
    private static boolean userExists(String userId) {
        String checkQuery = "SELECT COUNT(*) FROM User WHERE userId = ?;";
        try (PreparedStatement stmt = MySQLConnection.getConnection().prepareStatement(checkQuery)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return false;
    }

    private static String registerUser(Scanner input, char type) throws SQLException {
        int count = 0;

        // Get the number of existing users
        String countQuery = "SELECT COUNT(*) AS total FROM User;";
        try (PreparedStatement stmt = MySQLConnection.getConnection().prepareStatement(countQuery);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                count = rs.getInt("total") + 1;
            }
        }

        // Generate userId (e.g., "S001", "C002")
        String userId = type + String.format("%03d", count);
        System.out.println("Generated User ID: " + userId);

        // Default values for testing (replace with user input if needed)
        String firstName = "KK";
        String lastName = "Dony";
        String street = "1234 Main Street";
        String city = "Phnom Penh";
        String state = "Phnom Penh";
        String postalCode = "12000";
        String country = "Cambodia";
        String phoneNumber = "+85512345678";
        String email = "dony.kk@example.com";
        String password = "Donypass1234";

        String queryUser = "INSERT INTO User (userId, firstname, lastname, email, password, street, city, state, postalCode, country, phoneNumber) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try (PreparedStatement stmt = MySQLConnection.getConnection().prepareStatement(queryUser)) {
            stmt.setString(1, userId);
            stmt.setString(2, firstName);
            stmt.setString(3, lastName);
            stmt.setString(4, email);
            stmt.setString(5, password);
            stmt.setString(6, street);
            stmt.setString(7, city);
            stmt.setString(8, state);
            stmt.setString(9, postalCode);
            stmt.setString(10, country);
            stmt.setString(11, phoneNumber);

            int rowsInserted = stmt.executeUpdate();
            System.out.println(rowsInserted > 0 ? "User registration successful!" : "User registration failed.");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }

        return userId;
    }

    // Register Staff
    private static void registerStaff(Scanner input) throws SQLException {
        System.out.println("\n--- Staff Registration ---");
        String staffId = registerUser(input, 'S');

        System.out.print("Position: ");
        String position = input.nextLine();

        String queryStaff = "INSERT INTO Staff (staffId, position) VALUES ('"+staffId+"', '"+position+"');";
        MySQLConnection.executeUpdate(queryStaff);
        try (PreparedStatement stmt = MySQLConnection.getConnection().prepareStatement(queryStaff)) {
            stmt.setString(1, staffId);
            stmt.setString(2, position);

            int rowsInserted = stmt.executeUpdate();
            System.out.println(rowsInserted > 0 ? "Staff registration successful!" : "Staff registration failed.");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    // Register Customer
    private static void registerCustomer(Scanner input) throws SQLException {
        System.out.println("\n--- Customer Registration ---");
        String customerId = registerUser(input, 'C');

        String queryCustomer = "INSERT INTO Customer (customerId) VALUES (?);";

        try (PreparedStatement stmt = MySQLConnection.getConnection().prepareStatement(queryCustomer)) {
            stmt.setString(1, customerId);

            int rowsInserted = stmt.executeUpdate();
            System.out.println(rowsInserted > 0 ? "Customer registration successful!" : "Customer registration failed.");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}
