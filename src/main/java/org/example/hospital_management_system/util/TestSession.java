
package org.example.hospital_management_system.util;

import org.example.hospital_management_system.dao.UserDAO;
import org.example.hospital_management_system.model.User;
import java.sql.SQLException;

public class TestSession {

    public static void main(String[] args) {

        UserDAO dao = new UserDAO();

        try {
            User doctor = dao.findByUsername("doctor1");

            Session.start(doctor);

            System.out.println("Logged in: " + Session.isLoggedIn());
            System.out.println("Role: " + Session.getCurrentRole());
            System.out.println("Doctor ID: " + Session.getDoctorId());
            System.out.println("User ID: " + Session.getCurrentUserId());

            Session.clear();

            System.out.println("After logout: " + Session.isLoggedIn());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
