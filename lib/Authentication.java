import java.util.Date;

public interface Authentication {
    User register(String name, String email, String password, String address, String phoneNumber, Date Dateofbirth);
    User login(String email, String password);
}