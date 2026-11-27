package org.example.hospital_management_system.dao;

import org.example.hospital_management_system.config.DatabaseConnection;
import org.example.hospital_management_system.model.Appointment;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    public List<Appointment> getAllAppointments() throws SQLException {
        return searchAppointments(null, null, null, null);
    }

    public List<Appointment> getAppointmentsForDoctor(int doctorId)
            throws SQLException {
        return searchAppointments(null, doctorId, null, null);
    }

    public void addAppointment(Appointment appointment) throws SQLException {
        String sql = """
                INSERT INTO Appointment
                (PatientID, DoctorID, AppointmentDate, Reason, Status)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            setAppointmentValues(statement, appointment);
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    appointment.setAppointmentId(keys.getInt(1));
                }
            }
        }
    }

    public boolean updateAppointment(Appointment appointment)
            throws SQLException {
        String sql = """
                UPDATE Appointment
                SET PatientID = ?, DoctorID = ?, AppointmentDate = ?,
                    Reason = ?, Status = ?
                WHERE AppointmentID = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            setAppointmentValues(statement, appointment);
            statement.setInt(6, appointment.getAppointmentId());

            return statement.executeUpdate() > 0;
        }
    }

    public boolean deleteAppointment(int appointmentId) throws SQLException {
        String sql = "DELETE FROM Appointment WHERE AppointmentID = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, appointmentId);
            return statement.executeUpdate() > 0;
        }
    }

    public List<Appointment> searchAppointments(
            Integer patientId,
            Integer doctorId,
            LocalDate date,
            String status) throws SQLException {

        StringBuilder sql =
                new StringBuilder("SELECT * FROM Appointment WHERE 1=1");
        List<Object> values = new ArrayList<>();

        if (patientId != null) {
            sql.append(" AND PatientID = ?");
            values.add(patientId);
        }

        if (doctorId != null) {
            sql.append(" AND DoctorID = ?");
            values.add(doctorId);
        }

        if (date != null) {
            sql.append(" AND DATE(AppointmentDate) = ?");
            values.add(date);
        }

        if (status != null && !status.isBlank()) {
            sql.append(" AND Status = ?");
            values.add(status);
        }

        sql.append(" ORDER BY AppointmentDate");

        List<Appointment> appointments = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql.toString())) {

            for (int i = 0; i < values.size(); i++) {
                Object value = values.get(i);
                int parameterNumber = i + 1;

                if (value instanceof Integer id) {
                    statement.setInt(parameterNumber, id);
                } else if (value instanceof LocalDate appointmentDate) {
                    statement.setDate(
                            parameterNumber,
                            java.sql.Date.valueOf(appointmentDate)
                    );
                } else if (value instanceof String appointmentStatus) {
                    statement.setString(parameterNumber, appointmentStatus);
                }
            }

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    appointments.add(readAppointment(result));
                }
            }
        }

        return appointments;
    }

    private void setAppointmentValues(
            PreparedStatement statement,
            Appointment appointment) throws SQLException {

        statement.setInt(1, appointment.getPatientId());
        statement.setInt(2, appointment.getDoctorId());
        statement.setTimestamp(
                3, Timestamp.valueOf(appointment.getAppointmentDate()));
        statement.setString(4, appointment.getReason());
        statement.setString(5, appointment.getStatus());
    }

    private Appointment readAppointment(ResultSet result)
            throws SQLException {

        Appointment appointment = new Appointment(
                result.getInt("PatientID"),
                result.getInt("DoctorID"),
                result.getTimestamp("AppointmentDate").toLocalDateTime(),
                result.getString("Reason"),
                result.getString("Status")
        );

        appointment.setAppointmentId(result.getInt("AppointmentID"));
        return appointment;
    }
}