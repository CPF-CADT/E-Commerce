package Testing;

import Exception.InvalidTextException;
import java.util.Scanner;




public class TestCode {
    public static void main(String[] args) throws InvalidTextException {
        Scanner scanner = new Scanner(System.in);
        InvalidTextException q = new InvalidTextException("Invalid Name");
        try {
            System.out.print("Enter First Name: ");
            String firstName = scanner.nextLine();
            System.out.print("Enter last Name: ");
            String lastname = scanner.nextLine();
            InvalidTextException l = new InvalidTextException(firstName, "[a-zA-Z]+");
            InvalidTextException l1 = new InvalidTextException(lastname, "[a-zA-Z]+");

        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

    }
}
