package com.clinicapp.clinicsystem;

public class Session {
    private static String username;
    private static String role;

    public static String getUsername() { return username; }
    public static String getRole() { return role; }

    public static void login(String u, String r) {
        username = u;
        role = r;
    }

    public static void logout() {
        username = null;
        role = null;
    }
}
