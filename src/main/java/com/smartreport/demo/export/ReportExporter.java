package com.smartreport.demo.export;

import com.smartreport.demo.model.DataRecord;
import javafx.collections.ObservableList;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ReportExporter {

    public void exportToCSV(ObservableList<DataRecord> data, String filePath) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("ID,Date,Value,Category,Region");

            for (DataRecord record : data) {
                writer.printf("%s,%s,%.2f,%s,%s%n",
                        record.getId(),
                        record.getDate(),
                        record.getValue(),
                        record.getCategory(),
                        record.getRegion()
                );
            }
        }
    }

    public void exportToExcel(ObservableList<DataRecord> data, String insights, String filePath) throws IOException {
        // Basic Excel export using CSV format with .xlsx extension
        // For full Excel support, integrate Apache POI library

        String csvContent = generateCSVContent(data);

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath.replace(".xlsx", ".csv")))) {
            writer.println("Smart Report Generator - Excel Export");
            writer.println("Generated on: " + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
            writer.println();
            writer.println("INSIGHTS:");
            writer.println(insights.replace("\n", " | "));
            writer.println();
            writer.println("DATA:");
            writer.print(csvContent);
        }

        System.out.println("Note: Excel file exported as CSV format. For native .xlsx support, integrate Apache POI library.");
    }

    private String generateCSVContent(ObservableList<DataRecord> data) {
        StringBuilder content = new StringBuilder();
        content.append("ID,Date,Value,Category,Region\n");

        for (DataRecord record : data) {
            content.append(String.format("%s,%s,%.2f,%s,%s%n",
                    record.getId(),
                    record.getDate(),
                    record.getValue(),
                    record.getCategory(),
                    record.getRegion()
            ));
        }

        return content.toString();
    }

    public void exportToJSON(ObservableList<DataRecord> data, String insights, String filePath) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("{");
            writer.println("  \"reportInfo\": {");
            writer.printf("    \"generatedOn\": \"%s\",%n", LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
            writer.printf("    \"totalRecords\": %d,%n", data.size());
            writer.println("    \"generator\": \"Smart Report Generator\"");
            writer.println("  },");

            writer.println("  \"insights\": [");
            String[] insightLines = insights.split("\n");
            for (int i = 0; i < insightLines.length; i++) {
                String line = insightLines[i].trim();
                if (!line.isEmpty()) {
                    writer.printf("    \"%s\"%s%n",
                            line.replace("\"", "\\\""),
                            (i < insightLines.length - 1) ? "," : "");
                }
            }
            writer.println("  ],");

            writer.println("  \"data\": [");
            for (int i = 0; i < data.size(); i++) {
                DataRecord record = data.get(i);
                writer.println("    {");
                writer.printf("      \"id\": \"%s\",%n", record.getId());
                writer.printf("      \"date\": \"%s\",%n", record.getDate());
                writer.printf("      \"value\": %.2f,%n", record.getValue());
                writer.printf("      \"category\": \"%s\",%n", record.getCategory());
                writer.printf("      \"region\": \"%s\"%n", record.getRegion());
                writer.printf("    }%s%n", (i < data.size() - 1) ? "," : "");
            }
            writer.println("  ]");
            writer.println("}");
        }
    }

    public void exportReport(javafx.collections.ObservableList<DataRecord> allData, String absolutePath) {
    }
}