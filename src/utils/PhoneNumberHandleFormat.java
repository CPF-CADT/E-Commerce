package utils;

public class PhoneNumberHandleFormat extends IllegalArgumentException{
    public PhoneNumberHandleFormat() {
        super();
    }

    public PhoneNumberHandleFormat(String message) {
        super(message);
    }

    public static void validatePhoneNumber(String number) {
        if (!number.matches("^\\+?[0-9]+$")) {
            throw new PhoneNumberHandleFormat("Invalid phone number format. It should contain only digits and may start with a '+'.");
        }
    }
}
