package com.smartreport.demo;

import com.smartreport.demo.model.DataRecord;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class SmartFilterSystem {

    public static class FilterCriteria {
        private String dateFrom;
        private String dateTo;
        private Set<String> categories;
        private Set<String> regions;
        private Double minValue;
        private Double maxValue;
        private String searchText;

        public FilterCriteria() {
            this.categories = new HashSet<>();
            this.regions = new HashSet<>();
        }

        // Getters and setters
        public String getDateFrom() { return dateFrom; }
        public void setDateFrom(String dateFrom) { this.dateFrom = dateFrom; }
        public String getDateTo() { return dateTo; }
        public void setDateTo(String dateTo) { this.dateTo = dateTo; }
        public Set<String> getCategories() { return categories; }
        public Set<String> getRegions() { return regions; }
        public Double getMinValue() { return minValue; }
        public void setMinValue(Double minValue) { this.minValue = minValue; }
        public Double getMaxValue() { return maxValue; }
        public void setMaxValue(Double maxValue) { this.maxValue = maxValue; }
        public String getSearchText() { return searchText; }
        public void setSearchText(String searchText) { this.searchText = searchText; }
    }

    public static ObservableList<DataRecord> applyFilters(ObservableList<DataRecord> data,
                                                          FilterCriteria criteria) {
        return data.stream()
                .filter(record -> matchesDateRange(record, criteria.getDateFrom(), criteria.getDateTo()))
                .filter(record -> matchesCategories(record, criteria.getCategories()))
                .filter(record -> matchesRegions(record, criteria.getRegions()))
                .filter(record -> matchesValueRange(record, criteria.getMinValue(), criteria.getMaxValue()))
                .filter(record -> matchesSearchText(record, criteria.getSearchText()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    private static boolean matchesDateRange(DataRecord record, String dateFrom, String dateTo) {
        if (dateFrom == null && dateTo == null) return true;

        String recordDate = record.getDate();
        if (recordDate == null) return false;

        if (dateFrom != null && recordDate.compareTo(dateFrom) < 0) return false;
        if (dateTo != null && recordDate.compareTo(dateTo) > 0) return false;

        return true;
    }

    private static boolean matchesCategories(DataRecord record, Set<String> categories) {
        return categories.isEmpty() || categories.contains(record.getCategory());
    }

    private static boolean matchesRegions(DataRecord record, Set<String> regions) {
        return regions.isEmpty() || regions.contains(record.getRegion());
    }

    private static boolean matchesValueRange(DataRecord record, Double minValue, Double maxValue) {
        double value = record.getValue();
        if (minValue != null && value < minValue) return false;
        if (maxValue != null && value > maxValue) return false;
        return true;
    }

    private static boolean matchesSearchText(DataRecord record, String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) return true;

        String text = searchText.toLowerCase();
        return record.getId().toLowerCase().contains(text) ||
                record.getCategory().toLowerCase().contains(text) ||
                record.getRegion().toLowerCase().contains(text) ||
                record.getDate().contains(text);
    }

    public static List<String> getUniqueCategories(ObservableList<DataRecord> data) {
        return data.stream()
                .map(DataRecord::getCategory)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public static List<String> getUniqueRegions(ObservableList<DataRecord> data) {
        return data.stream()
                .map(DataRecord::getRegion)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
}