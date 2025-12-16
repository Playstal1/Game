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
 * Основной класс представления игры, отвечающий за пользовательский интерфейс.
 * <p>
 * Этот класс является центральным компонентом представления (View) в архитектуре MVC.
 * Он отвечает за:
 * <ul>
 *   <li>Создание и управление всем пользовательским интерфейсом игры</li>
 *   <li>Отображение игрового состояния (ресурсы, карта, день)</li>
 *   <li>Обработку пользовательских действий (клики, нажатия кнопок)</li>
 *   <li>Взаимодействие с контроллером ({@link GameController}) для выполнения игровых действий</li>
 *   <li>Отображение диалоговых окон, меню и информации о игре</li>
 * </ul>
 * <p>
 * Интерфейс построен с использованием JavaFX и организован в виде {@link BorderPane} с
 * пятью основными областями:
 * <ul>
 *   <li><strong>Верхняя панель</strong>: Меню и информация о текущем дне</li>
 *   <li><strong>Центральная область</strong>: Игровое поле в виде сетки клеток</li>
 *   <li><strong>Левая панель</strong>: Информация об игроках и правила игры</li>
 *   <li><strong>Правая панель</strong>: Панель действий игрока</li>
 *   <li><strong>Нижняя панель</strong>: Строка статуса и прогресс игры</li>
 * </ul>
 * <p>
 * Класс реализует паттерн Наблюдатель для обновления интерфейса при изменении игрового состояния.
 *
 * @author Playstall
 * @version 1.0
 * @see GameController
 * @see BorderPane
 * @see <a href="https://openjfx.io/javadoc/17/javafx.graphics/javafx/scene/control/package-summary.html">JavaFX Controls</a>
 * @since 1.0
 */
public class GameView {
    /**
     * Контроллер игры, обеспечивающий связь между представлением и моделью.
     */
    private GameController gameController;
    /**
     * Основной макет пользовательского интерфейса, использующий компоновку BorderPane.
     */
    private BorderPane mainLayout;


    /**
     * Метка для отображения текущего игрового дня.
     */
    private Label dayLabel;

    /**
     * Метка для отображения ресурсов игрока.
     */
    private Label playerResourcesLabel;

    /**
     * Метка для отображения ресурсов компьютерного противника (AI).
     */
    private Label aiResourcesLabel;

    /**
     * Метка для отображения текущего статуса игры.
     */
    private Label statusLabel;

    /**
     * Сетка для отображения игрового поля.
     */
    private GridPane gameGrid;

    /**
     * Вертикальная панель для кнопок действий.
     */
    private VBox actionButtons;

    /**
     * Вертикальная панель для отображения информации.
     */
    private VBox infoPanel;

    /**
     * Компонент для отображения графиков статистики (опциональный).
     */
    private GraphView graphView;

    /**
     * Создает новое представление игры и инициализирует пользовательский интерфейс.
     * <p>
     * Конструктор создает новый контроллер игры и вызывает метод {@link #initializeUI()}
     * для настройки всех компонентов интерфейса.
     *
     * @see GameController
     * @see #initializeUI()
     */
    public GameView() {
        this.gameController = new GameController();
        initializeUI();
    }

