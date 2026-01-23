package com.clinicapp.clinicsystem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PatientStore {

    private static final String FILE_NAME = "patients.json";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Type LIST_TYPE = new TypeToken<List<Patient>>() {}.getType();

    private PatientStore() {}

    public static List<Patient> loadAll() {
        try {
            File f = new File(FILE_NAME);
            if (!f.exists()) return new ArrayList<>();

            try (FileReader r = new FileReader(f)) {
                List<Patient> list = GSON.fromJson(r, LIST_TYPE);
                return (list == null) ? new ArrayList<>() : list;
            }
        } catch (Exception e) {

            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void saveAll(List<Patient> patients) {
        try (FileWriter w = new FileWriter(FILE_NAME)) {
            GSON.toJson(patients, LIST_TYPE, w);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void seedDemoData() {
        List<Patient> demo = new ArrayList<>();
        demo.add(new Patient("P001", "Ahmad", 22));
        demo.add(new Patient("P002", "Sara", 19));
        demo.add(new Patient("P003", "Omar", 30));
        saveAll(demo);
    }
}
