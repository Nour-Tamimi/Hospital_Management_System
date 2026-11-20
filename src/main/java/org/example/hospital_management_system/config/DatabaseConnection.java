
package org.example.hospital_management_system.config;

import java.sql.*;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

public class DatabaseConnection {

    public static Connection getConnection()
            throws SQLException {

        Properties properties = new Properties();

        try (FileInputStream file =
                     new FileInputStream("db.properties")) {

            properties.load(file);

        } catch (IOException e) {
            throw new SQLException("Cannot load configuration", e);
        }

        return DriverManager.getConnection(
                properties.getProperty("db.url"),
                properties.getProperty("db.username"),
                properties.getProperty("db.password")
        );
    }
}
