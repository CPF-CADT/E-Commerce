package User;
import Exception.CastToUserHandleException;
import java.util.Date;
import java.util.HashMap;

public abstract class User implements Authentication {
    private static int counter = 0;
    // private int userId;
    public String firstname;
    public String lastname;
    public String email;
    private String password;
    protected String address;
    protected String phoneNumber;
    public Date Dateofbirth;
    public static HashMap<String, User> userList = new HashMap<>();

    public User(String firstname, String lastname, String email, String password, String address, String phoneNumber, Date Dateofbirth) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.Dateofbirth = Dateofbirth;
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
        // public void displayUserInfo() {
        //     System.out.println("\n====================================");
        //     System.out.println("              USER DETAILS      ");
        //     System.out.println("====================================");
        //     System.out.println("First Name   : " + firstname);
        //     System.out.println("Last Name    : " + lastname);
        //     System.out.println("Address      : " + address);
        //     System.out.println("Phone Number : " + phoneNumber);
        //     System.out.println("Email        : " + email);
        //     System.out.println("Date of Birth: " + Dateofbirth);
        // }
    
    public static int getTotalUsers() {
        return counter;  
    }

    @Override
    public String toString() {
        return 
                " firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", Dateofbirth=" + Dateofbirth;
    }
@Override
    public boolean equals(Object obj) throws ClassCastException{
        try{
            CastToUserHandleException c = new CastToUserHandleException(obj);
            User log = (User) obj;
            if (this.email.equals(log.getEmail())) {
                if ((log.checkingPassword(this.password))) {
                    return true;
                }
            }
        }catch(ClassCastException c){
            System.out.println(c.getMessage());
        }
        return false;
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
    public boolean checkingPassword(String password){
        if(this.password.equals(password)){
            return true;
        }else{
            return false;
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

