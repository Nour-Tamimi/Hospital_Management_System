
package org.example.hospital_management_system.model;

public class Department {

    private int departmentId;
    private String departmentName;
    private String location;
    private String phone;

    // Used when retrieving an existing department from the database
    public Department(int departmentId, String departmentName,
                      String location, String phone) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.location = location;
        this.phone = phone;
    }

    // Used when creating a new department
    // The database generates departmentId automatically
    public Department(String departmentName, String location, String phone) {
        this.departmentName = departmentName;
        this.location = location;
        this.phone = phone;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return departmentName;
    }
}
