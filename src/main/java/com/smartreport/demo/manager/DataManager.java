package com.smartreport.demo.manager;

import com.smartreport.demo.model.DataRecord;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class DataManager {
    private ObservableList<DataRecord> data;
    private Random random;

    public DataManager() {
        this.data = FXCollections.observableArrayList();
        this.random = new Random();
    }

    public void generateSampleData(int count) {
        data.clear();
        String[] categories = {"Sales", "Marketing", "Operations", "HR", "Finance"};
        String[] regions = {"North", "South", "East", "West", "Central"};

        LocalDate startDate = LocalDate.now().minusMonths(12);

        for (int i = 0; i < count; i++) {
            String id = "REC" + String.format("%04d", i + 1);
            LocalDate recordDate = startDate.plusDays(random.nextInt(365));
            double value = 1000 + random.nextGaussian() * 500;
            String category = categories[random.nextInt(categories.length)];
            String region = regions[random.nextInt(regions.length)];

            data.add(new DataRecord(
                    id,
                    recordDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
                    Math.max(0, value),
                    category,
                    region
            ));
        }

        // Sort by date
        data.sort(Comparator.comparing(DataRecord::getDate));
    }

    public void loadFromCSV(String filePath) throws IOException {
        data.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine(); // Skip header
            int idCounter = 1;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String id = parts.length > 4 ? parts[4] : "REC" + String.format("%04d", idCounter++);
                    String date = parts[0].trim();
                    double value = Double.parseDouble(parts[1].trim());
                    String category = parts[2].trim();
                    String region = parts.length > 3 ? parts[3].trim() : "Unknown";

                    data.add(new DataRecord(id, date, value, category, region));
                }
            }
        }
    }

    public ObservableList<DataRecord> getAllData() {
        return data;
    }

    public List<DataRecord> getDataByCategory(String category) {
        return data.stream()
                .filter(record -> record.getCategory().equals(category))
                .collect(Collectors.toList());
    }

    public List<DataRecord> getDataByDateRange(String startDate, String endDate) {
        return data.stream()
                .filter(record -> record.getDate().compareTo(startDate) >= 0 &&
                        record.getDate().compareTo(endDate) <= 0)
                .collect(Collectors.toList());
    }
}