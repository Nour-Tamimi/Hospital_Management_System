
package org.example.hospital_management_system.config;

import java.sql.*;

public class TestConnection {

    public static void main(String[] args) {

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            if (connection == null) {
                System.out.println("Connection failed!");
                return;
            }

            System.out.println("Connected successfully!");

            try (Statement statement = connection.createStatement();
                 ResultSet result = statement.executeQuery(
                         "SELECT * FROM Department")) {

                while (result.next()) {
                    System.out.println(
                            result.getString("DepartmentName"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
