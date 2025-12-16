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
 * Класс для создания и отображения графиков статистики игры.
 * <p>
 * Этот класс предоставляет визуальное представление статистических данных игры
 * с использованием JavaFX Chart API. Он организует графики в виде вкладок для
 * удобного просмотра различных аспектов игры:
 * <ul>
 *   <li>Ресурсы игрока</li>
 *   <li>Ресурсы компьютерного противника (AI)</li>
 *   <li>Сравнительные графики между игроком и AI</li>
 * </ul>
 * <p>
 * Графики отображаются в виде линейных диаграмм ({@link LineChart}), которые
 * показывают изменение значений ресурсов и показателей игры по дням.
 * Класс использует данные из объекта {@link GameStatistics} для построения графиков.
 *
 * @author Playstall
 * @version 1.0
 * @see GameStatistics
 * @see LineChart
 * @see TabPane
 * @see <a href="https://openjfx.io/javadoc/17/javafx.controls/javafx/scene/chart/package-summary.html">JavaFX Charts</a>
 * @since 1.0
 */
public class GraphView {
    /**
     * Объект статистики игры, содержащий исторические данные для построения графиков.
     */
    private GameStatistics statistics;

    /**
     * Создает новый экземпляр представления графиков с указанной статистикой.
     *
     * @param statistics объект статистики игры, содержащий данные для визуализации
     * @throws NullPointerException если {@code statistics} равен {@code null}
     */
    public GraphView(GameStatistics statistics) {
        this.statistics = statistics;
    }

    /**
     * Создает и возвращает основной контейнер с графиками статистики.
     * <p>
     * Метод создает интерфейс с вкладками ({@link TabPane}), где каждая вкладка
     * содержит набор связанных графиков:
     * <ul>
     *   <li><strong>Ресурсы игрока</strong>: 4 графика (рис, вода, крестьяне, территории)</li>
     *   <li><strong>Ресурсы компьютера</strong>: 4 аналогичных графика для AI</li>
     *   <li><strong>Сравнение</strong>: 2 сравнительных графика (территории и крестьяне)</li>
     * </ul>
     * Все графики представляются в виде линейных диаграмм, показывающих динамику
     * изменения показателей по дням игры.
     *
     * @return контейнер {@link VBox}, содержащий все графики статистики
     * @see TabPane
     * @see #createPlayerResourceCharts()
     * @see #createAIResourceCharts()
     * @see #createComparisonCharts()
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
     * Создает набор графиков для отображения ресурсов игрока.
     * <p>
     * Включает четыре линейных графика:
     * <ol>
     *   <li>Рост риса у игрока</li>
     *   <li>Рост воды у игрока</li>
     *   <li>Рост крестьян у игрока</li>
     *   <li>Захваченные территории игрока</li>
     * </ol>
     * Каждый график показывает изменение соответствующего показателя
     * по дням игры.
     *
     * @return контейнер {@link VBox} с четырьмя графиками ресурсов игрока
     * @see #createLineChart(String, String, String, List)
     * @see GameStatistics#getPlayerRiceHistory()
     * @see GameStatistics#getPlayerWaterHistory()
     * @see GameStatistics#getPlayerPeasantsHistory()
     * @see GameStatistics#getPlayerTilesHistory()
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
     * Создает набор графиков для отображения ресурсов компьютерного противника (AI).
     * <p>
     * Включает четыре линейных графика, аналогичных графикам игрока:
     * <ol>
     *   <li>Рост риса у компьютера</li>
     *   <li>Рост воды у компьютера</li>
     *   <li>Рост крестьян у компьютера</li>
     *   <li>Захваченные территории компьютера</li>
     * </ol>
     *
     * @return контейнер {@link VBox} с четырьмя графиками ресурсов AI
     * @see #createLineChart(String, String, String, List)
     * @see GameStatistics#getAiRiceHistory()
     * @see GameStatistics#getAiWaterHistory()
     * @see GameStatistics#getAiPeasantsHistory()
     * @see GameStatistics#getAiTilesHistory()
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
     * Создает набор графиков для сравнительного анализа игрока и компьютерного противника.
     * <p>
     * Включает два линейных графика с двумя линиями каждый:
     * <ol>
     *   <li>Сравнение захваченных территорий (игрок vs компьютер)</li>
     *   <li>Сравнение количества крестьян (игрок vs компьютер)</li>
     * </ol>
     * Каждый график позволяет визуально сравнить прогресс игрока и AI
     * по ключевым показателям игры.
     *
     * @return контейнер {@link VBox} с двумя сравнительными графиками
     * @see #createComparisonLineChart(String, String, String, String, List, String, List)
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
     * Создает стандартный линейный график с одним набором данных.
     * <p>
     * Метод настраивает оси, заголовок и добавляет данные в виде серии точек.
     * Ось X представляет дни игры (начиная с 1), ось Y - значения показателя.
     *
     * @param title заголовок графика
     * @param xLabel подпись оси X (обычно "День")
     * @param yLabel подпись оси Y (описывает измеряемый показатель)
     * @param data список значений для отображения, где индекс элемента соответствует
     *             дню игры (0 = день 1, 1 = день 2 и т.д.)
     * @return настроенный объект {@link LineChart} для отображения
     * @throws NullPointerException если {@code title}, {@code xLabel},
     *         {@code yLabel} или {@code data} равны {@code null}
     *
     * @see LineChart
     * @see CategoryAxis
     * @see NumberAxis
     * @see XYChart.Series
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
     * Создает линейный график для сравнения двух наборов данных.
     * <p>
     * Метод создает график с двумя линиями (сериями данных) для визуального
     * сравнения. Обе линии отображаются на одних осях, что позволяет
     * легко сравнивать динамику изменения двух показателей.
     *
     * @param title заголовок графика
     * @param xLabel подпись оси X (обычно "День")
     * @param yLabel подпись оси Y (описывает измеряемый показатель)
     * @param series1Name название первой серии данных
     * @param data1 первый набор данных для сравнения
     * @param series2Name название второй серии данных
     * @param data2 второй набор данных для сравнения
     * @return настроенный объект {@link LineChart} с двумя линиями для сравнения
     * @throws NullPointerException если любой из строковых параметров или списков данных равен {@code null}
     *
     * @see LineChart
     * @see FXCollections#observableArrayList(Object[])
     * @see XYChart.Series
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
