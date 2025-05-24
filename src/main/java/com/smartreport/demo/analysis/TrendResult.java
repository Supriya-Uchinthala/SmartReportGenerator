package com.smartreport.demo.analysis;

import java.util.List;

public class TrendResult {
    private String category;
    private String trend;
    private double confidence;

    private String overallTrend;
    private double growthRate;
    private double volatility;
    private List<String> insights;

    public TrendResult(String category, String trend, double confidence,
                       String overallTrend, double growthRate, double volatility, List<String> insights) {
        this.category = category;
        this.trend = trend;
        this.confidence = confidence;
        this.overallTrend = overallTrend;
        this.growthRate = growthRate;
        this.volatility = volatility;
        this.insights = insights;
    }

    public String getCategory() {
        return category;
    }

    public String getTrend() {
        return trend;
    }

    public double getConfidence() {
        return confidence;
    }

    public String getOverallTrend() {
        return overallTrend;
    }

    public double getGrowthRate() {
        return growthRate;
    }

    public double getVolatility() {
        return volatility;
    }

    public List<String> getInsights() {
        return insights;
    }
}
