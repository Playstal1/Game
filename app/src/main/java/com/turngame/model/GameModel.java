package main.java.com.turngame.model;

import main.java.com.turngame.util.Constants;
import main.java.com.turngame.util.GameLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * Основной класс игровой модели, управляющий всей логикой игры.
 * <p>
 * Этот класс является ядром игровой логики и отвечает за:
 * <ul>
 *   <li>Управление состоянием игры (текущий день, игроки, карта)</li>
 *   <li>Обработку действий игроков и AI</li>
 *   <li>Проверку условий победы</li>
 *   <li>Ведение игровой статистики</li>
 *   <li>Координацию всех игровых объектов (здания, территории, ресурсы)</li>
 * </ul>
 * <p>
 * Класс использует пошаговую систему: каждый успешный ход игрока завершает текущий день,
 * после чего выполняются действия AI и производятся ресурсы.
 *
 * @author Playstall
 * @version 1.0
 * @see Player
 * @see Tile
 * @see Building
 * @see GameStatistics
 * @see Constants
 * @since 1.0
 */
public class GameModel {
    private int currentDay;
    private Player player;
    private Player ai;
    private Tile[][] map;
    private List<Building> buildings;
    private GameStatistics statistics;
    private boolean gameOver;
    private Player winner;

    /**
     * Создает новую игровую модель и инициализирует игру.
     * <p>
     * Конструктор вызывает метод {@link #initializeGame()} для настройки
     * начального состояния игры.
     */
    public GameModel() {
        initializeGame();
    }

    /**
     * Инициализирует новую игру с начальными значениями.
     * <p>
     * Выполняет следующие действия:
     * <ul>
     *   <li>Устанавливает текущий день в 1</li>
     *   <li>Создает игрока и AI</li>
     *   <li>Создает пустую карту указанных размеров</li>
     *   <li>Инициализирует пустой список зданий</li>
     *   <li>Создает объект для ведения статистики</li>
     *   <li>Сбрасывает флаг завершения игры и победителя</li>
     *   <li>Инициализирует карту с начальными позициями игроков</li>
     * </ul>
     *
     * @see #initializeMap()
     * @see #recordStatistics()
     */
    private void initializeGame() {
        this.currentDay = 1;
        this.player = new Player("Игрок");
        this.ai = new Player("Компьютер");
        this.map = new Tile[Constants.MAP_WIDTH][Constants.MAP_HEIGHT];
        this.buildings = new ArrayList<>();
        this.statistics = new GameStatistics();
        this.gameOver = false;
        this.winner = null;

        initializeMap();
        recordStatistics();

        GameLogger.log("Игра инициализирована. Карта " + Constants.MAP_WIDTH + "x" + Constants.MAP_HEIGHT);
    }

    /**
     * Инициализирует игру из сохраненного состояния.
     * <p>
     * Восстанавливает состояние игры из объекта {@link GameState},
     * включая текущий день, игроков, карту, здания и статистику.
     * После восстановления проверяет условия победы.
     *
     * @param state объект состояния игры, содержащий сохраненные данные
     * @throws NullPointerException если {@code state} равен {@code null}
     * @see GameState
     * @see #checkVictory()
     * @see #recordStatistics()
     */
    public void initializeGame(GameState state) {
        this.currentDay = state.getCurrentDay();
        this.player = state.getPlayer();
        this.ai = state.getAi();
        this.map = state.getMap();
        this.buildings = state.getBuildings();
        recordStatistics();
        checkVictory();

        recordStatistics();

        GameLogger.log("Игра инициализирована. Карта " + Constants.MAP_WIDTH + "x" + Constants.MAP_HEIGHT);
    }

