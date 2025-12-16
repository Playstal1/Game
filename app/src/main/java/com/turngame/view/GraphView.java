package main.java.com.turngame.view;

import main.java.com.turngame.model.GameStatistics;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.chart.*;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Класс для отображения графиков статистики
 */
public class GraphView {
    private GameStatistics statistics;

    public GraphView(GameStatistics statistics) {
        this.statistics = statistics;
    }

    /**
     * Создает представление с графиками
     */
    public VBox createGraphView() {
        VBox container = new VBox(10);
        container.setPadding(new Insets(10));

        TabPane tabPane = new TabPane();

        // Вкладка с ресурсами игрока
        Tab playerTab = new Tab("Ресурсы игрока");
        playerTab.setContent(createPlayerResourceCharts());
        playerTab.setClosable(false);

        // Вкладка с ресурсами AI
        Tab aiTab = new Tab("Ресурсы компьютера");
        aiTab.setContent(createAIResourceCharts());
        aiTab.setClosable(false);

        // Вкладка с сравнением
        Tab comparisonTab = new Tab("Сравнение");
        comparisonTab.setContent(createComparisonCharts());
        comparisonTab.setClosable(false);

        tabPane.getTabs().addAll(playerTab, aiTab, comparisonTab);

        container.getChildren().add(tabPane);
        return container;
    }

    /**
     * Создает графики ресурсов игрока
     */
    private VBox createPlayerResourceCharts() {
        VBox chartsBox = new VBox(20);

        LineChart<String, Number> riceChart = createLineChart(
                "Рост риса у игрока",
                "День",
                "Количество риса",
                statistics.getPlayerRiceHistory()
        );

        LineChart<String, Number> waterChart = createLineChart(
                "Рост воды у игрока",
                "День",
                "Количество воды",
                statistics.getPlayerWaterHistory()
        );

        LineChart<String, Number> peasantsChart = createLineChart(
                "Рост крестьян у игрока",
                "День",
                "Количество крестьян",
                statistics.getPlayerPeasantsHistory()
        );

        LineChart<String, Number> tilesChart = createLineChart(
                "Захваченные территории игрока",
                "День",
                "Количество территорий",
                statistics.getPlayerTilesHistory()
        );

        chartsBox.getChildren().addAll(riceChart, waterChart, peasantsChart, tilesChart);
        return chartsBox;
    }

    /**
     * Создает графики ресурсов AI
     */
    private VBox createAIResourceCharts() {
        VBox chartsBox = new VBox(20);

        LineChart<String, Number> riceChart = createLineChart(
                "Рост риса у компьютера",
                "День",
                "Количество риса",
                statistics.getAiRiceHistory()
        );

        LineChart<String, Number> waterChart = createLineChart(
                "Рост воды у компьютера",
                "День",
                "Количество воды",
                statistics.getAiWaterHistory()
        );

        LineChart<String, Number> peasantsChart = createLineChart(
                "Рост крестьян у компьютера",
                "День",
                "Количество крестьян",
                statistics.getAiPeasantsHistory()
        );

        LineChart<String, Number> tilesChart = createLineChart(
                "Захваченные территории компьютера",
                "День",
                "Количество территорий",
                statistics.getAiTilesHistory()
        );

        chartsBox.getChildren().addAll(riceChart, waterChart, peasantsChart, tilesChart);
        return chartsBox;
    }

    /**
     * Создает графики сравнения игрока и AI
     */
    private VBox createComparisonCharts() {
        VBox chartsBox = new VBox(20);

        // Сравнение территорий
        LineChart<String, Number> territoryChart = createComparisonLineChart(
                "Сравнение захваченных территорий",
                "День",
                "Количество территорий",
                "Игрок",
                statistics.getPlayerTilesHistory(),
                "Компьютер",
                statistics.getAiTilesHistory()
        );

        // Сравнение крестьян
        LineChart<String, Number> peasantsChart = createComparisonLineChart(
                "Сравнение количества крестьян",
                "День",
                "Количество крестьян",
                "Игрок",
                statistics.getPlayerPeasantsHistory(),
                "Компьютер",
                statistics.getAiPeasantsHistory()
        );

        chartsBox.getChildren().addAll(territoryChart, peasantsChart);
        return chartsBox;
    }

    /**
     * Создает линейный график
     */
    private LineChart<String, Number> createLineChart(String title, String xLabel,
                                                      String yLabel, List<Integer> data) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel(xLabel);
        yAxis.setLabel(yLabel);

        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle(title);
        lineChart.setLegendVisible(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        for (int i = 0; i < data.size(); i++) {
            series.getData().add(new XYChart.Data<>(String.valueOf(i + 1), data.get(i)));
        }

        lineChart.getData().add(series);
        lineChart.setPrefHeight(300);

        return lineChart;
    }

    /**
     * Создает линейный график для сравнения двух наборов данных
     */
    private LineChart<String, Number> createComparisonLineChart(String title, String xLabel,
                                                                String yLabel,
                                                                String series1Name, List<Integer> data1,
                                                                String series2Name, List<Integer> data2) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel(xLabel);
        yAxis.setLabel(yLabel);

        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle(title);

        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName(series1Name);

        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName(series2Name);

        int maxLength = Math.max(data1.size(), data2.size());
        for (int i = 0; i < maxLength; i++) {
            if (i < data1.size()) {
                series1.getData().add(new XYChart.Data<>(String.valueOf(i + 1), data1.get(i)));
            }
            if (i < data2.size()) {
                series2.getData().add(new XYChart.Data<>(String.valueOf(i + 1), data2.get(i)));
            }
        }

        ObservableList<XYChart.Series<String, Number>> seriesList =
                FXCollections.observableArrayList(series1, series2);
        lineChart.setData(seriesList);
        lineChart.setPrefHeight(400);

        return lineChart;
    }
}