    /**
     * Инициализирует все компоненты пользовательского интерфейса.
     * <p>
     * Метод создает и настраивает все элементы интерфейса, распределяя их по областям
     * основного макета {@link BorderPane}. После инициализации вызывает метод
     * {@link #updateView()} для первоначального заполнения данных.
     *
     * @see BorderPane
     * @see #createTopPanel()
     * @see #createGameCenter()
     * @see #createActionPanel()
     * @see #createInfoPanel()
     * @see #createStatusBar()
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
     * Создает верхнюю панель интерфейса, содержащую меню и информацию о текущем дне.
     * <p>
     * Включает следующие меню:
     * <ul>
     *   <li><strong>Игра</strong>: Новая игра, сохранение/загрузка, выход</li>
     *   <li><strong>Статистика</strong>: Просмотр графиков</li>
     *   <li><strong>Помощь</strong>: Информация об игре</li>
     * </ul>
     *
     * @return элемент {@link Node}, содержащий верхнюю панель интерфейса
     * @see MenuBar
     * @see Menu
     * @see MenuItem
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
     * Создает центральную область интерфейса, содержащую игровое поле.
     * <p>
     * Центральная область включает заголовок и сетку клеток игрового поля,
     * размещенную внутри {@link ScrollPane} для поддержки прокрутки при
     * больших размерах карты.
     *
     * @return элемент {@link Node}, содержащий центральную область с игровым полем
     * @see GridPane
     * @see ScrollPane
     * @see #updateGameGrid()
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
     * Обновляет отображение игровой сетки на основе текущего состояния игры.
     * <p>
     * Метод полностью очищает текущую сетку и заново создает все клетки,
     * используя данные из игровой модели. Каждая клетка представляется
     * в виде {@link Button} с соответствующим стилем и обработчиком событий.
     *
     * @see #createTileButton(int, int, Tile)
     * @see GridPane#getChildren()
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
     * Создает кнопку для представления одной клетки игрового поля.
     * <p>
     * Внешний вид кнопки зависит от состояния клетки:
     * <ul>
     *   <li><strong>Контролируется игроком</strong>: синий фон, текст "P"</li>
     *   <li><strong>Контролируется AI</strong>: красный фон, текст "AI"</li>
     *   <li><strong>Свободная клетка</strong>: серый фон, количество требуемых крестьян</li>
     * </ul>
     * Каждая кнопка имеет всплывающую подсказку с подробной информацией
     * и обработчик клика для возможности захвата территории.
     *
     * @param x координата X клетки
     * @param y координата Y клетки
     * @param tile объект клетки ({@link Tile}) для отображения
     * @return настроенная кнопка {@link Button} для представления клетки
     * @throws NullPointerException если {@code tile} равен {@code null}
     *
     * @see Button
     * @see Tooltip
     * @see #handleTileClick(int, int)
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
     * Обрабатывает событие клика по клетке игрового поля.
     * <p>
     * Если клетка свободна (не контролируется), показывает диалоговое окно
     * с подтверждением захвата территории. При подтверждении пытается
     * выполнить действие {@link ActionType#EXPLORE_TILE} через контроллер.
     *
     * @param x координата X клетки, по которой был произведен клик
     * @param y координата Y клетки, по которой был произведен клик
     *
     * @see Alert
     * @see GameController#performAction(ActionType, Object...)
     * @see #updateView()
     * @see #showError(String, String)
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
     * Создает правую панель интерфейса с кнопками действий игрока.
     * <p>
     * Панель содержит следующие действия:
     * <ul>
     *   <li><strong>Набрать воды</strong>: Сбор воды крестьянами</li>
     *   <li><strong>Полить рис</strong>: Полив риса (требуется 10 воды)</li>
     *   <li><strong>Построить дом крестьянина</strong>: Строительство дома для производства крестьян</li>
     *   <li><strong>Завершить день</strong>: Пропуск хода (эквивалентен сбору воды)</li>
     * </ul>
     *
     * @return элемент {@link Node}, содержащий панель действий
     * @see Button
     * @see VBox
     * @see GameController#performAction(ActionType, Object...)
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
     * Создает левую панель интерфейса с информацией об игроках и правилами игры.
     * <p>
     * Панель содержит:
     * <ul>
     *   <li>Ресурсы игрока (динамически обновляемые)</li>
     *   <li>Ресурсы компьютерного противника (AI)</li>
     *   <li>Правила игры в текстовом виде</li>
     * </ul>
     *
     * @return элемент {@link Node}, содержащий информационную панель
     * @see Label
     * @see TextArea
     * @see Separator
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
     * Создает нижнюю панель интерфейса (строку статуса).
     * <p>
     * Строка статуса отображает текущее состояние игры и прогресс
     * захвата территории в виде {@link ProgressBar}.
     *
     * @return элемент {@link Node}, содержащий строку статуса
     * @see Label
     * @see ProgressBar
     * @see HBox
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
     * Обновляет все элементы пользовательского интерфейса на основе текущего состояния игры.
     * <p>
     * Метод синхронизирует отображение интерфейса с состоянием игровой модели,
     * вызывая методы обновления для всех компонентов:
     * <ul>
     *   <li>Текущий игровой день</li>
     *   <li>Ресурсы игрока и AI</li>
     *   <li>Игровое поле</li>
     *   <li>Статус игры (активна/завершена)</li>
     *   <li>Доступность кнопок действий</li>
     * </ul>
     *
     * @see #updateGameGrid()
     * @see #updateButtonStates()
     * @see GameController#getCurrentDay()
     * @see GameController#getPlayerResourcesInfo()
     * @see GameController#getAIResourcesInfo()
     * @see GameController#isGameOver()
     * @see GameController#getWinner()
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
     * Обновляет состояния кнопок действий в зависимости от доступности ресурсов.
     * <p>
     * Метод проверяет условия для каждого действия и отключает/включает
     * соответствующие кнопки:
     * <ul>
     *   <li>Кнопка "Полить рис" отключается, если недостаточно воды</li>
     *   <li>Кнопка "Построить дом крестьянина" отключается, если недостаточно ресурсов</li>
     * </ul>
     *
     * @see GameController#canWaterRice()
     * @see GameController#canBuildPeasantHouse()
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
     * Отключает все кнопки действий в правой панели.
     * <p>
     * Используется при завершении игры для предотвращения дальнейших действий.
     *
     * @see #enableActions()
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
     * Включает все кнопки действий в правой панели.
     * <p>
     * Используется при начале новой игры или загрузке существующей.
     *
     * @see #disableActions()
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
     * Начинает новую игру после подтверждения пользователем.
     * <p>
     * Показывает диалоговое окно с подтверждением, так как текущий прогресс
     * будет потерян. При подтверждении вызывает метод {@link GameController#newGame()}
     * и обновляет интерфейс.
     *
     * @see Alert
     * @see GameController#newGame()
     * @see #updateView()
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
     * Сохраняет текущую игру в стандартный файл.
     * <p>
     * Использует стандартный путь к файлу сохранения из {@link Constants#SAVE_FILE}.
     * Показывает информационное сообщение о результате операции.
     *
     * @see GameController#saveGame(String)
     * @see Constants#SAVE_FILE
     * @see #showInfo(String, String)
     * @see #showError(String, String)
     */
    private void saveGame() {
        if (gameController.saveGame(Constants.SAVE_FILE)) {
            showInfo("Игра сохранена", "Игра успешно сохранена в файл: " + Constants.SAVE_FILE);
        } else {
            showError("Ошибка сохранения", "Не удалось сохранить игру.");
        }
    }