    /**
     * Инициализирует игровую карту начальными значениями.
     * <p>
     * Создает все клетки карты и устанавливает начальные позиции для игрока и AI:
     * <ul>
     *   <li>Игрок начинает в правом нижнем углу ({@code [MAP_WIDTH-1, MAP_HEIGHT-1]})</li>
     *   <li>AI начинает в левом верхнем углу ({@code [0, 0]})</li>
     * </ul>
     * Обе начальные клетки помечаются как контролируемые соответствующими игроками.
     *
     * @see Tile
     * @see Constants#MAP_WIDTH
     * @see Constants#MAP_HEIGHT
     */
    private void initializeMap() {
        for (int x = 0; x < Constants.MAP_WIDTH; x++) {
            for (int y = 0; y < Constants.MAP_HEIGHT; y++) {
                map[x][y] = new Tile(x, y);
            }
        }

        // Начальные позиции
        Tile playerStart = map[Constants.MAP_WIDTH - 1][Constants.MAP_HEIGHT - 1];
        playerStart.setOwner(player);
        playerStart.setControlled(true);
        player.setControlledTiles(1);

        Tile aiStart = map[0][0];
        aiStart.setOwner(ai);
        aiStart.setControlled(true);
        ai.setControlledTiles(1);
    }

    /**
     * Выполняет действие игрока.
     * <p>
     * Метод обрабатывает действия, доступные игроку в его ход.
     * Если действие успешно выполнено, завершает текущий день
     * (вызывается метод {@link #endDay()}).
     *
     * @param action тип действия для выполнения
     * @param params параметры действия (зависят от типа действия):
     *               <ul>
     *                 <li>Для {@link ActionType#EXPLORE_TILE}: координаты x и y (Integer)</li>
     *                 <li>Для других действий: параметры не требуются</li>
     *               </ul>
     * @return {@code true} если действие успешно выполнено,
     *         {@code false} если действие не удалось выполнить или игра завершена
     * @throws NullPointerException если {@code action} равен {@code null}
     * @throws ClassCastException если параметры имеют неверный тип
     * @see ActionType
     * @see #endDay()
     */
    public boolean performPlayerAction(ActionType action, Object... params) {
        if (gameOver) return false;

        boolean success = false;

        switch (action) {
            case COLLECT_WATER:
                success = collectWater(player);
                break;
            case WATER_RICE:
                success = waterRice(player);
                break;
            case EXPLORE_TILE:
                if (params.length >= 2) {
                    int x = (int) params[0];
                    int y = (int) params[1];
                    success = exploreTile(player, x, y);
                }
                break;
            case BUILD_PEASANT_HOUSE:
                success = buildPeasantHouse(player);
                break;
        }

        if (success) {
            endDay();
        }

        return success;
    }

    /**
     * Действие: сбор воды.
     * <p>
     * Игрок собирает воду в количестве, пропорциональном
     * текущему количеству крестьян у игрока.
     *
     * @param player игрок, выполняющий действие
     * @return всегда {@code true}, так как сбор воды всегда возможен
     * @throws NullPointerException если {@code player} равен {@code null}
     * @see Constants#WATER_PER_DAY
     * @see Player#getResource(ResourceType)
     * @see Player#addResource(ResourceType, int)
     */
    private boolean collectWater(Player player) {
        int waterCollected = player.getResource(ResourceType.PEASANTS) * Constants.WATER_PER_DAY;
        player.addResource(ResourceType.WATER, waterCollected);
        GameLogger.log(player.getName() + " собрал " + waterCollected + " воды");
        return true;
    }

    /**
     * Действие: полив риса.
     * <p>
     * Игрок тратит 10 единиц воды для полива риса.
     * Это действие увеличивает рост риса в следующий день.
     *
     * @param player игрок, выполняющий действие
     * @return {@code true} если у игрока достаточно воды для полива,
     *         {@code false} в противном случае
     * @throws NullPointerException если {@code player} равен {@code null}
     * @see Player#deductResource(ResourceType, int)
     */
    private boolean waterRice(Player player) {
        if (player.deductResource(ResourceType.WATER, 10)) {
            // Полив увеличивает рост риса в следующий день
            GameLogger.log(player.getName() + " полил рис");
            return true;
        }
        return false;
    }

