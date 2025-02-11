package utils;

import java.util.Date;
import java.util.HashMap;

public class Admin implements Account {
    private static int counterId = 0;
    public int adminId;
    public String name;
    public String email;
    private String password;
    public String address;
    public String phoneNumber;
    Date dateOfBirth;

    public static HashMap<Integer, Admin> adminsById = new HashMap<>();
    public static HashMap<String, Admin> adminsByEmail = new HashMap<>();
    public static HashMap<Integer, User> usersById = new HashMap<>();
    public static HashMap<String, User> usersByEmail = new HashMap<>();

    public Admin(String name, String email, String password, String address, String phoneNumber,Date dateOfBirth) {
        this.adminId = ++counterId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.phoneNumber = phoneNumber;

        adminsById.put(this.adminId, this);
        adminsByEmail.put(this.email, this);
    }

    // Admin registration
    public static Admin register(String name, String email, String password,  String address, String phoneNumber,Date dateOfBirth) {
        if (adminsByEmail.containsKey(email)) {
            System.out.println("Admin with this email already exists.");
            return null;
        }
        Admin newAdmin = new Admin(name, email, password, address, phoneNumber, dateOfBirth);
        System.out.println("Admin registration successful! Admin ID: " + newAdmin.adminId);
        return newAdmin;
    }

    // Admin login
    @Override
    public boolean login(String email, String password) {
        if (this.email.equals(email) && this.password.equals(password)) {
            System.out.println("User login successful! Welcome, " + this.name);
            return true;
        }
        System.out.println("Invalid email or password.");
        return false;
    }

    // Admin creates a new user
    public User createUser(String name, String email, String password, String address, String phoneNumber, Date dateOfBirth) {
        if (usersByEmail.containsKey(email)) {
            System.out.println("User with this email already exists.");
            return null;
        }
        User newUser = new User(name, email, password, address, phoneNumber, dateOfBirth);
        usersById.put(newUser.userId, newUser);
        usersByEmail.put(newUser.email, newUser);
        System.out.println("User created successfully by Admin: " + this.name);
        return newUser;
    }

    // Admin deletes a user
    public void deleteUser(int userId) {
        User user = usersById.get(userId);
        if (user != null) {
            usersById.remove(userId);
            usersByEmail.remove(user.email);
            System.out.println("User " + user.name + " has been deleted by Admin: " + this.name);
        } else {
            System.out.println("User not found.");
        }
    }

    // Admin views all users
    public void viewAllUsers() {
        if (usersById.isEmpty()) {
            System.out.println("No users found.");
            return;
        }
        System.out.println("List of Users:");
        for (User user : usersById.values()) {
            System.out.println(user);
        }
    }

    // Change password
    @Override
    public void setPassword(String newPassword) {
        this.password = newPassword;
        System.out.println("Admin password has been updated.");
    }

    // Get total registered admins
    public static int getTotalAdmins() {
        return counterId;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "adminId=" + adminId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }

    public static void main(String[] args) {
        // Registering admins
        Admin admin1 = register("Admin One", "admin1@example.com", "adminpass1", "123 Admin St", "1234567890", new Date());

        if (admin1 != null) {
            // Creating users
            User user1 = admin1.createUser("John Doe", "john@example.com", "password123", "123 Main St", "1234567890", new Date());
            User user2 = admin1.createUser("Alice Smith", "alice@example.com", "alicepass", "456 Elm St", "0987654321", new Date());

            // Viewing all users
            admin1.viewAllUsers();

            // Deleting a user
            if (user1 != null) {
                admin1.deleteUser(user1.userId);
            }

            // Viewing all users after deletion
            admin1.viewAllUsers();
        }

        // Verify total admins
        System.out.println("Total registered admins: " + getTotalAdmins());
    }
}
