package utils;

public interface Account {
    boolean login(String email, String password);
    void register(String name, String email, String password, String address, String phoneNumber, String dateOfBirth);
    void setPassword(String newPassword);
    
}
