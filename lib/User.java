import java.util.HashMap;
import java.util.Map;

public class User {
    private static int counterId = 1;
    public int userId;
    public String name;
    public String email;
    private String password;
    public String address;
    public String phoneNumber;

   
    public static Map<Integer, User> usersById = new HashMap<>();
    public static Map<String, User> usersByEmail = new HashMap<>();

    public User(String userName, String userEmail, String userPassword, String userAddress, String userPhoneNumber) {
        this.userId = counterId++; 
        this.name = userName;
        this.email = userEmail;
        this.password = userPassword;
        this.address = userAddress;
        this.phoneNumber = userPhoneNumber;

       
        usersById.put(this.userId, this);
        usersByEmail.put(this.email, this);
    }

    public void getPassword(String password) {
        if (this.password.equals(password)) {
            System.out.println("Password is correct");
        } else {
            System.out.println("Password not set");
        }
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
        System.out.println("Password has been updated.");
    }

    public static User getUserByEmail(String email) {
        return usersByEmail.get(email);
    }

    public static int getTotalUsers() {
        return counterId - 1;  
    }

    public static void displayUsers() {
        for (User user : usersById.values()) {
            System.out.println("ID: " + user.userId + ", Name: " + user.name + ", Email: " + user.email);
        }
    }
}
