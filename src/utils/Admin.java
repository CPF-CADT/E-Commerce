package utils;
public class Admin {
    static int adminId;
    public  String name;
    public String email;
    private String adminPassword;

 
    public Admin(String adminName, String adminEmail, String password) {
        adminId = adminIdCounter+1;
        this.name = adminName;
        this.email = adminEmail;
        Admin.adminPassword = password;
    }

    public static boolean isValidPassword(String password) {
        return Admin.adminPassword.equals(password);
    }

    public String getPassword() {
        return adminPassword;
    }

    public void setPassword(String password) {
        Admin.adminPassword = password;
    }
    
}
