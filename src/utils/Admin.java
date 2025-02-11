package utils;
public class Admin {
    static int adminId;
    public  String name;
    public String email;
    private String password;

    // Constructor
    public Admin(int adminId, String adminName, String adminEmail, String adminPassword) {
        adminId = adminId +1;
        this.name = adminName;
        this.email = adminEmail;
        this.password = adminPassword;
        
    }

}
