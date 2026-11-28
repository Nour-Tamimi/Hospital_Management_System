
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

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private final AuthService authService = new AuthService();

    @FXML
    private void handleLogin() {

        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Login failed", "Please enter username and password.");
            return;
        }

        // Step 1: Authenticate using the database
        User user;

        try {
            user = authService.login(username, password);

        } catch (SQLException e) {
            e.printStackTrace();
            showError("Database error",
                    "Could not connect to the database.");
            return;

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            showError("Login failed",
                    "Could not verify the login credentials.");
            return;
        }

        // Step 2: Check login result
        if (user == null) {
            showError("Login failed",
                    "Invalid credentials or inactive account.");
            return;
        }

        // Step 3: Start session and open dashboard
        try {
            Session.start(user);

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/dashboard.fxml")
            );

            Parent root = loader.load();

            Stage stage =
                    (Stage) usernameField.getScene().getWindow();

            stage.setScene(new Scene(root, 1000, 700));
            stage.setTitle("Hospital Management System");

        } catch (IOException | RuntimeException e) {
            Session.clear();
            e.printStackTrace();

            showError("Screen error",
                    "Could not open the dashboard.");
        }
    }

    private void showError(String title, String message) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
