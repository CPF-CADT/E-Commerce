public class User {
    static  int userId;
    public  String name;
    public String email;
    private String password;
    public String address;
    public String phoneNumber;

    public User(int userId, String userName, String userEmail, String userPassword, String userAddress, String userPhoneNumber) {
        userId = userId + 1;
        this.name = userName;
        this.email = userEmail;
        this.password = userPassword;
        this.address = userAddress;
        this.phoneNumber = userPhoneNumber;
    }

    
}