    /**
     * Загружает игру из стандартного файла.
     * <p>
     * Использует стандартный путь к файлу сохранения из {@link Constants#SAVE_FILE}.
     * Показывает информационное сообщение о результате операции и обновляет интерфейс.
     *
     * @see GameController#loadGame(String)
     * @see Constants#SAVE_FILE
     * @see #updateView()
     * @see #showInfo(String, String)
     * @see #showError(String, String)
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
     * Открывает окно с графиками статистики игры.
     * <p>
     * Создает новое окно ({@link Stage}) и отображает в нем графики,
     * построенные с использованием класса {@link GraphView}.
     *
     * @see GraphView
     * @see Stage
     * @see Scene
     */
    private void showStatistics() {
        graphView = new GraphView(gameController.getGameModel().getStatistics());
        Stage statsStage = new Stage();
        statsStage.setTitle("Статистика игры");
        statsStage.setScene(new Scene(graphView.createGraphView(), 800, 600));
        statsStage.show();
    }

    /**
     * Показывает диалоговое окно с информацией об игре.
     * <p>
     * Содержит описание игры, информацию о разработчике и основные функции.
     *
     * @see Alert
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
     * Показывает информационное диалоговое окно.
     *
     * @param title заголовок окна
     * @param message текст сообщения
     * @see Alert
     */
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Показывает диалоговое окно с сообщением об ошибке.
     *
     * @param title заголовок окна
     * @param message текст сообщения об ошибке
     * @see Alert
     */
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Возвращает основной макет пользовательского интерфейса.
     * <p>
     * Этот метод используется главным классом приложения ({@link main.java.com.turngame.Main})
     * для получения корневого элемента сцены.
     *
     * @return основной макет интерфейса {@link BorderPane}
     */
    public BorderPane createMainView() {
        return mainLayout;
    }
}
