package utils;
import java.util.HashMap;

public class User {
    private static int counterId = 0;
    public int userId;
    public String name;
    public String email;
    private String password;
    public String address;
    public String phoneNumber;
    public Date Dateofbirth;

    public static HashMap<Integer, User> usersById = new HashMap<>();
    public static HashMap<String, User> usersByEmail = new HashMap<>();

    public User(String name, String email, String password, String address, String phoneNumber, Date Dateofbirth) {
        this.userId = ++counterId; 
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.Dateofbirth = Dateofbirth;
        
        usersById.put(this.userId, this);
        usersByEmail.put(this.email, this);
    }
    // User register
    public static User register(String name, String email, String password, String address, String phoneNumber, Date Dateofbirth) {
        if (usersByEmail.containsKey(email)) {
            System.out.println("Email already registered.");
            return null;
        }
        User newUser = new User(name, email, password, address, phoneNumber, Dateofbirth);
        System.out.println("Registration successful! User ID: " + newUser.userId);
        return newUser;
    }
    //User login
    public static User login(String email, String password) {
        User user = usersByEmail.get(email);
        if (user != null && user.password.equals(password)) {
            System.out.println("Login successful! Welcome, " + user.name);
            return user;
        } else {
            System.out.println("Invalid email or password.");
            return null;
        }
    }
    
    public void setPassword(String newPassword) {
        this.password = newPassword;
        System.out.println("Password has been updated.");
    }
    
    public static int getTotalUsers() {
        return counterId;  
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", Dateofbirth=" + Dateofbirth +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return this.email.equals(user.email) && this.password.equals(user.password);
    }

    @Override
    public int hashCode() {
        return email.hashCode() + password.hashCode();
    }
    
    public static User findUser(User user) {
        for (User u : usersById.values()) {
            if (user.equals(u)) {
                return u;
            }
        }
        return null;
    }
    public static void main(String[] args) {
        User user1 = register("John Doe", "john@example.com", "password123", "123 Main St", "1234567890", new Date());
        User user2 = register("Alice Smith", "alice@example.com", "alicepass", "456 Elm St", "0987654321", new Date());
        User user3 = register("Bob Johnson", "bob@example.com", "bobpass", "789 Oak St", "5678901234", new Date());
        
        // Attempt to login with correct credentials
        login("john@example.com", "password123");
        login("alice@example.com", "alicepass");
        login("bob@example.com", "bobpass");
        
        // Attempt to login with incorrect credentials
        login("john@example.com", "wrongpassword");
        login("alice@example.com", "incorrect");
        
        // Change password
        if (user1 != null) {
            user1.setPassword("newpassword456");
        }
        
        // Verify total users
        System.out.println("Total registered users: " + getTotalUsers());
        
        // Print user details
        if (user1 != null) {
            System.out.println(user1.toString());
        }
        if (user2 != null) {
            System.out.println(user2.toString());
        }
        if (user3 != null) {
            System.out.println(user3.toString());
        }
    }
}