    /**
     * Действие: освоение территории.
     * <p>
     * Игрок пытается захватить указанную клетку на карте.
     * Для успешного захвата должны выполняться условия:
     * <ul>
     *   <li>Клетка находится в пределах карты</li>
     *   <li>У игрока достаточно крестьян для захвата клетки</li>
     *   <li>Клетка еще не контролируется другим игроком</li>
     * </ul>
     *
     * @param player игрок, выполняющий действие
     * @param x координата X клетки для захвата
     * @param y координата Y клетки для захвата
     * @return {@code true} если клетка успешно захвачена,
     *         {@code false} если захват не удался
     * @throws NullPointerException если {@code player} равен {@code null}
     * @see Tile#conquer(Player)
     */
    private boolean exploreTile(Player player, int x, int y) {
        if (x < 0 || x >= Constants.MAP_WIDTH || y < 0 || y >= Constants.MAP_HEIGHT) {
            return false;
        }

        Tile tile = map[x][y];
        if (tile.conquer(player)) {
            GameLogger.log(player.getName() + " освоил территорию (" + x + "," + y + ")");
            return true;
        }
        return false;
    }

    /**
     * Действие: постройка дома крестьянина.
     * <p>
     * Игрок строит дом крестьянина, если у него достаточно ресурсов:
     * <ul>
     *   <li>Рис: {@link Constants#HOUSE_RICE_COST}</li>
     *   <li>Вода: {@link Constants#HOUSE_WATER_COST}</li>
     *   <li>Крестьяне: {@link Constants#HOUSE_PEASANT_COST}</li>
     * </ul>
     * После постройки здание добавляется в список зданий игрока.
     *
     * @param player игрок, выполняющий действие
     * @return {@code true} если здание успешно построено,
     *         {@code false} если у игрока недостаточно ресурсов
     * @throws NullPointerException если {@code player} равен {@code null}
     * @see Building
     * @see Building.BuildingType#PEASANT_HOUSE
     */
    private boolean buildPeasantHouse(Player player) {
        Building.BuildingType type = Building.BuildingType.PEASANT_HOUSE;

        if (player.deductResource(ResourceType.RICE, type.getRiceCost()) &&
                player.deductResource(ResourceType.WATER, type.getWaterCost()) &&
                player.deductResource(ResourceType.PEASANTS, type.getPeasantCost())) {

            buildings.add(new Building(type, player));
            GameLogger.log(player.getName() + " построил " + type.getName());
            return true;
        }
        return false;
    }

    /**
     * Завершает текущий игровой день.
     * <p>
     * Выполняет следующие действия в конце каждого дня:
     * <ul>
     *   <li>Производство ресурсов во всех зданиях</li>
     *   <li>Рост риса у обоих игроков</li>
     *   <li>Ход компьютерного игрока (AI)</li>
     *   <li>Запись статистики</li>
     *   <li>Проверка условий победы</li>
     *   <li>Увеличение счетчика дней</li>
     * </ul>
     *
     * @see Building#producePeasants()
     * @see Constants#RICE_GROWTH_PER_DAY
     * @see #performAITurn()
     * @see #recordStatistics()
     * @see #checkVictory()
     */
    private void endDay() {
        // Производство в зданиях
        buildings.forEach(Building::producePeasants);

        // Рост риса
        player.addResource(ResourceType.RICE, Constants.RICE_GROWTH_PER_DAY);
        ai.addResource(ResourceType.RICE, Constants.RICE_GROWTH_PER_DAY);

        // Ход AI
        performAITurn();

        // Запись статистики
        recordStatistics();

        // Проверка победы
        checkVictory();

        currentDay++;
    }

    /**
     * Выполняет ход компьютерного игрока (AI).
     * <p>
     * В текущей реализации AI выбирает случайное действие из доступных
     * и пытается его выполнить:
     * <ul>
     *   <li>Сбор воды</li>
     *   <li>Полив риса</li>
     *   <li>Освоение случайной территории (до 10 попыток)</li>
     *   <li>Постройка дома крестьянина</li>
     * </ul>
     *
     * @see ActionType
     * @see #collectWater(Player)
     * @see #waterRice(Player)
     * @see #exploreTile(Player, int, int)
     * @see #buildPeasantHouse(Player)
     */
    private void performAITurn() {
        // Простой AI: случайное действие
        ActionType[] possibleActions = ActionType.values();
        ActionType randomAction = possibleActions[(int) (Math.random() * possibleActions.length)];

        switch (randomAction) {
            case COLLECT_WATER:
                collectWater(ai);
                break;
            case WATER_RICE:
                waterRice(ai);
                break;
            case EXPLORE_TILE:
                // Пытаемся освоить случайную соседнюю территорию
                for (int i = 0; i < 10; i++) {
                    int x = (int) (Math.random() * Constants.MAP_WIDTH);
                    int y = (int) (Math.random() * Constants.MAP_HEIGHT);
                    if (exploreTile(ai, x, y)) {
                        break;
                    }
                }
                break;
            case BUILD_PEASANT_HOUSE:
                buildPeasantHouse(ai);
                break;
        }

        GameLogger.log("AI выполнил действие: " + randomAction);
    }

