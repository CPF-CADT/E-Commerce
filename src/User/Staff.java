package User;

import java.util.Date;

public class Staff extends User  {
    private static int counterId = 0;
    private String staffId = "S";
    // public int staffId;
    public String position;

    
    //for registration
    public Staff(String firstname, String lastname, String email, String password, String address, String phoneNumber, Date dateOfBirth, String position) {
        super(firstname, lastname, email, password, address, phoneNumber, dateOfBirth);
        this.position = position;
        staffId+= String.valueOf(++counterId);
        User.userList.put(staffId, this);
    }
    //for login
    public Staff(String email, String password) {
        super(email, password);
    }
    // for login with database
    public Staff(String userId, String firstname, String lastname, String address, String phoneNumber, String email, String password, String position) {
        super(firstname, lastname, email, password, address, phoneNumber, new Date());
        this.position = position;
        this.staffId = userId;
        User.userList.put(staffId, this);
    }

    // Admin views all users
    // public void viewAlladmin() {
    //     System.out.println(super.toString());
    //     System.out.println("ID      : " + staffId);
    //     System.out.println("position: " + position);
    //     System.out.println("====================================");
    // }

    // Get total registered admins
    public static int getTotalAdmins() {
        return counterId;
    }

    @Override
    public String toString() {
        return "Staff{" + "staffId='" + staffId + super.toString() +  '\'' +
                " position='" + position + '\'' +
                '}';
    }
    // @Override
    // public boolean equals(Object obj) {
    //   Staff staff = (Staff) obj;
    //     return (this.email.equals(staff.email) && this.password.equals(user.password));
    // }

   
}
