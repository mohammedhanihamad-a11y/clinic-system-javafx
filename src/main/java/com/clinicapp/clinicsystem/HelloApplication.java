package com.clinicapp.clinicsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class HelloApplication extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("Clinic System (Mohammad Hani Hammad)");
        showLogin();
        primaryStage.show();
    }

    public static void showLogin() {
        setRoot("/com/clinicapp/clinicsystem/login-view.fxml", 520, 330);
    }

    public static void showDashboard() {
        setRoot("/com/clinicapp/clinicsystem/dashboard-view.fxml", 700, 450);
    }

    public static void showPatients() {
        setRoot("/com/clinicapp/clinicsystem/patients-view.fxml", 950, 560);
    }

    private static void setRoot(String fxmlPath, int w, int h) {
        try {
            URL url = HelloApplication.class.getResource(fxmlPath);
            if (url == null) throw new RuntimeException("FXML not found: " + fxmlPath);

            Parent root = FXMLLoader.load(url);
            primaryStage.setScene(new Scene(root, w, h));
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Failed to load UI: " + fxmlPath, ex);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
