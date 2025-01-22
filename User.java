public class User {
    private long userId;
    private String name;
    private String email;
    private String password;
    private String address;
    private String phoneNumber;

    public User(long userId, String userName, String userEmail, String userPassword, String userAddress, String userPhoneNumber) {
        this.userId = userId;
        this.name = userName;
        this.email = userEmail;
        this.password = userPassword;
        this.address = userAddress;
        this.phoneNumber = userPhoneNumber;
    }

   
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String userName) {
        this.name = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String userEmail) {
        this.email = userEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String userPassword) {
        this.password = userPassword;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String userAddress) {
        this.address = userAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String userPhoneNumber) {
        this.phoneNumber = userPhoneNumber;
    }
}
