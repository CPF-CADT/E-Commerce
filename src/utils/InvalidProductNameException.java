package utils;

public class InvalidProductNameException extends IllegalArgumentException {
    public InvalidProductNameException() {
        super();
    }
    public InvalidProductNameException(String message) {
        super(message);
    }

    public static void checkProductName(String productName) {
        // Regex: Ensures exactly two words with only letters
        if (!productName.matches("^[a-zA-Z]+ [a-zA-Z]+$")) {
            throw new InvalidProductNameException("Invalid product name! It must contain exactly two words with only letters.");
        }
    }
}
