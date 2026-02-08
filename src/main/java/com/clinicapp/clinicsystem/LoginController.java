package com.clinicapp.clinicsystem;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblStatus;

    @FXML
    public void initialize() {
        // Enter يعمل Login
        txtPassword.setOnAction(e -> onLogin());
        txtUsername.setOnAction(e -> onLogin());
    }

    @FXML
    private void onLogin() {
        String u = txtUsername.getText() == null ? "" : txtUsername.getText().trim();
        String p = txtPassword.getText() == null ? "" : txtPassword.getText().trim();

        if (u.isEmpty() || p.isEmpty()) {
            lblStatus.setText("Please enter username & password");
            return;
        }

        if ("admin".equals(u) && "admin123".equals(p)) {
            lblStatus.setStyle("-fx-font-weight: bold; -fx-text-fill: #1a7f37;");
            lblStatus.setText("Login Success");
            HelloApplication.showDashboard();
        }
        if ("hamida".equals(u) && "hamida123".equals(p)) {
            lblStatus.setStyle("-fx-font-weight: bold; -fx-text-fill: #1a7f37;");
            lblStatus.setText("Login Success");
            HelloApplication.showDashboard();
        }
        else {
            lblStatus.setStyle("-fx-font-weight: bold; -fx-text-fill: #cc0000;");
            lblStatus.setText("Wrong username or password");
        }
    }
}
