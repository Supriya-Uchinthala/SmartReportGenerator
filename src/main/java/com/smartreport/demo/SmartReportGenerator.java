package com.smartreport.demo;

import com.smartreport.demo.analysis.TrendAnalyzer;
import com.smartreport.demo.analysis.TrendResult;
import com.smartreport.demo.chart.ChartGenerator;
import com.smartreport.demo.export.ReportExporter;
import com.smartreport.demo.manager.DataManager;
import com.smartreport.demo.model.DataRecord;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class SmartReportGenerator extends Application {
    private DataManager dataManager;
    private ChartGenerator chartGenerator;
    private TrendAnalyzer trendAnalyzer;
    private ReportExporter reportExporter;
    private TableView<DataRecord> dataTable;
    private VBox chartContainer;
    private TextArea insightsArea;

    @Override
    public void start(Stage primaryStage) {
        initializeComponents();

        primaryStage.setTitle("Smart Report Generator");
        primaryStage.setScene(createMainScene());
        primaryStage.setMaximized(true);
        primaryStage.show();

        // Load sample data
        loadSampleData();
    }

    private void initializeComponents() {
        dataManager = new DataManager();
        chartGenerator = new ChartGenerator();
        trendAnalyzer = new TrendAnalyzer();
        reportExporter = new ReportExporter();

        dataTable = new TableView<>();
        chartContainer = new VBox(10);
        insightsArea = new TextArea();
        insightsArea.setEditable(false);
        insightsArea.setPrefRowCount(10);
    }

    private Scene createMainScene() {
        BorderPane root = new BorderPane();

        // Top Menu Bar
        MenuBar menuBar = createMenuBar();
        root.setTop(menuBar);

        // Left Panel - Data Controls
        VBox leftPanel = createLeftPanel();
        root.setLeft(leftPanel);

        // Center - Charts and Analysis
        TabPane centerPane = createCenterPane();
        root.setCenter(centerPane);

        // Right Panel - Insights
        VBox rightPanel = createRightPanel();
        root.setRight(rightPanel);

        return new Scene(root, 1400, 900);
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        MenuItem loadData = new MenuItem("Load CSV Data");
        MenuItem exportReport = new MenuItem("Export Report");
        MenuItem exit = new MenuItem("Exit");

        loadData.setOnAction(e -> loadDataFromFile());
        exportReport.setOnAction(e -> exportReport());
        exit.setOnAction(e -> System.exit(0));

        fileMenu.getItems().addAll(loadData, exportReport, new SeparatorMenuItem(), exit);

        Menu analyzeMenu = new Menu("Analyze");
        MenuItem generateCharts = new MenuItem("Generate Charts");
        MenuItem runTrendAnalysis = new MenuItem("Run Trend Analysis");
        MenuItem generateInsights = new MenuItem("Generate Insights");

        generateCharts.setOnAction(e -> generateAllCharts());
        runTrendAnalysis.setOnAction(e -> performTrendAnalysis());
        generateInsights.setOnAction(e -> generateInsights());

        analyzeMenu.getItems().addAll(generateCharts, runTrendAnalysis, generateInsights);

        menuBar.getMenus().addAll(fileMenu, analyzeMenu);
        return menuBar;
    }

    private VBox createLeftPanel() {
        VBox leftPanel = new VBox(10);
        leftPanel.setPadding(new Insets(10));
        leftPanel.setPrefWidth(300);
        leftPanel.setStyle("-fx-background-color: #f5f5f5;");

        Label dataLabel = new Label("Data Management");
        dataLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Button loadBtn = new Button("Load Data");
        Button generateBtn = new Button("Generate Sample Data");
        Button analyzeBtn = new Button("Analyze Data");

        loadBtn.setOnAction(e -> loadDataFromFile());
        generateBtn.setOnAction(e -> generateSampleData());
        analyzeBtn.setOnAction(e -> {
            generateAllCharts();
            performTrendAnalysis();
            generateInsights();
        });

        // Data table
        setupDataTable();

        leftPanel.getChildren().addAll(
                dataLabel,
                loadBtn,
                generateBtn,
                analyzeBtn,
                new Separator(),
                new Label("Data Preview:"),
                dataTable
        );

        return leftPanel;
    }

    private TabPane createCenterPane() {
        TabPane tabPane = new TabPane();

        // Charts Tab
        Tab chartsTab = new Tab("Charts");
        chartsTab.setClosable(false);
        ScrollPane chartScroll = new ScrollPane(chartContainer);
        chartScroll.setFitToWidth(true);
        chartsTab.setContent(chartScroll);

        // Raw Data Tab
        Tab dataTab = new Tab("Raw Data");
        dataTab.setClosable(false);
        TableView<DataRecord> fullDataTable = new TableView<>();
        setupFullDataTable(fullDataTable);
        dataTab.setContent(new ScrollPane(fullDataTable));

        tabPane.getTabs().addAll(chartsTab, dataTab);
        return tabPane;
    }

    private VBox createRightPanel() {
        VBox rightPanel = new VBox(10);
        rightPanel.setPadding(new Insets(10));
        rightPanel.setPrefWidth(350);
        rightPanel.setStyle("-fx-background-color: #f9f9f9;");

        Label insightsLabel = new Label("AI Insights & Trends");
        insightsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Button refreshInsights = new Button("Refresh Insights");
        refreshInsights.setOnAction(e -> generateInsights());

        rightPanel.getChildren().addAll(
                insightsLabel,
                refreshInsights,
                new Separator(),
                new Label("Analysis Results:"),
                insightsArea
        );

        return rightPanel;
    }

    private void setupDataTable() {
        TableColumn<DataRecord, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(data -> data.getValue().dateProperty());

        TableColumn<DataRecord, Number> valueCol = new TableColumn<>("Value");
        valueCol.setCellValueFactory(data -> data.getValue().valueProperty());

        TableColumn<DataRecord, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(data -> data.getValue().categoryProperty());

        dataTable.getColumns().addAll(dateCol, valueCol, categoryCol);
        dataTable.setPrefHeight(200);
    }

    private void setupFullDataTable(TableView<DataRecord> table) {
        TableColumn<DataRecord, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> data.getValue().idProperty());

        TableColumn<DataRecord, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(data -> data.getValue().dateProperty());

        TableColumn<DataRecord, Number> valueCol = new TableColumn<>("Value");
        valueCol.setCellValueFactory(data -> data.getValue().valueProperty());

        TableColumn<DataRecord, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(data -> data.getValue().categoryProperty());

        TableColumn<DataRecord, String> regionCol = new TableColumn<>("Region");
        regionCol.setCellValueFactory(data -> data.getValue().regionProperty());

        table.getColumns().addAll(idCol, dateCol, valueCol, categoryCol, regionCol);

        // Use JavaFX ObservableList here
        ObservableList<DataRecord> allData = FXCollections.observableArrayList(dataManager.getAllData());
        table.setItems(allData);
    }

    private void loadSampleData() {
        generateSampleData();
        updateDataTable();
    }

    private void generateSampleData() {
        dataManager.generateSampleData(200);
        updateDataTable();
        showAlert("Success", "Sample data generated successfully!");
    }

    private void loadDataFromFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load CSV Data");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                dataManager.loadFromCSV(file.getAbsolutePath());
                updateDataTable();
                showAlert("Success", "Data loaded successfully!");
            } catch (Exception e) {
                showAlert("Error", "Failed to load data: " + e.getMessage());
            }
        }
    }

    private void updateDataTable() {
        List<DataRecord> recent = dataManager.getAllData().stream()
                .limit(10)
                .collect(Collectors.toList());

        ObservableList<DataRecord> recentData = FXCollections.observableArrayList(recent);
        dataTable.setItems(recentData);
    }

    private void generateAllCharts() {
        chartContainer.getChildren().clear();

        // Use JavaFX ObservableList
        ObservableList<DataRecord> fxList = FXCollections.observableArrayList(dataManager.getAllData());

        LineChart<String, Number> lineChart = chartGenerator.createTrendChart(fxList);
        BarChart<String, Number> barChart = chartGenerator.createCategoryChart(fxList);
        PieChart pieChart = chartGenerator.createDistributionChart(fxList);
        AreaChart<String, Number> areaChart = chartGenerator.createAreaChart(fxList);

        chartContainer.getChildren().addAll(lineChart, barChart, pieChart, areaChart);
    }


    private void performTrendAnalysis() {
        if (dataManager.getAllData().isEmpty()) {
            showAlert("Info", "No data to analyze. Please load or generate data first.");
            return;
        }

        List<TrendResult> results = trendAnalyzer.analyzeTrends(dataManager.getAllData());
        StringBuilder sb = new StringBuilder();

        for (TrendResult result : results) {
            double confidence = ((Double) result.getConfidence()) * 100;
            sb.append(String.format("Category: %s\nTrend: %s\nConfidence: %.2f%%\n\n",
                    result.getCategory(),
                    result.getTrend(),
                    confidence));
        }

        insightsArea.setText(sb.toString());
    }

    private void generateInsights() {
        if (dataManager.getAllData().isEmpty()) {
            insightsArea.setText("No data available for generating insights.");
            return;
        }

        // Example simple insight generation
        String insights = "Top Categories:\n";

        List<String> topCategories = dataManager.getAllData().stream()
                .collect(Collectors.groupingBy(DataRecord::getCategory, Collectors.counting()))
                .entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .limit(3)
                .map(entry -> entry.getKey() + " (" + entry.getValue() + " records)")
                .collect(Collectors.toList());

        for (String cat : topCategories) {
            insights += "- " + cat + "\n";
        }

        insightsArea.setText(insights);
    }

    private void exportReport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Report");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                reportExporter.exportReport(dataManager.getAllData(), file.getAbsolutePath());
                showAlert("Success", "Report exported to " + file.getAbsolutePath());
            } catch (Exception e) {
                showAlert("Error", "Failed to export report: " + e.getMessage());
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
