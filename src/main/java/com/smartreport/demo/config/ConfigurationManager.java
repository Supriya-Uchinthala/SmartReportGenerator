package com.smartreport.demo.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

class ConfigurationManager {
    private static final String CONFIG_FILE = "report_generator_config.properties";
    private Properties properties;

    public ConfigurationManager() {
        properties = new Properties();
        loadDefaultSettings();
        loadConfigFile();
    }

    private void loadDefaultSettings() {
        properties.setProperty("chart.theme", "modern");
        properties.setProperty("export.default.format", "html");
        properties.setProperty("data.validation.enabled", "true");
        properties.setProperty("insights.auto.generate", "true");
        properties.setProperty("performance.monitoring", "false");
        properties.setProperty("sample.data.size", "200");
        properties.setProperty("chart.animation.enabled", "true");
        properties.setProperty("export.include.charts", "false");
    }

    private void loadConfigFile() {
        File configFile = new File(CONFIG_FILE);
        if (configFile.exists()) {
            try (FileInputStream fis = new FileInputStream(configFile)) {
                properties.load(fis);
            } catch (IOException e) {
                System.err.println("Failed to load configuration file: " + e.getMessage());
            }
        }
    }

    public void saveConfiguration() {
        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {
            properties.store(fos, "Smart Report Generator Configuration");
        } catch (IOException e) {
            System.err.println("Failed to save configuration: " + e.getMessage());
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    public boolean getBooleanProperty(String key) {
        return Boolean.parseBoolean(properties.getProperty(key, "false"));
    }

    public int getIntProperty(String key, int defaultValue) {
        try {
            return Integer.parseInt(properties.getProperty(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
