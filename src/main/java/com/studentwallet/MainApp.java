package com.studentwallet;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainApp extends Application {

    private VBox contentArea = new VBox(20); 
    private Label balanceAmount; // Moved here so buttons can update it!

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(20);
        root.getStyleClass().add("root-layout");
        root.setPadding(new Insets(20));

        
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Label logo = new Label("🎓");
        logo.setStyle("-fx-font-size: 24px;");
        Label brand = new Label("EduFinance Hub");
        brand.getStyleClass().add("brand-title");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button menuBtn = new Button("☰");
        menuBtn.getStyleClass().add("menu-button");
        header.getChildren().addAll(logo, brand, spacer, menuBtn);

        
        VBox titleBox = new VBox(5);
        Label title = new Label("Student Wallet");
        title.getStyleClass().add("main-title");
        Label subtitle = new Label("Track your expenses, manage budgets, and stay financially healthy.");
        subtitle.getStyleClass().add("subtitle");
        titleBox.getChildren().addAll(title, subtitle);

        
        HBox tabs = new HBox(10);
        tabs.setAlignment(Pos.CENTER);
        Button btnOverview = new Button("Overview");
        btnOverview.getStyleClass().add("tab-button");
        Button btnSpending = new Button("Spending");
        btnSpending.getStyleClass().add("tab-button");
        Button btnTrends = new Button("Trends");
        btnTrends.getStyleClass().addAll("tab-button", "tab-active");

        
        Button btnAdd = new Button("+ Add R100");
        btnAdd.getStyleClass().add("tab-button");
        Button btnDelete = new Button("- Delete Last");
        btnDelete.getStyleClass().add("tab-button");

        tabs.getChildren().addAll(btnOverview, btnSpending, btnTrends, btnAdd, btnDelete);

        
        btnOverview.setOnAction(e -> {
            updateActiveTab(btnOverview, btnSpending, btnTrends);
            contentArea.getChildren().setAll(createOverviewView());
        });
        btnSpending.setOnAction(e -> {
            updateActiveTab(btnSpending, btnOverview, btnTrends);
            contentArea.getChildren().setAll(createSpendingView());
        });
        btnTrends.setOnAction(e -> {
            updateActiveTab(btnTrends, btnOverview, btnSpending);
            contentArea.getChildren().setAll(createTrendsView());
        });

        
        btnAdd.setOnAction(e -> {
            DatabaseManager.addTransaction(1, 2, "Expense", 100.00, "Quick Expense", "2024-05-10");
            balanceAmount.setText(String.format("R%.2f", DatabaseManager.getTotalBalance()));
            System.out.println("✅ Transaction Added! New Balance: " + balanceAmount.getText());
        });

        btnDelete.setOnAction(e -> {
            DatabaseManager.deleteLastTransaction();
            balanceAmount.setText(String.format("R%.2f", DatabaseManager.getTotalBalance()));
            System.out.println("✅ Transaction Deleted! New Balance: " + balanceAmount.getText());
        });

        
        contentArea.getChildren().setAll(createOverviewView());

        root.getChildren().addAll(header, titleBox, createBalanceCard(), tabs, contentArea);

        Scene scene = new Scene(root, 450, 950);
        scene.getStylesheets().add(getClass().getResource("/com/studentwallet/styles.css").toExternalForm());
        
        primaryStage.setTitle("EduFinance Hub - Student Wallet");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updateActiveTab(Button active, Button... others) {
        active.getStyleClass().add("tab-active");
        for (Button b : others) b.getStyleClass().remove("tab-active");
    }

    private VBox createOverviewView() {
        VBox view = new VBox(15);
        PieChart pieChart = new PieChart();
        pieChart.getData().addAll(
            new PieChart.Data("Accommodation", 3200),
            new PieChart.Data("Food", 1500),
            new PieChart.Data("Transport", 600),
            new PieChart.Data("Education", 800),
            new PieChart.Data("Toiletries", 200)
        );
        pieChart.setTitle("Spending Distribution");
        pieChart.setPrefHeight(250);
        pieChart.getStyleClass().add("chart-card");

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis(0, 300, 100);
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Weekly Spending");
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Mon", 120));
        series.getData().add(new XYChart.Data<>("Tue", 80));
        series.getData().add(new XYChart.Data<>("Wed", 200));
        series.getData().add(new XYChart.Data<>("Thu", 50));
        series.getData().add(new XYChart.Data<>("Fri", 200));
        series.getData().add(new XYChart.Data<>("Sat", 60));
        series.getData().add(new XYChart.Data<>("Sun", 260));
        barChart.getData().add(series);
        barChart.setPrefHeight(250);
        barChart.getStyleClass().add("chart-card");

        view.getChildren().addAll(pieChart, barChart);
        return view;
    }

    private VBox createSpendingView() {
        VBox view = new VBox(15);
        Button btnAddCategory = new Button("Add Category +");
        btnAddCategory.getStyleClass().add("tab-active");
        btnAddCategory.setMaxWidth(Double.MAX_VALUE);

        HBox alertBox = new HBox(10);
        alertBox.setAlignment(Pos.CENTER_LEFT);
        alertBox.setStyle("-fx-background-color: #ffebee; -fx-background-radius: 10px; -fx-padding: 10px; -fx-border-color: #f44336; -fx-border-radius: 10px;");
        Label alertIcon = new Label("⚠️");
        Label alertText = new Label("Over Budget\nAccommodation has exceeded the budget limit.");
        alertText.setStyle("-fx-text-fill: #d32f2f; -fx-font-size: 11px; -fx-font-weight: bold;");
        alertBox.getChildren().addAll(alertIcon, alertText);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        
        grid.add(createCategoryCard("Food & Groceries", "59% Used", "R890", "of R1,500", "R610 remaining", 0.59), 0, 0);
        grid.add(createCategoryCard("Transport", "70% Used", "R420", "of R600", "R180 remaining", 0.70), 1, 0);
        grid.add(createCategoryCard("Education", "56% Used", "R450", "of R800", "R350 remaining", 0.56), 0, 1);
        grid.add(createCategoryCard("Accommodation", "100% Used", "R3200", "of R3000", "R200 Over", 1.0), 1, 1);

        view.getChildren().addAll(btnAddCategory, alertBox, grid);
        return view;
    }

    private VBox createCategoryCard(String title, String percent, String amount, String total, String remaining, double progress) {
        VBox card = new VBox(5);
        card.getStyleClass().add("balance-card");
        card.setPadding(new Insets(10));
        Label lblTitle = new Label(title);
        lblTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");
        Label lblPercent = new Label(percent);
        lblPercent.setStyle("-fx-text-fill: #6a4cba; -fx-font-size: 10px;");
        ProgressBar pb = new ProgressBar(progress);
        pb.setMaxWidth(Double.MAX_VALUE);
        pb.setStyle("-fx-accent: #6a4cba;");
        HBox amountRow = new HBox(5);
        amountRow.setAlignment(Pos.CENTER_LEFT);
        Label lblAmount = new Label(amount);
        lblAmount.setStyle("-fx-font-weight: bold;");
        Label lblTotal = new Label(total);
        lblTotal.setStyle("-fx-text-fill: #777; -fx-font-size: 10px;");
        amountRow.getChildren().addAll(lblAmount, lblTotal);
        Label lblRemaining = new Label(remaining);
        lblRemaining.setStyle("-fx-text-fill: #777; -fx-font-size: 10px;");
        card.getChildren().addAll(lblTitle, lblPercent, pb, amountRow, lblRemaining);
        return card;
    }

    private VBox createTrendsView() {
        VBox chartBox = new VBox(10);
        chartBox.getStyleClass().add("chart-card");
        chartBox.setPadding(new Insets(15));
        Label chartTitle = new Label("Monthly Financial Trends");
        chartTitle.getStyleClass().add("chart-title");
        chartTitle.setAlignment(Pos.CENTER);
        chartTitle.setMaxWidth(Double.MAX_VALUE);

        final NumberAxis yAxis = new NumberAxis(0, 4000, 800);
        final CategoryAxis xAxis = new CategoryAxis();
        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setAnimated(false);
        lineChart.setCreateSymbols(true);
        lineChart.getStyleClass().add("custom-chart");

        XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
        incomeSeries.setName("Income");
        XYChart.Series<String, Number> expenseSeries = new XYChart.Series<>();
        expenseSeries.setName("Expenses");
        XYChart.Series<String, Number> savingsSeries = new XYChart.Series<>();
        savingsSeries.setName("Savings");

        String[] months = {"01", "02", "03", "04", "05"};
        String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May"};
        
        for (int i = 0; i < months.length; i++) {
            double inc = DatabaseManager.getTrendData("Income", months[i]);
            double exp = DatabaseManager.getTrendData("Expense", months[i]);
            double sav = inc - exp;
            incomeSeries.getData().add(new XYChart.Data<>(monthNames[i], inc));
            expenseSeries.getData().add(new XYChart.Data<>(monthNames[i], exp));
            savingsSeries.getData().add(new XYChart.Data<>(monthNames[i], sav));
        }

        lineChart.getData().addAll(incomeSeries, expenseSeries, savingsSeries);
        chartBox.getChildren().addAll(chartTitle, lineChart);
        return chartBox;
    }

    private VBox createBalanceCard() {
        VBox balanceCard = new VBox(15);
        balanceCard.getStyleClass().add("balance-card");
        balanceCard.setPadding(new Insets(20));
        Label availLabel = new Label("Available Balance");
        availLabel.getStyleClass().add("card-subtitle");
        
        double totalBal = DatabaseManager.getTotalBalance();
        balanceAmount = new Label(String.format("R%.2f", totalBal)); // Initialized here
        balanceAmount.getStyleClass().add("balance-amount");

        HBox statsBox = new HBox(20);
        statsBox.setAlignment(Pos.CENTER);
        VBox incomeBox = createStatBox("Monthly Income", String.format("R%.2f", DatabaseManager.getMonthlyIncome()), "↑", "green");
        VBox expenseBox = createStatBox("Monthly Expenses", String.format("R%.2f", DatabaseManager.getMonthlyExpenses()), "↓", "red");
        statsBox.getChildren().addAll(incomeBox, expenseBox);
        balanceCard.getChildren().addAll(availLabel, balanceAmount, statsBox);
        return balanceCard;
    }

    private VBox createStatBox(String title, String amount, String arrow, String color) {
        VBox box = new VBox(5);
        box.getStyleClass().add("stat-box");
        box.setPadding(new Insets(10));
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("stat-title");
        HBox amountRow = new HBox(5);
        amountRow.setAlignment(Pos.CENTER_LEFT);
        Label amountLabel = new Label(amount);
        amountLabel.getStyleClass().add("stat-amount");
        Label arrowLabel = new Label(arrow);
        arrowLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 18px; -fx-font-weight: bold;");
        amountRow.getChildren().addAll(amountLabel, arrowLabel);
        box.getChildren().addAll(titleLabel, amountRow);
        return box;
    }

    public static void main(String[] args) {
        launch(args);
    }
}