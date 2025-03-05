package Exception;

public class PhoneNumberHandleFormat extends IllegalArgumentException{
    

    public PhoneNumberHandleFormat(String message) {
        super(message);
    }

    public PhoneNumberHandleFormat(String number,String format) throws PhoneNumberHandleFormat {
        if (!number.matches(format)) {
            throw new PhoneNumberHandleFormat("Invalid phone number format. It should contain only digits and may start with a '+'." + format);
        }
    }
}
//"^\\+?[0-9]+$