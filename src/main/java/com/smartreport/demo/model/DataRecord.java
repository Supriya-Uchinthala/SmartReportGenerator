package com.smartreport.demo.model;

import javafx.beans.property.*;

public class DataRecord {
    private final StringProperty id;
    private final StringProperty date;
    private final DoubleProperty value;
    private final StringProperty category;
    private final StringProperty region;

    public DataRecord(String id, String date, double value, String category, String region) {
        this.id = new SimpleStringProperty(id);
        this.date = new SimpleStringProperty(date);
        this.value = new SimpleDoubleProperty(value);
        this.category = new SimpleStringProperty(category);
        this.region = new SimpleStringProperty(region);
    }

    // Property getters
    public StringProperty idProperty() { return id; }
    public StringProperty dateProperty() { return date; }
    public DoubleProperty valueProperty() { return value; }
    public StringProperty categoryProperty() { return category; }
    public StringProperty regionProperty() { return region; }

    // Value getters
    public String getId() { return id.get(); }
    public String getDate() { return date.get(); }
    public double getValue() { return value.get(); }
    public String getCategory() { return category.get(); }
    public String getRegion() { return region.get(); }

    // Setters
    public void setId(String id) { this.id.set(id); }
    public void setDate(String date) { this.date.set(date); }
    public void setValue(double value) { this.value.set(value); }
    public void setCategory(String category) { this.category.set(category); }
    public void setRegion(String region) { this.region.set(region); }
}
