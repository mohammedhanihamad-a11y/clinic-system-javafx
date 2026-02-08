package com.clinicapp.clinicsystem;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.util.List;

public class DashboardController {

    @FXML private Label lblWelcome;

    @FXML private Label lblPatients;
    @FXML private Label lblNextVisits;
    @FXML private Label lblAttended;

    @FXML private Label lblStatus;

    @FXML
    public void initialize() {
        lblWelcome.setText("Welcome, Admin");
        refreshNumbers();
    }

    @FXML
    private void onOpenPatients() {
        HelloApplication.showPatients();
    }

    @FXML
    //To refresh the list
    private void onRefresh() {
        refreshNumbers();
        lblStatus.setText("Refreshed");
    }
//Add here


    @FXML
    private void onLogout() {
        HelloApplication.showLogin();
    }

    private void refreshNumbers() {
        List<Patient> patients = PatientStore.loadAll();

        int totalPatients = patients.size();
        int nextVisits = 0;     // not attended
        int attended = 0;       // attended

        for (Patient p : patients) {
            if (p.getVisits() == null) continue;
            for (Visit v : p.getVisits()) {
                if (v.isAttended()) attended++;
                else nextVisits++;
            }
        }

        lblPatients.setText(String.valueOf(totalPatients));
        lblNextVisits.setText(String.valueOf(nextVisits));
        lblAttended.setText(String.valueOf(attended));
    }
}