    /**
     * Проверяет условия победы в игре.
     * <p>
     * Игра завершается, когда один из игроков контролирует
     * {@link Constants#WIN_PERCENTAGE} процентов от общей территории.
            * Если условие выполнено, устанавливается флаг {@code gameOver}
     * и определяется победитель.
     *
             * @see Constants#WIN_PERCENTAGE
     * @see Player#getControlledTiles()
     */
    private void checkVictory() {
        int totalTiles = Constants.MAP_WIDTH * Constants.MAP_HEIGHT;
        double playerPercentage = (double) player.getControlledTiles() / totalTiles;
        double aiPercentage = (double) ai.getControlledTiles() / totalTiles;

        if (playerPercentage >= Constants.WIN_PERCENTAGE) {
            gameOver = true;
            winner = player;
            GameLogger.log("Игрок победил! Контролирует " + (playerPercentage * 100) + "% территории");
        } else if (aiPercentage >= Constants.WIN_PERCENTAGE) {
            gameOver = true;
            winner = ai;
            GameLogger.log("Компьютер победил! Контролирует " + (aiPercentage * 100) + "% территории");
        }
    }

    /**
     * Записывает текущую статистику игры.
     * <p>
     * Обновляет объект статистики текущими данными обоих игроков.
     *
     * @see GameStatistics#recordPlayerStats(Player)
     * @see GameStatistics#recordAiStats(Player)
     */
    private void recordStatistics() {
        statistics.recordPlayerStats(player);
        statistics.recordAiStats(ai);
    }

    /**
     * Возвращает текущий игровой день.
     *
     * @return номер текущего дня (начинается с 1)
     */
    public int getCurrentDay() { return currentDay; }

    /**
     * Возвращает объект игрока-человека.
     *
     * @return игрок-человек
     */    public Player getPlayer() { return player; }

    /**
     * Возвращает объект компьютерного игрока (AI).
     *
     * @return компьютерный игрок
     */
    public Player getAi() { return ai; }

    /**
     * Возвращает всю игровую карту.
     *
     * @return двумерный массив клеток карты
     */
    public Tile[][] getMap() { return map; }

    /**
     * Возвращает клетку карты по указанным координатам.
     *
     * @param x координата X (от 0 до {@link Constants#MAP_WIDTH}-1)
     * @param y координата Y (от 0 до {@link Constants#MAP_HEIGHT}-1)
     * @return клетка карты, или {@code null} если координаты вне диапазона
     */
    public Tile getTile(int x, int y) {
        if (x >= 0 && x < Constants.MAP_WIDTH && y >= 0 && y < Constants.MAP_HEIGHT) {
            return map[x][y];
        }
        return null;
    }

    /**
     * Возвращает объект статистики игры.
     *
     * @return текущая статистика игры
     */
    public GameStatistics getStatistics() { return statistics; }

    /**
     * Проверяет, завершена ли игра.
     *
     * @return {@code true} если игра завершена, {@code false} в противном случае
     */
    public boolean isGameOver() { return gameOver; }

    /**
     * Возвращает победителя игры.
     *
     * @return победитель игры, или {@code null} если игра еще не завершена
     */
    public Player getWinner() { return winner; }

    /**
     * Возвращает список всех зданий в игре.
     *
     * @return список зданий (может быть пустым, но не {@code null})
     */
    public List<Building> getBuildings() { return buildings; }

    /**
     * Перечисление возможных действий игрока в игре.
     * <p>
     * Каждое действие имеет человеко-читаемое описание
     * для отображения в пользовательском интерфейсе.
     */
    public enum ActionType {
        COLLECT_WATER("Набрать воды"),
        WATER_RICE("Полить рис"),
        EXPLORE_TILE("Освоить территорию"),
        BUILD_PEASANT_HOUSE("Построить дом крестьянина");

        private final String description;

        ActionType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}