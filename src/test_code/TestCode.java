package test_code;
import java.util.Scanner;
import utils.InvalidTextException;




public class TestCode {
    public static void main(String[] args) throws InvalidTextException {
        Scanner scanner = new Scanner(System.in);
        InvalidTextException q = new InvalidTextException("Invalid Name");
        try {
            System.out.println("Enter First Name: ");
            String firstName = scanner.nextLine();
            System.out.println("Enter last Name: ");
            String lastname = scanner.nextLine();
            InvalidTextException.check(firstName);
            InvalidTextException.check(lastname);

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

    }
}
