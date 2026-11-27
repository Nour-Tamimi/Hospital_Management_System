package org.example.hospital_management_system.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.hospital_management_system.model.Patient;
import org.example.hospital_management_system.service.PatientService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class PatientController {

    @FXML private TextField searchField;
    @FXML private TableView<Patient> patientTable;
    @FXML private TableColumn<Patient, Integer> patientIdColumn;
    @FXML private TableColumn<Patient, String> firstNameColumn;
    @FXML private TableColumn<Patient, String> lastNameColumn;
    @FXML private TableColumn<Patient, LocalDate> dateOfBirthColumn;
    @FXML private TableColumn<Patient, String> genderColumn;
    @FXML private TableColumn<Patient, String> phoneColumn;
    @FXML private TableColumn<Patient, String> emailColumn;
    @FXML private TableColumn<Patient, String> bloodTypeColumn;

    private final PatientService patientService = new PatientService();

    @FXML
    private void initialize() {
        patientIdColumn.setCellValueFactory(
                new PropertyValueFactory<>("patientId"));
        firstNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("lastName"));
        dateOfBirthColumn.setCellValueFactory(
                new PropertyValueFactory<>("dateOfBirth"));
        genderColumn.setCellValueFactory(
                new PropertyValueFactory<>("gender"));
        phoneColumn.setCellValueFactory(
                new PropertyValueFactory<>("phone"));
        emailColumn.setCellValueFactory(
                new PropertyValueFactory<>("email"));
        bloodTypeColumn.setCellValueFactory(
                new PropertyValueFactory<>("bloodType"));

        loadPatients();
    }

    @FXML
    private void handleSearch() {
        try {
            patientTable.setItems(FXCollections.observableArrayList(
                    patientService.searchPatients(searchField.getText())));
        } catch (SQLException | SecurityException e) {
            showError("Could not search patients", e.getMessage());
        }
    }

    @FXML
    private void loadPatients() {
        try {
            patientTable.setItems(FXCollections.observableArrayList(
                    patientService.getAllPatients()));
        } catch (SQLException | SecurityException e) {
            showError("Could not load patients", e.getMessage());
        }
    }

    @FXML
    private void handleAddPatient() {
        openPatientForm(null);
    }

    @FXML
    private void handleEditPatient() {
        Patient selected = patientTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Select a patient", "Choose a patient to edit.");
            return;
        }

        openPatientForm(selected);
    }

    @FXML
    private void handleDeletePatient() {
        Patient selected = patientTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Select a patient", "Choose a patient to delete.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setHeaderText("Delete patient?");
        confirmation.setContentText(
                "Delete " + selected.getFirstName() + " "
                        + selected.getLastName() + "?");

        if (confirmation.showAndWait().orElse(ButtonType.CANCEL)
                != ButtonType.OK) {
            return;
        }

        try {
            if (!patientService.deletePatient(selected.getPatientId())) {
                showError("Patient not found", "Refresh the patient list.");
            }
            loadPatients();
        } catch (SQLException | SecurityException | IllegalArgumentException e) {
            showError("Could not delete patient", e.getMessage());
        }
    }

    private void openPatientForm(Patient patient) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/patient-form.fxml"));
            Parent root = loader.load();

            PatientFormController formController = loader.getController();
            formController.setPatientToEdit(patient);
            formController.setOnSaved(this::loadPatients);

            Stage stage = new Stage();
            stage.initOwner(patientTable.getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(patient == null ? "Add Patient" : "Edit Patient");
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (IOException e) {
            showError("Could not open patient form", e.getMessage());
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message == null ? "An unexpected error occurred." : message);
        alert.showAndWait();
    }
    @FXML
    private void handleOpenAppointments() {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/fxml/appointments.fxml"));
            patientTable.getScene().setRoot(root);
        } catch (IOException e) {
            showError("Could not open appointments", e.getMessage());
        }
    }
}