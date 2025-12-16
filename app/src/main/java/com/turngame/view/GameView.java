package main.java.com.turngame.view;

import main.java.com.turngame.controller.GameController;
import main.java.com.turngame.model.GameModel;
import main.java.com.turngame.model.GameModel.ActionType;
import main.java.com.turngame.model.Tile;
import main.java.com.turngame.util.Constants;
import main.java.com.turngame.util.GameLogger;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Optional;

/**
 * Основной класс представления игры
 */
public class GameView {
    private GameController gameController;
    private BorderPane mainLayout;

    // Компоненты интерфейса
    private Label dayLabel;
    private Label playerResourcesLabel;
    private Label aiResourcesLabel;
    private Label statusLabel;
    private GridPane gameGrid;
    private VBox actionButtons;
    private VBox infoPanel;

    // Графики
    private GraphView graphView;

    public GameView() {
        this.gameController = new GameController();
        initializeUI();
    }

    /**
     * Инициализирует пользовательский интерфейс
     */
    private void initializeUI() {
        mainLayout = new BorderPane();
        mainLayout.getStyleClass().add("root");

        // Верхняя панель
        mainLayout.setTop(createTopPanel());

        // Центр - игровое поле
        mainLayout.setCenter(createGameCenter());

        // Правая панель - действия
        mainLayout.setRight(createActionPanel());

        // Левая панель - информация
        mainLayout.setLeft(createInfoPanel());

        // Нижняя панель - статус
        mainLayout.setBottom(createStatusBar());

        updateView();
    }

    /**
     * Создает верхнюю панель с меню
     */
    private Node createTopPanel() {
        MenuBar menuBar = new MenuBar();

        // Меню "Игра"
        Menu gameMenu = new Menu("Игра");
        MenuItem newGameItem = new MenuItem("Новая игра");
        MenuItem saveGameItem = new MenuItem("Сохранить игру");
        MenuItem loadGameItem = new MenuItem("Загрузить игру");
        MenuItem exitItem = new MenuItem("Выход");

        newGameItem.setOnAction(e -> startNewGame());
        saveGameItem.setOnAction(e -> saveGame());
        loadGameItem.setOnAction(e -> loadGame());
        exitItem.setOnAction(e -> Platform.exit());

        gameMenu.getItems().addAll(newGameItem, new SeparatorMenuItem(),
                saveGameItem, loadGameItem, new SeparatorMenuItem(), exitItem);

        // Меню "Статистика"
        Menu statsMenu = new Menu("Статистика");
        MenuItem showStatsItem = new MenuItem("Показать графики");
        showStatsItem.setOnAction(e -> showStatistics());
        statsMenu.getItems().add(showStatsItem);

        // Меню "Помощь"
        Menu helpMenu = new Menu("Помощь");
        MenuItem aboutItem = new MenuItem("Об игре");
        aboutItem.setOnAction(e -> showAboutDialog());
        helpMenu.getItems().add(aboutItem);

        menuBar.getMenus().addAll(gameMenu, statsMenu, helpMenu);

        // Панель дня
        HBox dayPanel = new HBox(10);
        dayPanel.setPadding(new Insets(10));
        dayPanel.setAlignment(Pos.CENTER_LEFT);

        dayLabel = new Label();
        dayLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        dayPanel.getChildren().addAll(dayLabel);

        VBox topPanel = new VBox();
        topPanel.getChildren().addAll(menuBar, dayPanel);

        return topPanel;
    }

    /**
     * Создает центральную панель с игровым полем
     */
    private Node createGameCenter() {
        VBox centerPanel = new VBox(10);
        centerPanel.setPadding(new Insets(10));
        centerPanel.setAlignment(Pos.CENTER);

        // Заголовок
        Label titleLabel = new Label("Игровое поле");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Создаем игровую сетку
        gameGrid = new GridPane();
        gameGrid.setHgap(2);
        gameGrid.setVgap(2);
        gameGrid.setAlignment(Pos.CENTER);

        // Заполняем сетку
        updateGameGrid();

        centerPanel.getChildren().addAll(titleLabel, gameGrid);

        return new ScrollPane(centerPanel);
    }

