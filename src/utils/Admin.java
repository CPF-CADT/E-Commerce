package utils;

import java.util.Date;
import java.util.HashMap;

public class Admin extends User implements Authentication {
    private static int counterId = 0;
    public int adminId;
    public String position;

    public static HashMap<Integer, Admin> adminsById = new HashMap<>();

    public Admin(String firstname, String lastname, String email, String password, String address, String phoneNumber, Date dateOfBirth, String position) {
        super(firstname, lastname, email, password, address, phoneNumber, dateOfBirth);
        this.adminId = ++counterId;
        this.position = position;

        adminsById.put(this.adminId, this);
    }
    public Admin(String email, String password) {
        super(email, password);
    }

    // Admin views all users
    public void viewAlladmin() {
        super.displayUserInfo();
        System.out.println(super.toString());
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
                "adminId=" + adminId +
                ", position='" + position + '\'' +
                '}';
    }

    @Override
    public Object login(Object t) {
        for (Admin adm : adminsById.values()) {
            if (t.equals(adm)) {
                return adm;
            }
        }
        return null;
    }
}
