package utils;

import java.util.Date;
import java.util.HashMap;

public class Admin extends User implements Authentication {
    public static int adminId;
    public String position;

    public static HashMap<Integer, Admin> adminsById = new HashMap<>();

    public Admin(String firstname, String lastname, String email, String password, String address, String phoneNumber, Date dateOfBirth, String position) {
        super(firstname, lastname, email, password, address, phoneNumber, dateOfBirth);
        adminId++;
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
        return adminId;
    }

    @Override
    public String toString() {
        return super.toString() + "Admin{" +
                "adminId=" + adminId +
                ", position='" + position + '\'' +
                '}';
    }

    @Override
    public Admin login(Object t) {
        Admin admin = (Admin)(t);
        for (Admin adm : adminsById.values()) {
            if (admin.equals(adm)) {
                return adm;
            }
        }
        return null;
    }

}
