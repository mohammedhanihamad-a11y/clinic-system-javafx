package com.clinicapp.clinicsystem;

import java.util.ArrayList;
import java.util.List;

public class Patient {

    private String id;
    private String name;
    private int age;

    private List<Visit> visits = new ArrayList<>();

    // Required for Gson
    public Patient() {}

    public Patient(String id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public Patient(String id, String name, int age, String diagnosis) {
        this(id, name, age);
        this.visits.add(new Visit("2026-01-01 10:00", true, diagnosis));
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public List<Visit> getVisits() {
        if (visits == null) visits = new ArrayList<>();
        return visits;
    }

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setVisits(List<Visit> visits) { this.visits = visits; }

    public String getLastVisitDateTime() {
        if (getVisits().isEmpty()) return "-";
        return getVisits().get(getVisits().size() - 1).getDateTime();
    }

    public String getLastDiagnosis() {
        if (getVisits().isEmpty()) return "-";
        String d = getVisits().get(getVisits().size() - 1).getDiagnosis();
        return (d == null || d.trim().isEmpty()) ? "-" : d;
    }
}
