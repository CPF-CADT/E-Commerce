package utils;

import java.util.Date;
import java.util.HashMap;
class Customer extends User {
    private static int counterId = 0;
    public static HashMap<Integer, Customer> customersById = new HashMap<>();
    public static HashMap<String, Customer> customersByEmail = new HashMap<>();
    public Customer(String email, String password) {
        super(email, password);
    }
    public Customer(String firstname,String lastname, String email, String password, String address, String phoneNumber, Date dateOfBirth) {
        super(firstname,lastname, email, password, address, phoneNumber, dateOfBirth);
        this.userId = ++counterId;
        customersById.put(userId, this);
        
    }
    public static Customer login(Customer t) {
        for (Customer customer : customersById.values()) {
            if (t.equals(customer)) {
                return customer;
            }
        }
        return null;
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
}