    /**
     * Обновляет игровую сетку
     */
    private void updateGameGrid() {
        gameGrid.getChildren().clear();

        // Создаем клетки
        for (int x = 0; x < Constants.MAP_WIDTH; x++) {
            for (int y = 0; y < Constants.MAP_HEIGHT; y++) {
                Tile tile = gameController.getGameModel().getTile(x, y);
                Button tileButton = createTileButton(x, y, tile);
                gameGrid.add(tileButton, x, y);
            }
        }
    }

    /**
     * Создает кнопку для клетки
     */
    private Button createTileButton(int x, int y, Tile tile) {
        Button button = new Button();
        button.setMinSize(50, 50);
        button.setMaxSize(50, 50);
        button.getStyleClass().add("grid-cell");

        // Определяем стиль в зависимости от владельца
        if (tile != null) {
            if (tile.isControlled()) {
                if (tile.getOwner() == gameController.getGameModel().getPlayer()) {
                    button.getStyleClass().add("player-cell");
                    button.setText("P");
                } else if (tile.getOwner() == gameController.getGameModel().getAi()) {
                    button.getStyleClass().add("ai-cell");
                    button.setText("AI");
                }
            } else {
                button.getStyleClass().add("neutral-cell");
                button.setText(tile.getRequiredPeasants() + "");
            }
        }

        // Всплывающая подсказка
        Tooltip tooltip = new Tooltip(gameController.getTileInfo(x, y));
        Tooltip.install(button, tooltip);

        // Обработчик клика
        button.setOnAction(e -> handleTileClick(x, y));

        return button;
    }

