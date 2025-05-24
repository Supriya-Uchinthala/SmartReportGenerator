package com.smartreport.demo.analysis;

import com.smartreport.demo.model.DataRecord;
import javafx.collections.ObservableList;

import java.util.*;
import java.util.stream.Collectors;

public class TrendAnalyzer {

    public List<TrendResult> analyzeTrends(ObservableList<DataRecord> data) {
        if (data.isEmpty()) {
            return List.of(new TrendResult("No Data", "None", 0.0, "No Data", 0.0, 0.0, List.of("No data available")));
        }

        // Group data by category
        Map<String, List<DataRecord>> dataByCategory = data.stream()
                .collect(Collectors.groupingBy(DataRecord::getCategory));

        List<TrendResult> results = new ArrayList<>();

        for (Map.Entry<String, List<DataRecord>> entry : dataByCategory.entrySet()) {
            String category = entry.getKey();
            List<DataRecord> categoryData = entry.getValue();

            List<Double> values = categoryData.stream()
                    .map(DataRecord::getValue)
                    .collect(Collectors.toList());

            String trendDirection = calculateTrendDirection(values);
            double growthRate = calculateGrowthRate(values);
            double volatility = calculateVolatility(values);

            // Use arbitrary confidence logic (can be improved)
            double confidence = Math.min(100, Math.max(0, 100 - volatility));

            List<String> insights = generateTrendInsights(categoryData, trendDirection, growthRate, volatility);

            TrendResult result = new TrendResult(
                    category,
                    trendDirection,
                    confidence,
                    trendDirection,
                    growthRate,
                    volatility,
                    insights
            );
            results.add(result);
        }

        return results;
    }

    private String calculateTrendDirection(List<Double> values) {
        if (values.size() < 2) return "Insufficient Data";
        double first = values.get(0);
        double last = values.get(values.size() - 1);
        double change = (last - first) / first * 100;

        if (Math.abs(change) < 5) return "Stable";
        return change > 0 ? "Upward" : "Downward";
    }

    private double calculateGrowthRate(List<Double> values) {
        if (values.size() < 2) return 0.0;
        double first = values.get(0);
        double last = values.get(values.size() - 1);
        return (last - first) / first * 100;
    }

    private double calculateVolatility(List<Double> values) {
        double mean = values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double variance = values.stream()
                .mapToDouble(v -> Math.pow(v - mean, 2))
                .average()
                .orElse(0.0);
        return Math.sqrt(variance);
    }

    private List<String> generateTrendInsights(List<DataRecord> data, String trend, double growthRate, double volatility) {
        List<String> insights = new ArrayList<>();

        insights.add(String.format("The data shows a %s trend with %.2f%% overall change", trend.toLowerCase(), growthRate));

        if (volatility > 500) {
            insights.add("High volatility detected - values fluctuate significantly");
        } else if (volatility < 100) {
            insights.add("Low volatility - values are relatively stable");
        } else {
            insights.add("Moderate volatility observed in the data");
        }

        // Seasonal pattern placeholder
        if (data.size() > 10) {
            insights.add("Sufficient data available for seasonal pattern analysis");
        }

        return insights;
    }
}
