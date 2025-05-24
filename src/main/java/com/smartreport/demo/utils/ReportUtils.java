package com.smartreport.demo.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class ReportUtils {

    public static String formatNumber(double number) {
        if (number >= 1_000_000) {
            return String.format("%.1fM", number / 1_000_000);
        } else if (number >= 1_000) {
            return String.format("%.1fK", number / 1_000);
        } else {
            return String.format("%.2f", number);
        }
    }

    public static String formatPercentage(double value) {
        return String.format("%.1f%%", value);
    }

    public static String capitalizeFirst(String text) {
        if (text == null || text.isEmpty()) return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    public static LocalDate parseDate(String dateString) {
        DateTimeFormatter[] formatters = {
                DateTimeFormatter.ISO_LOCAL_DATE,
                DateTimeFormatter.ofPattern("MM/dd/yyyy"),
                DateTimeFormatter.ofPattern("dd-MM-yyyy")
        };

        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDate.parse(dateString, formatter);
            } catch (Exception ignored) {
            }
        }

        throw new IllegalArgumentException("Unable to parse date: " + dateString);
    }

    public static String getCurrentTimestamp() {
        return LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + " " +
                java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
}