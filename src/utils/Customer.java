package utils;

import java.util.Date;
import java.util.HashMap;
class Customer extends User implements Authentication {
    private static int counterId = 0;
    private  int customerId;
    public static HashMap<Integer, Customer> customersById = new HashMap<>();
    public static HashMap<String, Customer> customersByEmail = new HashMap<>();
   //for login
    public Customer(String email, String password) {
        super(email, password);
    }
    //for registration
    public Customer(String firstname,String lastname, String email, String password, String address, String phoneNumber, Date dateOfBirth) {
        super(firstname,lastname, email, password, address, phoneNumber, dateOfBirth);
        this.customerId = ++counterId;
        customersById.put(this.customerId, this);
        
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
    public Customer login(Object t) {
        Customer customer = (Customer)(t);  // Cast Object to Customer
       for(Customer foundCustomer : customersById.values()) {
        if (foundCustomer != null && customer.equals(foundCustomer)) {
            return foundCustomer;  // Return the matched customer
        }
    }
        return null;
        
    
    }
    
}

