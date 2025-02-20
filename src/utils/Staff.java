package utils;

import java.util.Date;

public class Staff extends User  {
    private static int counterId = 0;
    private String staffId = "S";
    // public int staffId;
    public String position;

    

    public Staff(String firstname, String lastname, String email, String password, String address, String phoneNumber, Date dateOfBirth, String position) {
        super(firstname, lastname, email, password, address, phoneNumber, dateOfBirth);
        this.position = position;
        staffId+= String.valueOf(++counterId);
        User.userList.put(staffId, this);
    }
    public Staff(String email, String password) {
        super(email, password);
    }

    // Admin views all users
    public void viewAlladmin() {
        super.displayUserInfo();
        System.out.println(super.toString());
        System.out.println("ID      : " + staffId);
        System.out.println("position: " + position);
        System.out.println("====================================");
    }

    // Get total registered admins
    public static int getTotalAdmins() {
        return counterId;
    }

    @Override
    public String toString() {
        return super.toString() + "Admin{" +
                ", position='" + position + '\'' +
                '}';
    }

   
}
