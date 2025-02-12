class Admin {
    private static int adminIdCounter = 0;
    private int adminId;
    public String name;
    public String email;
    private static String adminPassword;

 
    public Admin(String adminName, String adminEmail, String password) {
        this.adminId = adminIdCounter+1;
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
