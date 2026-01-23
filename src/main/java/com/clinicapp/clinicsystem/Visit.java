package com.clinicapp.clinicsystem;

public class Visit {

    private String dateTime;     // "2026-01-16 18:30"
    private boolean attended;    // true/false
    private String diagnosis;    // text

    // Required for Gson
    public Visit() {}

    public Visit(String dateTime, boolean attended, String diagnosis) {
        this.dateTime = dateTime;
        this.attended = attended;
        this.diagnosis = diagnosis;
    }

    public String getDateTime() { return dateTime; }
    public boolean isAttended() { return attended; }
    public String getDiagnosis() { return diagnosis; }

    public void setDateTime(String dateTime) { this.dateTime = dateTime; }
    public void setAttended(boolean attended) { this.attended = attended; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
}