    /**
     * Обрабатывает клик по клетке
     */
    private void handleTileClick(int x, int y) {
        Tile tile = gameController.getGameModel().getTile(x, y);
        if (tile != null && !tile.isControlled()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Освоение территории");
            alert.setHeaderText("Клетка [" + x + "," + y + "]");
            alert.setContentText("Требуется крестьян: " + tile.getRequiredPeasants() +
                    "\nОсвоить эту территорию?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (gameController.performAction(ActionType.EXPLORE_TILE, x, y)) {
                    updateView();
                } else {
                    showError("Не удалось освоить территорию",
                            "Недостаточно крестьян для освоения этой территории.");
                }
            }
        }
    }

    /**
     * Создает панель действий
     */
    private Node createActionPanel() {
        actionButtons = new VBox(10);
        actionButtons.setPadding(new Insets(10));
        actionButtons.setAlignment(Pos.TOP_CENTER);
        actionButtons.getStyleClass().add("resource-panel");

        Label titleLabel = new Label("Действия игрока");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Кнопка: Набрать воды
        Button collectWaterButton = new Button("Набрать воды");
        collectWaterButton.setOnAction(e -> {
            if (gameController.performAction(ActionType.COLLECT_WATER)) {
                updateView();
            }
        });
        collectWaterButton.setMaxWidth(Double.MAX_VALUE);

        // Кнопка: Полить рис
        Button waterRiceButton = new Button("Полить рис");
        waterRiceButton.setOnAction(e -> {
            if (gameController.performAction(ActionType.WATER_RICE)) {
                updateView();
            } else {
                showError("Недостаточно воды", "Для полива риса требуется 10 единиц воды.");
            }
        });
        waterRiceButton.setMaxWidth(Double.MAX_VALUE);

        // Кнопка: Построить дом крестьянина
        Button buildHouseButton = new Button("Построить дом крестьянина");
        buildHouseButton.setOnAction(e -> {
            if (gameController.performAction(ActionType.BUILD_PEASANT_HOUSE)) {
                updateView();
            } else {
                showError("Недостаточно ресурсов",
                        "Для постройки дома требуется:\n" +
                                "Рис: " + Constants.HOUSE_RICE_COST + "\n" +
                                "Вода: " + Constants.HOUSE_WATER_COST + "\n" +
                                "Крестьяне: " + Constants.HOUSE_PEASANT_COST);
            }
        });
        buildHouseButton.setMaxWidth(Double.MAX_VALUE);

        // Кнопка: Пропустить ход
        Button skipTurnButton = new Button("Завершить день");
        skipTurnButton.setOnAction(e -> {
            // Пропуск хода = сбор воды
            if (gameController.performAction(ActionType.COLLECT_WATER)) {
                updateView();
            }
        });
        skipTurnButton.setMaxWidth(Double.MAX_VALUE);
        skipTurnButton.setStyle("-fx-background-color: #ff9800;");

        actionButtons.getChildren().addAll(
                titleLabel,
                collectWaterButton,
                waterRiceButton,
                buildHouseButton,
                skipTurnButton
        );

        return actionButtons;
    }

    /**
     * Создает информационную панель
     */
    private Node createInfoPanel() {
        infoPanel = new VBox(10);
        infoPanel.setPadding(new Insets(10));
        infoPanel.setAlignment(Pos.TOP_CENTER);
        infoPanel.getStyleClass().add("resource-panel");

        // Ресурсы игрока
        Label playerTitle = new Label("Ресурсы игрока");
        playerTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2E7D32;");

        playerResourcesLabel = new Label();
        playerResourcesLabel.setStyle("-fx-font-size: 12px;");

        // Ресурсы AI
        Label aiTitle = new Label("Ресурсы компьютера");
        aiTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #C62828;");

        aiResourcesLabel = new Label();
        aiResourcesLabel.setStyle("-fx-font-size: 12px;");

        // Правила игры
        Label rulesTitle = new Label("Правила игры");
        rulesTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        TextArea rulesText = new TextArea(
                "Цель игры: захватить 50% территории.\n\n" +
                        "Действия за ход:\n" +
                        "1. Набрать воды - каждый крестьянин собирает 10 воды\n" +
                        "2. Полить рис - требуется 10 воды, увеличивает рост риса\n" +
                        "3. Освоить территорию - кликните на клетку поля\n" +
                        "4. Построить дом - производит крестьян каждый день\n\n" +
                        "В конце дня:\n" +
                        "- Дома производят крестьян\n" +
                        "- Растет рис\n" +
                        "- AI делает свой ход"
        );
        rulesText.setEditable(false);
        rulesText.setWrapText(true);
        rulesText.setMaxHeight(200);

        infoPanel.getChildren().addAll(
                playerTitle, playerResourcesLabel,
                new Separator(),
                aiTitle, aiResourcesLabel,
                new Separator(),
                rulesTitle, rulesText
        );

        return infoPanel;
    }

    /**
     * Создает строку статуса
     */
    private Node createStatusBar() {
        HBox statusBar = new HBox(10);
        statusBar.setPadding(new Insets(10));
        statusBar.setAlignment(Pos.CENTER_LEFT);
        statusBar.setStyle("-fx-background-color: #e0e0e0;");

        statusLabel = new Label("Готов к игре");
        statusLabel.setStyle("-fx-font-size: 12px;");

        // Прогресс захвата территории
        ProgressBar progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(200);

        statusBar.getChildren().addAll(statusLabel, progressBar);

        return statusBar;
    }

    /**
     * Обновляет все элементы представления
     */
    private void updateView() {
        // Обновляем день
        dayLabel.setText("День: " + gameController.getCurrentDay());

        // Обновляем ресурсы
        playerResourcesLabel.setText(gameController.getPlayerResourcesInfo());
        aiResourcesLabel.setText(gameController.getAIResourcesInfo());

        // Обновляем игровое поле
        updateGameGrid();

        // Обновляем статус
        if (gameController.isGameOver()) {
            statusLabel.setText("Игра окончена! Победитель: " +
                    gameController.getWinner().getName());
            disableActions();
        } else {
            statusLabel.setText("Ход игрока. Выберите действие.");
            enableActions();
        }

        // Обновляем доступность кнопок
        updateButtonStates();
    }

    /**
     * Обновляет состояния кнопок
     */
    private void updateButtonStates() {
        // Можно добавить логику для отключения кнопок при недостатке ресурсов
        Node actionPanel = mainLayout.getRight();
        if (actionPanel instanceof VBox) {
            for (Node node : ((VBox) actionPanel).getChildren()) {
                if (node instanceof Button) {
                    Button button = (Button) node;
                    String text = button.getText();

                    if (text.equals("Полить рис")) {
                        button.setDisable(!gameController.canWaterRice());
                    } else if (text.equals("Построить дом крестьянина")) {
                        button.setDisable(!gameController.canBuildPeasantHouse());
                    }
                }
            }
        }
    }

    /**
     * Отключает кнопки действий
     */
    private void disableActions() {
        Node actionPanel = mainLayout.getRight();
        if (actionPanel instanceof VBox) {
            for (Node node : ((VBox) actionPanel).getChildren()) {
                if (node instanceof Button) {
                    node.setDisable(true);
                }
            }
        }
    }

    /**
     * Включает кнопки действий
     */
    private void enableActions() {
        Node actionPanel = mainLayout.getRight();
        if (actionPanel instanceof VBox) {
            for (Node node : ((VBox) actionPanel).getChildren()) {
                if (node instanceof Button) {
                    node.setDisable(false);
                }
            }
        }
    }

    /**
     * Начинает новую игру
     */
    private void startNewGame() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Новая игра");
        alert.setHeaderText("Начать новую игру?");
        alert.setContentText("Текущий прогресс будет потерян.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            gameController.newGame();
            updateView();
            GameLogger.log("Начата новая игра");
        }
    }

    /**
     * Сохраняет игру
     */
    private void saveGame() {
        if (gameController.saveGame(Constants.SAVE_FILE)) {
            showInfo("Игра сохранена", "Игра успешно сохранена в файл: " + Constants.SAVE_FILE);
        } else {
            showError("Ошибка сохранения", "Не удалось сохранить игру.");
        }
    }

    /**
     * Загружает игру
     */
    private void loadGame() {
        if (gameController.loadGame(Constants.SAVE_FILE)) {
            updateView();
            showInfo("Игра загружена", "Игра успешно загружена из файла.");
        } else {
            showError("Ошибка загрузки", "Не удалось загрузить игру. Файл не найден или поврежден.");
        }
    }

    /**
     * Показывает статистику
     */
    private void showStatistics() {
        graphView = new GraphView(gameController.getGameModel().getStatistics());
        Stage statsStage = new Stage();
        statsStage.setTitle("Статистика игры");
        statsStage.setScene(new Scene(graphView.createGraphView(), 800, 600));
        statsStage.show();
    }

    /**
     * Показывает диалог "Об игре"
     */
    private void showAboutDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Об игре");
        alert.setHeaderText("Пошаговая стратегия: Вода и Рис");
        alert.setContentText(
                "Курсовая работа по Java с использованием JavaFX\n\n" +
                        "Тема: Разработка простой пошаговой игры\n\n" +
                        "Функции:\n" +
                        "- Экономическая модель\n" +
                        "- Искусственный интеллект\n" +
                        "- Сохранение/загрузка игры\n" +
                        "- Графики статистики\n\n" +
                        "Автор: Playstall"
        );
        alert.showAndWait();
    }

    /**
     * Показывает информационное сообщение
     */
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Показывает сообщение об ошибке
     */
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Возвращает главный макет
     */
    public BorderPane createMainView() {
        return mainLayout;
    }
}
