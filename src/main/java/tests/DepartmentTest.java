package org.example.hospital_management_system;


import org.example.hospital_management_system.dao.DepartmentDAO;
import org.example.hospital_management_system.model.Department;

public class DepartmentTest {

    public static void main(String[] args) {

        DepartmentDAO dao = new DepartmentDAO();

        try {

            // Display all departments
            System.out.println("ALL DEPARTMENTS:");

            for (Department d : dao.getAllDepartments()) {

                System.out.println(
                        d.getDepartmentId() + " | " +
                                d.getDepartmentName() + " | " +
                                d.getLocation() + " | " +
                                d.getPhone()
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
