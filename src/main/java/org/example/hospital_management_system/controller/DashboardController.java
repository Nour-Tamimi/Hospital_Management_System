package org.example.hospital_management_system.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import org.example.hospital_management_system.model.Role;
import org.example.hospital_management_system.util.Session;

import java.io.IOException;

public class DashboardController {
    @FXML
    private Label welcomeLabel;

    @FXML
    private StackPane contentArea;

    @FXML
    private Button patientsButton;

    @FXML
    private Button appointmentsButton;

    @FXML
    private Button doctorsButton;

    @FXML
    private Button departmentsButton;

    @FXML
    private Button medicalRecordsButton;

    @FXML
    private Button usersButton;

    @FXML
    private void initialize() {
        Role role = Session.getCurrentRole();
        welcomeLabel.setText("Welcome, " + role);
        boolean isAdmin = role == Role.ADMINISTRATOR;

        usersButton.setVisible(isAdmin);
        usersButton.setManaged(isAdmin);
        boolean isReceptionist = role == Role.RECEPTIONIST;
        boolean isDoctor = role == Role.DOCTOR;

        doctorsButton.setVisible(!isDoctor);
        doctorsButton.setManaged(!isDoctor);

        departmentsButton.setVisible(!isDoctor);
        departmentsButton.setManaged(!isDoctor);

        medicalRecordsButton.setVisible(isAdmin || isDoctor);
        medicalRecordsButton.setManaged(isAdmin || isDoctor);
    }
    @FXML
    private void showPatients() {
        loadPage("/fxml/patients.fxml");
    }

    private void loadPage(String path) {
        try {
            Parent page = FXMLLoader.load(
                    getClass().getResource(path)
            );

            contentArea.getChildren().setAll(page);

        } catch (IOException | RuntimeException e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Screen Error");
            alert.setContentText("Could not open the selected page.");
            alert.showAndWait();
        }
    }

    @FXML
    private void showAppointments() {
        loadPage("/fxml/appointments.fxml");
    }

    @FXML
    private void showDashboard() {
        contentArea.getChildren().setAll(
                new Label("Welcome to the Hospital Management System")
        );
    }

    @FXML
    private void showDoctors() {
        contentArea.getChildren().setAll(new Label("Doctors - Coming Soon"));
    }

    @FXML
    private void showDepartments() {
        contentArea.getChildren().setAll(new Label("Departments - Coming Soon"));
    }

    @FXML
    private void showMedicalRecords() {
        Role role = Session.getCurrentRole();

        if (role != Role.ADMINISTRATOR && role != Role.DOCTOR) {
            return;
        }

        contentArea.getChildren().setAll(
                new Label("Medical Records - Coming Soon")
        );
    }

    @FXML
    private void showUsers() {
        if (Session.getCurrentRole() != Role.ADMINISTRATOR) {
            return;
        }

        contentArea.getChildren().setAll(
                new Label("Manage Users - Coming Soon")
        );
    }
    @FXML
    private void handleLogout() {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/fxml/login.fxml")
            );

            Stage stage = (Stage) contentArea.getScene().getWindow();

            Session.clear();
            stage.setScene(new Scene(root, 1000, 700));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
