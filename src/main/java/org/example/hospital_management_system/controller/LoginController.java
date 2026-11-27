package org.example.hospital_management_system.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.hospital_management_system.model.User;
import org.example.hospital_management_system.service.AuthService;
import org.example.hospital_management_system.util.Session;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    private final AuthService authService = new AuthService();

    @FXML
    private void handleLogin() {
        try {
            User user = authService.login(
                    usernameField.getText().trim(),
                    passwordField.getText()
            );

            if (user == null) {
                showError("Login failed", "Check your username and password.");
                return;
            }

            Session.start(user);

            Parent root = FXMLLoader.load(
                    getClass().getResource("/fxml/patients.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Hospital Management System");

        } catch (SQLException e) {
            showError("Database error", "Could not log in. Check the database connection.");
        } catch (IllegalArgumentException e) {
            showError("Login failed", e.getMessage());
        } catch (IOException e) {
            showError("Screen error", "Could not open the patients screen.");
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}