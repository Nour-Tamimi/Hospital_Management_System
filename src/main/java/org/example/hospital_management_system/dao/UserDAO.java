
package org.example.hospital_management_system.dao;

import org.example.hospital_management_system.config.DatabaseConnection;
import org.example.hospital_management_system.model.Role;
import org.example.hospital_management_system.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public User findByUsername(String username) throws SQLException {

        String sql = "SELECT * FROM Users WHERE Username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    User user = new User(
                            rs.getInt("UserID"),
                            rs.getString("Username"),
                            rs.getString("PasswordHash"),
                            rs.getString("FullName"),
                            Role.valueOf(
                                    rs.getString("Role").toUpperCase()
                            ),
                            rs.getBoolean("IsActive"),
                            rs.getTimestamp("CreatedAt").toLocalDateTime(),
                            rs.getObject("DoctorID", Integer.class)
                    );

                    return user;
                }
            }
        }

        return null;
    }
}
