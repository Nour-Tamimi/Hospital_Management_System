package org.example.hospital_management_system.service;

import org.example.hospital_management_system.dao.AppointmentDAO;
import org.example.hospital_management_system.model.Appointment;
import org.example.hospital_management_system.model.Role;
import org.example.hospital_management_system.util.Session;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class AppointmentService {

    private final AppointmentDAO appointmentDAO = new AppointmentDAO();

    private static final Set<String> STATUSES =
            Set.of("Scheduled", "Completed", "Cancelled");

    public List<Appointment> getAllAppointments() throws SQLException {
        Role role = requireLoggedIn();

        if (role == Role.DOCTOR) {
            Integer doctorId = Session.getDoctorId();

            if (doctorId == null) {
                throw new SecurityException("Doctor account is not linked.");
            }

            return appointmentDAO.getAppointmentsForDoctor(doctorId);
        }

        requireRole(Role.ADMINISTRATOR, Role.RECEPTIONIST);
        return appointmentDAO.getAllAppointments();
    }

    public List<Appointment> searchAppointments(
            Integer patientId,
            Integer doctorId,
            LocalDate date,
            String status) throws SQLException {

        Role role = requireLoggedIn();

        if (role == Role.DOCTOR) {
            Integer currentDoctorId = Session.getDoctorId();

            if (currentDoctorId == null) {
                throw new SecurityException("Doctor account is not linked.");
            }

            // Always use the logged-in doctor's ID for the database search.
            return appointmentDAO.searchAppointments(
                    patientId, currentDoctorId, date, status);
        }

        requireRole(Role.ADMINISTRATOR, Role.RECEPTIONIST);
        return appointmentDAO.searchAppointments(
                patientId, doctorId, date, status);
    }

    public void addAppointment(Appointment appointment) throws SQLException {
        requireRole(Role.ADMINISTRATOR, Role.RECEPTIONIST);
        validateAppointment(appointment);
        appointmentDAO.addAppointment(appointment);
    }

    public boolean updateAppointment(Appointment appointment)
            throws SQLException {
        requireRole(Role.ADMINISTRATOR, Role.RECEPTIONIST);
        validateAppointment(appointment);

        if (appointment.getAppointmentId() <= 0) {
            throw new IllegalArgumentException("Select a valid appointment.");
        }

        return appointmentDAO.updateAppointment(appointment);
    }

    public boolean deleteAppointment(int appointmentId) throws SQLException {
        requireRole(Role.ADMINISTRATOR, Role.RECEPTIONIST);

        if (appointmentId <= 0) {
            throw new IllegalArgumentException("Select a valid appointment.");
        }

        // The database may reject deletion if another record refers to it.
        return appointmentDAO.deleteAppointment(appointmentId);
    }

    public boolean updateAppointmentStatus(int appointmentId, String status)
            throws SQLException {
        Role role = requireLoggedIn();

        if (role != Role.ADMINISTRATOR
                && role != Role.RECEPTIONIST
                && role != Role.DOCTOR) {
            throw new SecurityException(
                    "You do not have permission to change appointment status.");
        }

        if (appointmentId <= 0) {
            throw new IllegalArgumentException("Select a valid appointment.");
        }

        if (status == null || !STATUSES.contains(status)) {
            throw new IllegalArgumentException("Choose a valid appointment status.");
        }

        // For doctors, this list contains only appointments assigned to them.
        List<Appointment> visibleAppointments = getAllAppointments();

        for (Appointment appointment : visibleAppointments) {
            if (appointment.getAppointmentId() == appointmentId) {
                if (role == Role.DOCTOR
                        && status.equals("Scheduled")) {
                    throw new SecurityException(
                            "Doctors can mark their appointments Completed or Cancelled.");
                }

                appointment.setStatus(status);
                return appointmentDAO.updateAppointment(appointment);
            }
        }

        throw new SecurityException(
                "Appointment not found or you are not allowed to access it.");
    }

    private void validateAppointment(Appointment appointment) {
        if (appointment == null) {
            throw new IllegalArgumentException(
                    "Appointment information is missing.");
        }

        if (appointment.getPatientId() <= 0) {
            throw new IllegalArgumentException("Choose a valid patient.");
        }

        if (appointment.getDoctorId() <= 0) {
            throw new IllegalArgumentException("Choose a valid doctor.");
        }

        if (appointment.getAppointmentDate() == null) {
            throw new IllegalArgumentException(
                    "Appointment date and time are required.");
        }

        if (appointment.getStatus() == null
                || !STATUSES.contains(appointment.getStatus())) {
            throw new IllegalArgumentException(
                    "Choose Scheduled, Completed, or Cancelled.");
        }

        if (appointment.getStatus().equals("Scheduled")
                && appointment.getAppointmentDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "A scheduled appointment must be in the future.");
        }
    }

    private Role requireLoggedIn() {
        if (!Session.isLoggedIn()) {
            throw new SecurityException("Please log in first.");
        }

        return Session.getCurrentRole();
    }

    private void requireRole(Role... allowedRoles) {
        Role currentRole = requireLoggedIn();

        for (Role allowedRole : allowedRoles) {
            if (currentRole == allowedRole) {
                return;
            }
        }

        throw new SecurityException(
                "You do not have permission to do this.");
    }
}