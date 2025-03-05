package Exception;

public class InvalidTextException extends RuntimeException {
   
    public InvalidTextException(String message) {
        super(message);
    }
    public InvalidTextException(String text,String format) throws InvalidTextException {
            if (!text.matches(format)){
                throw new InvalidTextException("Wrong format: " + format);
            }
    }
    
    
}
