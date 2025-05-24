package com.smartreport.demo;

import com.smartreport.demo.manager.DatabaseManager;
import javafx.application.Application;

class ApplicationLauncher {

    public static void main(String[] args) {
        // Set system properties for better JavaFX performance
        System.setProperty("javafx.animation.fullspeed", "true");
        System.setProperty("prism.lcdtext", "false");

        try {
            // Initialize application components
            System.out.println("Smart Report Generator v2.0");
            System.out.println("Initializing application...");

            // Initialize database
            DatabaseManager.initializeDatabase();

            // Launch JavaFX application
            Application.launch(SmartReportGenerator.class, args);

        } catch (Exception e) {
            System.err.println("Failed to launch application: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
