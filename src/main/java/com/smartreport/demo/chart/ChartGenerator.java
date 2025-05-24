package com.smartreport.demo.chart;

import com.smartreport.demo.model.DataRecord;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.*;

import java.util.*;
import java.util.stream.Collectors;

public class ChartGenerator {

    public LineChart<String, Number> createTrendChart(ObservableList<DataRecord> data) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Date");
        yAxis.setLabel("Value");

        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Data Trends Over Time");
        lineChart.setPrefHeight(300);

        Map<String, Double> monthlyData = data.stream()
                .collect(Collectors.groupingBy(
                        record -> record.getDate().substring(0, 7),
                        Collectors.averagingDouble(DataRecord::getValue)
                ));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Average Value");

        monthlyData.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue())));

        lineChart.getData().add(series);
        return lineChart;
    }

    public BarChart<String, Number> createCategoryChart(ObservableList<DataRecord> data) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Category");
        yAxis.setLabel("Total Value");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Values by Category");
        barChart.setPrefHeight(300);

        Map<String, Double> categoryTotals = data.stream()
                .collect(Collectors.groupingBy(
                        DataRecord::getCategory,
                        Collectors.summingDouble(DataRecord::getValue)
                ));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Total Value");

        categoryTotals.forEach((category, total) ->
                series.getData().add(new XYChart.Data<>(category, total)));

        barChart.getData().add(series);
        return barChart;
    }

    public PieChart createDistributionChart(ObservableList<DataRecord> data) {
        Map<String, Double> regionTotals = data.stream()
                .collect(Collectors.groupingBy(
                        DataRecord::getRegion,
                        Collectors.summingDouble(DataRecord::getValue)
                ));

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        regionTotals.forEach((region, total) ->
                pieData.add(new PieChart.Data(region, total)));

        PieChart pieChart = new PieChart(pieData);
        pieChart.setTitle("Distribution by Region");
        pieChart.setPrefHeight(300);
        return pieChart;
    }

    public AreaChart<String, Number> createAreaChart(ObservableList<DataRecord> data) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Date");
        yAxis.setLabel("Cumulative Value");

        AreaChart<String, Number> areaChart = new AreaChart<>(xAxis, yAxis);
        areaChart.setTitle("Cumulative Value Over Time");
        areaChart.setPrefHeight(300);

        Map<String, Double> monthlyData = data.stream()
                .collect(Collectors.groupingBy(
                        record -> record.getDate().substring(0, 7),
                        TreeMap::new,
                        Collectors.summingDouble(DataRecord::getValue)
                ));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Cumulative Value");

        double cumulative = 0;
        for (Map.Entry<String, Double> entry : monthlyData.entrySet()) {
            cumulative += entry.getValue();
            series.getData().add(new XYChart.Data<>(entry.getKey(), cumulative));
        }

        areaChart.getData().add(series);
        return areaChart;
    }
}
