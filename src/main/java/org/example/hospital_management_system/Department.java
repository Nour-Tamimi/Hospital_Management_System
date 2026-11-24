package org.example.hospital_management_system;

public class Department {
    private int departmentId;
    private String departmentName;
    private String location;
    private String phone;

    //A constructor for the current departments - db departments:
    public Department(int departmentId, String departmentName, String location, String phone) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.phone = phone;
        this.location = location;
    }
    //A constructor for adding a new department
    public Department(String departmentName, String location, String phone){
        this.departmentName = departmentName;
        this.location = location;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public String toString() {
        return departmentName;
    }
}
