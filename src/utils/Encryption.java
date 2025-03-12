package utils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryption {
    
    public static String hashPassword(String password) {
        try {
            // Create MessageDigest instance for SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            
            // Perform hashing on the password (convert to bytes and hash)
            byte[] hashBytes = digest.digest(password.getBytes());
            
            // Convert the byte array to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b)); // Convert byte to hex
            }
            
            // Return the hashed password
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Method to verify if the password matches the stored hash
    public static boolean verifyPassword(String storedHashedPassword,String inputPassword) {
        // Hash the input password
        String hashedInputPassword = hashPassword(inputPassword);
        
        // Compare the hashed input with the stored hashed password
        return hashedInputPassword.equals(storedHashedPassword);
    }
}
