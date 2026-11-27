package org.example.hospital_management_system.service;
import org.example.hospital_management_system.dao.DepartmentDAO;
import org.example.hospital_management_system.model.Department;
import org.example.hospital_management_system.model.Role;

import java.sql.SQLException;
import java.util.List;

public class DepartmentService {

    private final DepartmentDAO departmentDAO;

    public DepartmentService() {
        this.departmentDAO = new DepartmentDAO();
    }

    // To retrieve all departments
    public List<Department> getAllDepartments()
            throws SQLException {
        return departmentDAO.getAllDepartments();
    }

    // Search departments
    public List<Department> searchDepartments(String keyword)
            throws SQLException {

        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllDepartments();
        }

        return departmentDAO.searchDepartments(keyword.trim());
    }

    // Add department
    public void addDepartment(
            Department department, Role currentRole)
            throws SQLException {

        checkAdminPermission(currentRole);
        validateDepartment(department);

        departmentDAO.addDepartment(department);
    }

    // Update department
    public void updateDepartment(
            Department department, Role currentRole)
            throws SQLException {

        checkAdminPermission(currentRole);
        validateDepartment(department);

        if (department.getDepartmentId() <= 0) {
            throw new IllegalArgumentException(
                    "Invalid department ID."
            );
        }

        departmentDAO.updateDepartment(department);
    }

    // Delete department
    public void deleteDepartment(
            int departmentID, Role currentRole)
            throws SQLException {

        checkAdminPermission(currentRole);

        if (departmentID <= 0) {
            throw new IllegalArgumentException(
                    "Invalid department ID."
            );
        }

        departmentDAO.deleteDepartment(departmentID);
    }

    // Check administrator permission
    private void checkAdminPermission(Role currentRole) {

        if (currentRole != Role.ADMINISTRATOR) {
            throw new SecurityException(
                    "Only administrators can manage departments."
            );
        }
    }

    // Validate department information
    private void validateDepartment(Department department) {

        if (department == null) {
            throw new IllegalArgumentException(
                    "Department information is required."
            );
        }

        String name = department.getDepartmentName();

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Department name is required."
            );
        }

        if (name.trim().length() > 100) {
            throw new IllegalArgumentException(
                    "Department name cannot exceed 100 characters."
            );
        }

        if (department.getLocation() != null &&
                department.getLocation().length() > 100) {

            throw new IllegalArgumentException(
                    "Location cannot exceed 100 characters."
            );
        }

        if (department.getPhone() != null &&
                department.getPhone().length() > 25) {

            throw new IllegalArgumentException(
                    "Phone cannot exceed 25 characters."
            );
        }
    }
}

