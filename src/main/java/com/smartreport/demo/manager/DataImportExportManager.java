package com.smartreport.demo.manager;

import com.smartreport.demo.export.ReportExporter;
import com.smartreport.demo.model.DataRecord;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class DataImportExportManager {

    public static class ImportResult {
        private boolean success;
        private int recordsImported;
        private List<String> errors;
        private List<String> warnings;

        public ImportResult() {
            this.errors = new ArrayList<>();
            this.warnings = new ArrayList<>();
        }

        // Getters and setters
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public int getRecordsImported() { return recordsImported; }
        public void setRecordsImported(int recordsImported) { this.recordsImported = recordsImported; }
        public List<String> getErrors() { return errors; }
        public List<String> getWarnings() { return warnings; }

        public void addError(String error) { errors.add(error); }
        public void addWarning(String warning) { warnings.add(warning); }
    }

    public static ImportResult importFromCSV(String filePath, DataManager dataManager) {
        ImportResult result = new ImportResult();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String headerLine = reader.readLine();
            if (headerLine == null) {
                result.addError("File is empty");
                return result;
            }

            String[] headers = headerLine.split(",");
            validateHeaders(headers, result);

            String line;
            int lineNumber = 1;
            int recordsProcessed = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                try {
                    DataRecord record = parseCSVLine(line, lineNumber);
                    if (record != null) {
                        dataManager.getAllData().add(record);
                        recordsProcessed++;
                    }
                } catch (Exception e) {
                    result.addError(String.format("Line %d: %s", lineNumber, e.getMessage()));
                }
            }

            result.setRecordsImported(recordsProcessed);
            result.setSuccess(recordsProcessed > 0);

        } catch (IOException e) {
            result.addError("Failed to read file: " + e.getMessage());
        }

        return result;
    }

    private static void validateHeaders(String[] headers, ImportResult result) {
        Set<String> requiredHeaders = Set.of("date", "value", "category");
        Set<String> foundHeaders = Arrays.stream(headers)
                .map(String::toLowerCase)
                .map(String::trim)
                .collect(Collectors.toSet());

        for (String required : requiredHeaders) {
            if (!foundHeaders.contains(required)) {
                result.addWarning("Missing recommended header: " + required);
            }
        }
    }

    private static DataRecord parseCSVLine(String line, int lineNumber) throws Exception {
        String[] parts = line.split(",");

        if (parts.length < 3) {
            throw new Exception("Insufficient columns (minimum 3 required)");
        }

        String id = parts.length > 4 ? parts[4].trim() : "REC" + String.format("%04d", lineNumber);
        String date = parts[0].trim();

        double value;
        try {
            value = Double.parseDouble(parts[1].trim());
        } catch (NumberFormatException e) {
            throw new Exception("Invalid numeric value: " + parts[1]);
        }

        String category = parts[2].trim();
        String region = parts.length > 3 ? parts[3].trim() : "Unknown";

        // Validate date format
        if (!isValidDate(date)) {
            throw new Exception("Invalid date format: " + date);
        }

        return new DataRecord(id, date, value, category, region);
    }

    private static boolean isValidDate(String date) {
        // Check common date formats
        return date.matches("\\d{4}-\\d{2}-\\d{2}") ||
                date.matches("\\d{2}/\\d{2}/\\d{4}") ||
                date.matches("\\d{2}-\\d{2}-\\d{4}");
    }

    public static void exportAdvancedReport(ObservableList<DataRecord> data, String insights,
                                            String format, String filePath) throws IOException {
        ReportExporter exporter = new ReportExporter();

        switch (format.toLowerCase()) {
            case "json":
                exporter.exportToJSON(data, insights, filePath);
                break;
            case "csv":
                exporter.exportToCSV(data, filePath);
                break;
            case "excel":
                exporter.exportToExcel(data, insights, filePath);
                break;
            default:
                throw new IllegalArgumentException("Unsupported format: " + format);
        }
    }
}