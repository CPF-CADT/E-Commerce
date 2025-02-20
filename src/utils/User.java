package utils;
import java.util.Date;
import java.util.HashMap;

public abstract class User {
    private static int counterId = 0;
    public int userId;
    public String firstname;
    public String lastname;
    public String email;
    private String password;
    protected String address;
    protected String phoneNumber;
    public Date Dateofbirth;
    public static HashMap<Integer, User> userList = new HashMap<>();

    public User(String firstname, String lastname, String email, String password, String address, String phoneNumber, Date Dateofbirth) {
        this.userId = ++counterId; 
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.Dateofbirth = Dateofbirth;
        userList.put(userId, this);

    }
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
    
   
    public String getPassword(String curPassword) {
        if (curPassword.equals(this.password)) {
            return password;
        } else {
            return "Password Invalid";
        }
    }
    public void setPassword(String newPassword, String curPassword) {
        if (curPassword.equals(this.password)) {
            this.password = newPassword;
            } else {
                System.out.println("Password Invalid");
            }
        }
        public void displayUserInfo() {
            System.out.println("\n====================================");
            System.out.println("              USER DETAILS      ");
            System.out.println("====================================");
            System.out.println("User ID      : " + userId);
            System.out.println("First Name   : " + firstname);
            System.out.println("Last Name    : " + lastname);
            System.out.println("Address      : " + address);
            System.out.println("Phone Number : " + phoneNumber);
            System.out.println("Email        : " + email);
            System.out.println("Date of Birth: " + Dateofbirth);
        }
    
    public static int getTotalUsers() {
        return counterId;  
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
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
        return email.hashCode();
    }
   
    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setEmail(String email, String conPassword) {
        if (conPassword.equals(this.password)) {
            this.email = email;
        } else {
            System.out.println("Password Invalid");
        }
    }

    public void setPhoneNumber(String phoneNumber, String conPassword) {
        if (conPassword.equals(this.password)) {
            this.phoneNumber = phoneNumber;
        } else {
            System.out.println("Password Invalid");
        }
    }

    
    public static User login(User t) {
        for (User adm : User.userList.values()) {
            if (t.equals(adm)) {
                return adm;
            }
        }
        return null;

    }
    
    

 }

