
package org.example.hospital_management_system.service;

import org.example.hospital_management_system.model.User;
import org.example.hospital_management_system.util.Session;

import java.sql.SQLException;

public class TestAuthService {

    public static void main(String[] args) {

        AuthService auth = new AuthService();

        try {
            User user = auth.login("admin", "TestAdmin123!");

            if (user != null) {
                Session.start(user);

                System.out.println("Logged in: " + Session.isLoggedIn());
                System.out.println("Role: " + Session.getCurrentRole());

                Session.clear();

                System.out.println("After logout: " + Session.isLoggedIn());
            }

            User disabledUser = auth.login("admin", "TestAdmin123!");

            System.out.println("Inactive account rejected: "
                    + (disabledUser == null));

            // Test 1: Correct credentials
            User user1 = auth.login("admin", "TestAdmin123!");

            System.out.println("Test 1 - Correct password: "
                    + (user1 != null));

            // Test 2: Wrong password
            User user2 = auth.login("admin", "WrongPassword");

            System.out.println("Test 2 - Wrong password rejected: "
                    + (user2 == null));

            // Test 3: Unknown username
            User user3 = auth.login("unknown_user", "TestAdmin123!");

            System.out.println("Test 3 - Unknown user rejected: "
                    + (user3 == null));

        } catch (SQLException e) {
            System.out.println("Database error during testing");
            e.printStackTrace();
        }
    }
}
