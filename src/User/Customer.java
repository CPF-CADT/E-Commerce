package User;

import java.util.Date;

public class Customer extends User  {
    private static int cusCounter = 0;
    private String cusId = "C";
    private boolean isActive;
    
   //for login
    public Customer(String email, String password) {
        super(email, password);
    }
    //for registration
    public Customer(String firstname,String lastname, String email, String password, String address, String phoneNumber, Date dateOfBirth) {
        super(firstname,lastname, email, password, address, phoneNumber,new Date());
        cusId += String.valueOf(++cusCounter);
        User.userList.put(cusId, this); 
        this.isActive = true;
    }
    // for login with database
    public Customer(String userId, String firstname, String lastname, String address, String phoneNumber, String email, String password) {
        super(firstname, lastname, email, password, address, phoneNumber,new Date());
        this.cusId = userId;
        User.userList.put(cusId, this);
        this.isActive = true;
    }
    @Override
    public String toString() {
            return "Customer{" + super.toString() +
                    ", isActive='" + isActive + '\'' +
                    '}';
    }
   

    // TO DO
    public static int getTotalAdmins() {
        return cusCounter;
    }
    // public boolean equals(Object obj) {
    //     Customer customer = (Customer) obj;
    //     return super.getEmail().equals(customer.getEmail()) && super.getPassword().equals(customer.getPassword());
    //   }
}
