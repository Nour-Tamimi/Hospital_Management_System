package org.example.hospital_management_system.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class PatientFormController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private DatePicker dateOfBirthPicker;
    @FXML private ComboBox<String> genderComboBox;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private TextField addressField;
    @FXML private ComboBox<String> bloodTypeComboBox;

    @FXML
    private void initialize() {
        genderComboBox.getItems().setAll("Male", "Female", "Other");
        bloodTypeComboBox.getItems().setAll(
                "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"
        );
    }

    @FXML
    private void handleSave() {
        // We'll add saving logic next.
    }

    @FXML
    private void handleCancel() {
        // We'll add cancel logic next.
    }
}