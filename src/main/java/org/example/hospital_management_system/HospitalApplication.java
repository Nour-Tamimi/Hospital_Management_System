package org.example.hospital_management_system;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HospitalApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/login.fxml"));

        stage.setTitle("Hospital Management System");
        stage.setScene(new Scene(loader.load(), 1000, 700));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}