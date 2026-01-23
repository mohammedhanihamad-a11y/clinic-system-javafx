package com.clinicapp.clinicsystem;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PatientsController {

    // Table
    @FXML private TableView<Patient> tblPatients;

    @FXML private TableColumn<Patient, String> colId;
    @FXML private TableColumn<Patient, String> colName;
    @FXML private TableColumn<Patient, Integer> colAge;
    @FXML private TableColumn<Patient, String> colLastVisit;
    @FXML private TableColumn<Patient, String> colLastDiagnosis;

    // Add patient
    @FXML private TextField txtId;
    @FXML private TextField txtName;
    @FXML private TextField txtAge;

    // Visit date/time (Calendar + time spinners)
    @FXML private DatePicker dateVisit;           // fx:id="dateVisit"
    @FXML private Spinner<Integer> spHour;        // fx:id="spHour"
    @FXML private Spinner<Integer> spMinute;      // fx:id="spMinute"

    // Diagnosis / history
    @FXML private TextField txtDiagnosis;
    @FXML private TextArea txtHistory;
    @FXML private Label lblMsg;

    private final ObservableList<Patient> data = FXCollections.observableArrayList();

    private static final DateTimeFormatter DT_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @FXML
    public void initialize() {

        // ===== Load from JSON =====
        data.setAll(PatientStore.loadAll());

        // Seed demo once (first run)
       // if (data.isEmpty()) {
         //   PatientStore.seedDemoData();
        //    data.setAll(PatientStore.loadAll());
       // }

        // ===== Columns =====
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("age"));

        colLastVisit.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getLastVisitDateTime())
        );

        colLastDiagnosis.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getLastDiagnosis())
        );

        tblPatients.setItems(data);

        // ===== Select patient -> show history =====
        tblPatients.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldV, newV) -> showHistory(newV));

        txtHistory.setEditable(false);
        lblMsg.setText("");

        // ===== DatePicker
        dateVisit.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) return;

                if (date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #eeeeee;");
                }
            }
        });

        // Default date = today
        dateVisit.setValue(LocalDate.now());

        // ===== Time spinners =====
        spHour.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 9));
        spMinute.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));

        spHour.setEditable(true);
        spMinute.setEditable(true);
    }

    @FXML
    private void backDashboard() {
        HelloApplication.showDashboard();
    }

    @FXML
    private void onAddPatient() {
        String id = txtId.getText() == null ? "" : txtId.getText().trim();
        String name = txtName.getText() == null ? "" : txtName.getText().trim();
        String ageStr = txtAge.getText() == null ? "" : txtAge.getText().trim();

        if (id.isEmpty() || name.isEmpty() || ageStr.isEmpty()) {
            lblMsg.setText("Fill ID, Name, Age");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageStr);
        } catch (Exception e) {
            lblMsg.setText("Age must be a number");
            return;
        }

        for (Patient p : data) {
            if (p.getId() != null && p.getId().equalsIgnoreCase(id)) {
                lblMsg.setText("ID already exists");
                return;
            }
        }

        Patient p = new Patient(id, name, age);
        data.add(p);

        PatientStore.saveAll(data);

        txtId.clear();
        txtName.clear();
        txtAge.clear();

        lblMsg.setText("Patient added & saved");
        tblPatients.refresh();
    }

    @FXML
    private void onAddVisit() {
        Patient selected = tblPatients.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblMsg.setText("Select a patient first");
            return;
        }

        LocalDate d = dateVisit.getValue();
        if (d == null) {
            lblMsg.setText("Choose visit date");
            return;
        }

        // Extra safety
        if (d.isBefore(LocalDate.now())) {
            lblMsg.setText("Cannot choose past date");
            return;
        }

        Integer hourVal = spHour.getValue();
        Integer minuteVal = spMinute.getValue();
        if (hourVal == null || minuteVal == null) {
            lblMsg.setText("Choose hour and minute");
            return;
        }

        String dt = d.format(DateTimeFormatter.ISO_LOCAL_DATE)
                + " "
                + String.format("%02d:%02d", hourVal, minuteVal);

        // Add visit (not attended yet)
        selected.getVisits().add(new Visit(dt, false, ""));

        PatientStore.saveAll(data);

        lblMsg.setText("Visit added & saved");
        tblPatients.refresh();
        showHistory(selected);
    }

    @FXML
    private void onMarkAttended() {
        Patient selected = tblPatients.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblMsg.setText("Select a patient first");
            return;
        }

        String diag = txtDiagnosis.getText() == null ? "" : txtDiagnosis.getText().trim();
        if (diag.isEmpty()) {
            lblMsg.setText("Enter diagnosis");
            return;
        }

        if (selected.getVisits() == null || selected.getVisits().isEmpty()) {
            lblMsg.setText("This patient has no visits");
            return;
        }

        // Find last NOT attended visit
        Visit target = null;
        for (int i = selected.getVisits().size() - 1; i >= 0; i--) {
            Visit v = selected.getVisits().get(i);
            if (v != null && !v.isAttended()) {
                target = v;
                break;
            }
        }

        if (target == null) {
            lblMsg.setText("All visits already attended");
            return;
        }

        target.setAttended(true);
        target.setDiagnosis(diag);

        PatientStore.saveAll(data);

        txtDiagnosis.clear();
        lblMsg.setText("Attendance saved");

        tblPatients.refresh();
        showHistory(selected);
    }

    private void showHistory(Patient p) {
        if (p == null) {
            txtHistory.setText("");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Patient: ").append(nullSafe(p.getName()))
                .append(" (").append(nullSafe(p.getId())).append(")\n");
        sb.append("Age: ").append(p.getAge()).append("\n\n");

        if (p.getVisits() == null || p.getVisits().isEmpty()) {
            sb.append("No visits yet.");
            txtHistory.setText(sb.toString());
            return;
        }

        for (int i = 0; i < p.getVisits().size(); i++) {
            Visit v = p.getVisits().get(i);
            if (v == null) continue;

            sb.append("#").append(i + 1).append("  ")
                    .append(nullSafe(v.getDateTime()))
                    .append("  | attended: ").append(v.isAttended() ? "YES" : "NO")
                    .append("\n")
                    .append("Diagnosis: ").append(nullSafe(v.getDiagnosis()))
                    .append("\n\n");
        }

        txtHistory.setText(sb.toString());
    }

    private String nullSafe(String s) {
        return s == null ? "" : s;
    }
}
