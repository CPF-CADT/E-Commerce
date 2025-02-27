package utils;

public class InvalidTextException extends RuntimeException {
    public InvalidTextException() {
        super();
    }
    public InvalidTextException(String message) {
        super(message);
    }
    public static void check(String text)  {
        if (!text.matches("[a-zA-Z]+")) {
            throw new InvalidTextException("Invalid text! It must contain only alphabet.");
        }
    }
    
}
