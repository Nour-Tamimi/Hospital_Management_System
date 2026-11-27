package org.example.hospital_management_system.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.hospital_management_system.model.Patient;
import org.example.hospital_management_system.service.PatientService;

import java.sql.SQLException;

public class PatientFormController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private DatePicker dateOfBirthPicker;
    @FXML private ComboBox<String> genderComboBox;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private TextField addressField;
    @FXML private ComboBox<String> bloodTypeComboBox;

    private final PatientService patientService = new PatientService();
    private Patient patientBeingEdited;
    private Runnable onSaved = () -> {};

    @FXML
    private void initialize() {
        genderComboBox.getItems().setAll("Male", "Female", "Other");
        bloodTypeComboBox.getItems().setAll(
                "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"
        );
    }

    public void setPatientToEdit(Patient patient) {
        patientBeingEdited = patient;

        if (patient == null) {
            return;
        }

        firstNameField.setText(patient.getFirstName());
        lastNameField.setText(patient.getLastName());
        dateOfBirthPicker.setValue(patient.getDateOfBirth());
        genderComboBox.setValue(patient.getGender());
        phoneField.setText(patient.getPhone());
        emailField.setText(patient.getEmail());
        addressField.setText(patient.getAddress());
        bloodTypeComboBox.setValue(patient.getBloodType());
    }

    public void setOnSaved(Runnable onSaved) {
        this.onSaved = onSaved == null ? () -> {} : onSaved;
    }

    @FXML
    private void handleSave() {
        Patient patient = new Patient(
                firstNameField.getText().trim(),
                lastNameField.getText().trim(),
                dateOfBirthPicker.getValue(),
                genderComboBox.getValue(),
                emptyToNull(phoneField.getText()),
                emptyToNull(emailField.getText()),
                emptyToNull(addressField.getText()),
                bloodTypeComboBox.getValue()
        );

        try {
            if (patientBeingEdited == null) {
                patientService.addPatient(patient);
            } else {
                patient.setPatientId(patientBeingEdited.getPatientId());

                if (!patientService.updatePatient(patient)) {
                    showError("Patient not found.");
                    return;
                }
            }

            onSaved.run();
            closeForm();

        } catch (IllegalArgumentException | SecurityException e) {
            showError(e.getMessage());
        } catch (SQLException e) {
            showError("Could not save the patient. Check the database and email.");
        }
    }

    @FXML
    private void handleCancel() {
        closeForm();
    }

    private String emptyToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Could not save patient");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeForm() {
        Stage stage = (Stage) firstNameField.getScene().getWindow();
        stage.close();
    }
}