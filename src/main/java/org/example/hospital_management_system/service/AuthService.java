package org.example.hospital_management_system.service;

import org.example.hospital_management_system.dao.UserDAO;
import org.example.hospital_management_system.model.User;
import org.example.hospital_management_system.util.PasswordUtil;

import java.sql.SQLException;

public class AuthService {
    public User login(String username, String password) throws SQLException {
        UserDAO dao = new UserDAO();
        User user = dao.findByUsername(username);

        if (user == null) {
            return null;
        }


        if(!PasswordUtil.verifyPassword(password, user.getPasswordHash())){
            return null;
        }

        if(!user.isActive()){
            return null;
        }
        return user;
        }
    }

