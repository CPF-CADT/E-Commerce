package utils;

import java.util.Date;

public class Customer extends User  {
    private static int cusCounter = 0;
    private String cusId = "C";
    
   //for login
    public Customer(String email, String password) {
        super(email, password);
    }
    //for registration
    public Customer(String firstname,String lastname, String email, String password, String address, String phoneNumber, Date dateOfBirth) {
        super(firstname,lastname, email, password, address, phoneNumber, dateOfBirth);
        cusId += String.valueOf(++cusCounter);
        User.userList.put(cusId, this);
        
    }
    @Override
    public String toString() {
            return super.toString();
    }
    @Override
    public void displayUserInfo(){
        super.displayUserInfo();
        System.out.println("ID      : " + cusId);
        System.out.println("====================================\n");
    }

    public void viewAlladmin() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // TO DO
    public static int getTotalAdmins() {
        return cusCounter;
    }
    
}
