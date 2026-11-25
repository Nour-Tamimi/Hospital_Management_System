package org.example.hospital_management_system.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.example.hospital_management_system.model.Patient;

import java.time.LocalDate;

public class PatientController {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Patient> patientTable;

    @FXML
    private TableColumn<Patient, Integer> patientIdColumn;

    @FXML
    private TableColumn<Patient, String> firstNameColumn;

    @FXML
    private TableColumn<Patient, String> lastNameColumn;

    @FXML
    private TableColumn<Patient, LocalDate> dateOfBirthColumn;

    @FXML
    private TableColumn<Patient, String> genderColumn;

    @FXML
    private TableColumn<Patient, String> phoneColumn;

    @FXML
    private TableColumn<Patient, String> emailColumn;

    @FXML
    private TableColumn<Patient, String> bloodTypeColumn;

    @FXML
    private void handleSearch() {
    }

    @FXML
    private void loadPatients() {
    }

    @FXML
    private void handleAddPatient() {
    }

    @FXML
    private void handleEditPatient() {
    }

    @FXML
    private void handleDeletePatient() {
    }
}