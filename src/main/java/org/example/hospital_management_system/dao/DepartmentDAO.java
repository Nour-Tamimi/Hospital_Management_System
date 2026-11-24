package org.example.hospital_management_system.dao;

import org.example.hospital_management_system.Department;
import org.example.hospital_management_system.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {
    //first operation, to get all the departments from the data base:
    public List<Department> getAllDepartments() throws SQLException{
        List<Department> departments = new ArrayList<>();

        String sql = """
                SELECT DepartmentID, DepartmentName, Location, Phone
                FROM Department
                ORDER BY DepartmentName
                """;

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){

            while(rs.next()){
                Department department = new Department(
                        rs.getInt("DepartmentID"),
                        rs.getString("DepartmentName"),
                        rs.getString("Location"),
                        rs.getString("Phone")
                );
                departments.add(department);
            }
        }
        return departments;
    }

    //second operation, to search departments by name or location:
    public List<Department> searchDepartments(String keyword)
            throws SQLException {

        List<Department> departments = new ArrayList<>();

        String sql = """
                SELECT DepartmentID, DepartmentName, Location, Phone
                FROM Department
                WHERE DepartmentName LIKE ?
                   OR Location LIKE ?
                ORDER BY DepartmentName
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchValue = "%" + keyword + "%";

            stmt.setString(1, searchValue);
            stmt.setString(2, searchValue);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    departments.add(new Department(
                            rs.getInt("DepartmentID"),
                            rs.getString("DepartmentName"),
                            rs.getString("Location"),
                            rs.getString("Phone")
                    ));
                }
            }
        }

        return departments;
    }

    //third operation, to add a department
    public void addDepartment(Department department)
            throws SQLException {

        String sql = """
                INSERT INTO Department
                (DepartmentName, Location, Phone)
                VALUES (?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, department.getDepartmentName());
            stmt.setString(2, department.getLocation());
            stmt.setString(3, department.getPhone());

            stmt.executeUpdate();
        }
    }

    //fourth operation, ot update a department
    public void updateDepartment(Department department) throws SQLException{
        String sql = """
                UPDATE Department
                SET DepartmentName = ?,
                    Location = ?,
                    Phone = ?
                WHERE DepartmentID = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, department.getDepartmentName());
            stmt.setString(2, department.getLocation());
            stmt.setString(3, department.getPhone());
            stmt.setInt(4, department.getDepartmentId());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("Department not found.");
            }
        }
    }

    //fifth operation, to delete a department
    public void deleteDepartment(int departmentID) throws SQLException{
        String sql = """
                DELETE FROM Department
                where DepartmentID = ?
                """;

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

                stmt.setInt(1, departmentID);

                int rowAffected = stmt.executeUpdate();

                if(rowAffected == 0){
                    throw new SQLException("Department not found.");
                }

        }
    }

}
