public class Admin {
    private long adminId;
    private String name;
    private String email;
    private String password;

    // Constructor
    public Admin(long id, String adminName, String adminEmail, String adminPassword) {
        this.adminId = id;
        this.name = adminName;
        this.email = adminEmail;
        this.password = adminPassword;
    }

    // Getters and Setters
    public long getAdminId() {
        return adminId;
    }

    public void setAdminId(long adminId) {
        this.adminId = adminId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
