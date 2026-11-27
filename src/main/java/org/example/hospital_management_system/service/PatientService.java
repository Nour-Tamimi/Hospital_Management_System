package org.example.hospital_management_system.service;

import org.example.hospital_management_system.dao.PatientDAO;
import org.example.hospital_management_system.model.Patient;
import org.example.hospital_management_system.model.Role;
import org.example.hospital_management_system.util.Session;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class PatientService {

    private final PatientDAO patientDAO = new PatientDAO();

    private static final Set<String> GENDERS =
            Set.of("Male", "Female", "Other");

    private static final Set<String> BLOOD_TYPES =
            Set.of("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");

    public List<Patient> getAllPatients() throws SQLException {
        Role role = requireLoggedIn();

        if (role == Role.DOCTOR) {
            Integer doctorId = Session.getDoctorId();

            if (doctorId == null) {
                throw new SecurityException("Doctor account is not linked.");
            }

            return patientDAO.getPatientsForDoctor(doctorId);
        }

        if (role == Role.ADMINISTRATOR || role == Role.RECEPTIONIST) {
            return patientDAO.getAllPatients();
        }

        throw new SecurityException("You cannot view patients.");
    }

    public List<Patient> searchPatients(String searchText)
            throws SQLException {
        Role role = requireLoggedIn();

        if (searchText == null || searchText.isBlank()) {
            return getAllPatients();
        }

        if (role == Role.DOCTOR) {
            Integer doctorId = Session.getDoctorId();

            if (doctorId == null) {
                throw new SecurityException("Doctor account is not linked.");
            }

            return patientDAO.searchPatientsForDoctor(
                    searchText.trim(), doctorId);
        }

        if (role == Role.ADMINISTRATOR || role == Role.RECEPTIONIST) {
            return patientDAO.searchPatients(searchText.trim());
        }

        throw new SecurityException("You cannot search patients.");
    }
    public void addPatient(Patient patient) throws SQLException {
        requireRole(Role.ADMINISTRATOR, Role.RECEPTIONIST);
        validatePatient(patient);
        patientDAO.addPatient(patient);
    }

    public boolean updatePatient(Patient patient) throws SQLException {
        requireRole(Role.ADMINISTRATOR, Role.RECEPTIONIST);
        validatePatient(patient);

        if (patient.getPatientId() <= 0) {
            throw new IllegalArgumentException("Select a valid patient.");
        }

        return patientDAO.updatePatient(patient);
    }

    public boolean deletePatient(int patientId) throws SQLException {
        requireRole(Role.ADMINISTRATOR);
        if (patientId <= 0) {
            throw new IllegalArgumentException("Select a valid patient.");
        }

        return patientDAO.deletePatient(patientId);
    }

    private void validatePatient(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Patient information is missing.");
        }

        if (patient.getFirstName() == null
                || patient.getFirstName().isBlank()) {
            throw new IllegalArgumentException("First name is required.");
        }

        if (patient.getLastName() == null
                || patient.getLastName().isBlank()) {
            throw new IllegalArgumentException("Last name is required.");
        }

        if (patient.getDateOfBirth() == null
                || patient.getDateOfBirth().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Enter a valid date of birth.");
        }

        if (patient.getGender() == null
                || !GENDERS.contains(patient.getGender())) {
            throw new IllegalArgumentException(
                    "Choose Male, Female, or Other.");
        }

        if (patient.getEmail() != null
                && !patient.getEmail().isBlank()
                && !patient.getEmail().matches(
                "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException(
                    "Enter a valid email address.");
        }

        if (patient.getBloodType() != null
                && !patient.getBloodType().isBlank()
                && !BLOOD_TYPES.contains(patient.getBloodType())) {
            throw new IllegalArgumentException(
                    "Choose a valid blood type.");
        }
    }
    private void requireRole(Role... allowedRoles) {
        if (!Session.isLoggedIn()) {
            throw new SecurityException("Please log in first.");
        }

        Role currentRole = Session.getCurrentRole();

        for (Role allowedRole : allowedRoles) {
            if (currentRole == allowedRole) {
                return;
            }
        }

        throw new SecurityException("You do not have permission to do this.");
    }
    private Role requireLoggedIn() {
        if (!Session.isLoggedIn()) {
            throw new SecurityException("Please log in first.");
        }

        return Session.getCurrentRole();
    }
}