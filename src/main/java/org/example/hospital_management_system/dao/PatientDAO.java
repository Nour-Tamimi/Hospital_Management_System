package org.example.hospital_management_system.dao;

import org.example.hospital_management_system.config.DatabaseConnection;
import org.example.hospital_management_system.model.Patient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    public List<Patient> getAllPatients() throws SQLException {
        String sql = "SELECT * FROM Patient ORDER BY PatientID";
        List<Patient> patients = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                patients.add(readPatient(result));
            }
        }

        return patients;
    }

    public void addPatient(Patient patient) throws SQLException {
        String sql = """
                INSERT INTO Patient
                (FirstName, LastName, DateOfBirth, Gender,
                 Phone, Email, Address, BloodType)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            setPatientValues(statement, patient);
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    patient.setPatientId(keys.getInt(1));
                }
            }
        }
    }

    public boolean updatePatient(Patient patient) throws SQLException {
        String sql = """
                UPDATE Patient
                SET FirstName = ?, LastName = ?, DateOfBirth = ?, Gender = ?,
                    Phone = ?, Email = ?, Address = ?, BloodType = ?
                WHERE PatientID = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            setPatientValues(statement, patient);
            statement.setInt(9, patient.getPatientId());

            return statement.executeUpdate() > 0;
        }
    }

    public boolean deletePatient(int patientId) throws SQLException {
        String sql = "DELETE FROM Patient WHERE PatientID = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, patientId);
            return statement.executeUpdate() > 0;
        }
    }

    public List<Patient> searchPatients(String searchText) throws SQLException {
        String sql = """
                SELECT * FROM Patient
                WHERE CAST(PatientID AS CHAR) LIKE ?
                   OR FirstName LIKE ?
                   OR LastName LIKE ?
                ORDER BY PatientID
                """;

        List<Patient> patients = new ArrayList<>();
        String search = "%" + searchText.trim() + "%";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, search);
            statement.setString(2, search);
            statement.setString(3, search);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    patients.add(readPatient(result));
                }
            }
        }

        return patients;
    }

    private void setPatientValues(PreparedStatement statement, Patient patient)
            throws SQLException {
        statement.setString(1, patient.getFirstName());
        statement.setString(2, patient.getLastName());
        statement.setDate(3, java.sql.Date.valueOf(patient.getDateOfBirth()));
        statement.setString(4, patient.getGender());
        statement.setString(5, patient.getPhone());
        statement.setString(6, patient.getEmail());
        statement.setString(7, patient.getAddress());
        statement.setString(8, patient.getBloodType());
    }

    private Patient readPatient(ResultSet result) throws SQLException {
        Patient patient = new Patient(
                result.getString("FirstName"),
                result.getString("LastName"),
                result.getDate("DateOfBirth").toLocalDate(),
                result.getString("Gender"),
                result.getString("Phone"),
                result.getString("Email"),
                result.getString("Address"),
                result.getString("BloodType")
        );

        patient.setPatientId(result.getInt("PatientID"));
        return patient;
    }

    public List<Patient> getPatientsForDoctor(int doctorId)
            throws SQLException {
        String sql = """
            SELECT DISTINCT p.*
            FROM Patient p
            JOIN Appointment a ON a.PatientID = p.PatientID
            WHERE a.DoctorID = ?
            ORDER BY p.PatientID
            """;

        List<Patient> patients = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, doctorId);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    patients.add(readPatient(result));
                }
            }
        }

        return patients;
    }

    public List<Patient> searchPatientsForDoctor(String searchText, int doctorId)
            throws SQLException {
        String sql = """
            SELECT DISTINCT p.*
            FROM Patient p
            JOIN Appointment a ON a.PatientID = p.PatientID
            WHERE a.DoctorID = ?
              AND (
                  CAST(p.PatientID AS CHAR) LIKE ?
                  OR p.FirstName LIKE ?
                  OR p.LastName LIKE ?
              )
            ORDER BY p.PatientID
            """;

        List<Patient> patients = new ArrayList<>();
        String search = "%" + searchText.trim() + "%";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, doctorId);
            statement.setString(2, search);
            statement.setString(3, search);
            statement.setString(4, search);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    patients.add(readPatient(result));
                }
            }
        }

        return patients;
    }
}