package utils;

import java.util.Date;
import java.util.HashMap;
public class Customer extends User implements Authentication {
    static int counterId = 0;
    public static HashMap<Integer, Customer> customersById = new HashMap<>();
   //for login
    public Customer(String email, String password) {
        super(email, password);
    }
    //for registration
    public Customer(String firstname,String lastname, String email, String password, String address, String phoneNumber, Date dateOfBirth) {
        super(firstname,lastname, email, password, address, phoneNumber, dateOfBirth);
        customersById.put(++counterId, this);
        
    }
    @Override
    public String toString() {
            return super.toString();
    }
    @Override
    public void displayUserInfo(){
        super.displayUserInfo();
        System.out.println("====================================\n");
    }

    @Override
    // login field use interface
    public Customer login(User t) {
        Customer customer = (Customer)(t);  // Cast Object to Customer
        for(Customer foundCustomer : customersById.values()) {
            if (customer.equals(foundCustomer)) {
                return foundCustomer;  // Return the matched customer
            }
        }
        return null;
    }
    
}

