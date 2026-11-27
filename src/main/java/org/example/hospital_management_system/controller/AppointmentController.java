package org.example.hospital_management_system.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.hospital_management_system.model.Appointment;
import org.example.hospital_management_system.model.Role;
import org.example.hospital_management_system.service.AppointmentService;
import org.example.hospital_management_system.util.Session;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AppointmentController {

    @FXML private TextField patientFilterField;
    @FXML private TextField doctorFilterField;
    @FXML private DatePicker dateFilterPicker;
    @FXML private ComboBox<String> statusFilterComboBox;

    @FXML private TableView<Appointment> appointmentTable;
    @FXML private TableColumn<Appointment, Integer> appointmentIdColumn;
    @FXML private TableColumn<Appointment, Integer> patientIdColumn;
    @FXML private TableColumn<Appointment, Integer> doctorIdColumn;
    @FXML private TableColumn<Appointment, LocalDateTime> appointmentDateColumn;
    @FXML private TableColumn<Appointment, String> reasonColumn;
    @FXML private TableColumn<Appointment, String> statusColumn;

    @FXML private TextField patientIdField;
    @FXML private TextField doctorIdField;
    @FXML private DatePicker appointmentDatePicker;
    @FXML private TextField timeField;
    @FXML private TextField reasonField;
    @FXML private ComboBox<String> appointmentStatusComboBox;
    @FXML private Button saveButton;
    @FXML private Button deleteButton;

    private final AppointmentService appointmentService =
            new AppointmentService();

    private Appointment selectedAppointment;

    @FXML
    private void initialize() {
        appointmentIdColumn.setCellValueFactory(
                new PropertyValueFactory<>("appointmentId"));
        patientIdColumn.setCellValueFactory(
                new PropertyValueFactory<>("patientId"));
        doctorIdColumn.setCellValueFactory(
                new PropertyValueFactory<>("doctorId"));
        appointmentDateColumn.setCellValueFactory(
                new PropertyValueFactory<>("appointmentDate"));
        reasonColumn.setCellValueFactory(
                new PropertyValueFactory<>("reason"));
        statusColumn.setCellValueFactory(
                new PropertyValueFactory<>("status"));

        statusFilterComboBox.getItems().setAll(
                "All statuses", "Scheduled", "Completed", "Cancelled");
        statusFilterComboBox.setValue("All statuses");

        appointmentStatusComboBox.getItems().setAll(
                "Scheduled", "Completed", "Cancelled");
        appointmentStatusComboBox.setValue("Scheduled");

        appointmentTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) ->
                        showSelectedAppointment(newValue));

        try {
            boolean doctor = Session.getCurrentRole() == Role.DOCTOR;
            patientIdField.setDisable(doctor);
            doctorIdField.setDisable(doctor);
            appointmentDatePicker.setDisable(doctor);
            timeField.setDisable(doctor);
            reasonField.setDisable(doctor);
            saveButton.setDisable(doctor);
            deleteButton.setDisable(doctor);

            loadAppointments();
        } catch (SecurityException e) {
            showError("Access denied", e.getMessage());
        }
    }

    @FXML
    private void loadAppointments() {
        try {
            appointmentTable.setItems(FXCollections.observableArrayList(
                    appointmentService.getAllAppointments()));
        } catch (SQLException | SecurityException e) {
            showError("Could not load appointments", e.getMessage());
        }
    }

    @FXML
    private void handleSearch() {
        try {
            Integer patientId = parseOptionalId(patientFilterField.getText());
            Integer doctorId = parseOptionalId(doctorFilterField.getText());
            LocalDate date = dateFilterPicker.getValue();
            String status = statusFilterComboBox.getValue();

            if ("All statuses".equals(status)) {
                status = null;
            }

            appointmentTable.setItems(FXCollections.observableArrayList(
                    appointmentService.searchAppointments(
                            patientId, doctorId, date, status)));
        } catch (NumberFormatException e) {
            showError("Invalid search", "Patient and doctor IDs must be numbers.");
        } catch (SQLException | SecurityException e) {
            showError("Could not search appointments", e.getMessage());
        }
    }

    @FXML
    private void handleSave() {
        try {
            int patientId = Integer.parseInt(patientIdField.getText().trim());
            int doctorId = Integer.parseInt(doctorIdField.getText().trim());

            LocalDate date = appointmentDatePicker.getValue();
            if (date == null) {
                throw new IllegalArgumentException("Choose an appointment date.");
            }

            LocalTime time = LocalTime.parse(timeField.getText().trim());

            Appointment appointment = new Appointment(
                    patientId,
                    doctorId,
                    LocalDateTime.of(date, time),
                    emptyToNull(reasonField.getText()),
                    appointmentStatusComboBox.getValue()
            );

            if (selectedAppointment == null) {
                appointmentService.addAppointment(appointment);
            } else {
                appointment.setAppointmentId(
                        selectedAppointment.getAppointmentId());
                appointmentService.updateAppointment(appointment);
            }

            loadAppointments();
            clearForm();

        } catch (NumberFormatException e) {
            showError("Invalid ID", "Patient ID and doctor ID must be numbers.");
        } catch (java.time.format.DateTimeParseException e) {
            showError("Invalid time", "Enter time like 14:30.");
        } catch (IllegalArgumentException | SecurityException e) {
            showError("Could not save appointment", e.getMessage());
        } catch (SQLException e) {
            showError("Could not save appointment",
                    "Check the patient/doctor IDs and database connection.");
        }
    }

    @FXML
    private void handleDelete() {
        if (selectedAppointment == null) {
            showError("Select an appointment", "Choose an appointment first.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setHeaderText("Delete appointment?");
        confirmation.setContentText("Delete appointment "
                + selectedAppointment.getAppointmentId() + "?");

        if (confirmation.showAndWait().orElse(ButtonType.CANCEL)
                != ButtonType.OK) {
            return;
        }

        try {
            appointmentService.deleteAppointment(
                    selectedAppointment.getAppointmentId());
            loadAppointments();
            clearForm();
        } catch (SQLException | SecurityException | IllegalArgumentException e) {
            showError("Could not delete appointment",
                    "It may be linked to a medical record. " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateStatus() {
        if (selectedAppointment == null) {
            showError("Select an appointment", "Choose an appointment first.");
            return;
        }

        try {
            appointmentService.updateAppointmentStatus(
                    selectedAppointment.getAppointmentId(),
                    appointmentStatusComboBox.getValue());
            loadAppointments();
            clearForm();
        } catch (SQLException | SecurityException | IllegalArgumentException e) {
            showError("Could not update status", e.getMessage());
        }
    }

    @FXML
    private void handleClear() {
        clearForm();
    }

    @FXML
    private void handleBackToPatients() {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/fxml/patients.fxml"));
            appointmentTable.getScene().setRoot(root);
        } catch (IOException e) {
            showError("Could not open patients", e.getMessage());
        }
    }

    private void showSelectedAppointment(Appointment appointment) {
        selectedAppointment = appointment;

        if (appointment == null) {
            return;
        }

        patientIdField.setText(String.valueOf(appointment.getPatientId()));
        doctorIdField.setText(String.valueOf(appointment.getDoctorId()));
        appointmentDatePicker.setValue(
                appointment.getAppointmentDate().toLocalDate());
        timeField.setText(
                appointment.getAppointmentDate().toLocalTime().toString());
        reasonField.setText(appointment.getReason());
        appointmentStatusComboBox.setValue(appointment.getStatus());
    }

    private void clearForm() {
        selectedAppointment = null;
        appointmentTable.getSelectionModel().clearSelection();
        patientIdField.clear();
        doctorIdField.clear();
        appointmentDatePicker.setValue(null);
        timeField.clear();
        reasonField.clear();
        appointmentStatusComboBox.setValue("Scheduled");
    }

    private Integer parseOptionalId(String text) {
        return text == null || text.isBlank()
                ? null
                : Integer.parseInt(text.trim());
    }

    private String emptyToNull(String text) {
        return text == null || text.isBlank() ? null : text.trim();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(title);
        alert.setContentText(message == null ? "An unexpected error occurred." : message);
        alert.showAndWait();
    }
}