package org.example.hospital_management_system.dao;
import org.example.hospital_management_system.model.User;
import java.sql.SQLException;

public class TestUserDAO {
    public static void main(String[] args){
        UserDAO u = new UserDAO();

        try{
            User user =u.findByUsername("doctor1");

            if(user != null){
                System.out.println("User Found");
                System.out.println("Name: " + user.getFullName());
                System.out.println("Role: " + user.getRole());
                System.out.println("Active: " + user.isActive());
                System.out.println("Doctor ID: " + user.getDoctorId());

            }else{
                System.out.println("User not found");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
