package main.java.com.turngame.controller;

import main.java.com.turngame.model.GameModel;
import main.java.com.turngame.model.Tile;
import main.java.com.turngame.util.Constants;
import main.java.com.turngame.util.GameLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Контроллер для управления искусственным интеллектом (AI) в игре.
 * Отвечает за принятие решений AI в соответствии с текущим состоянием игры
 * и реализацией стратегии поведения противника.
 * <p>
 * Основные обязанности класса:
 * <ul>
 *   <li>Определение приоритетных действий для AI на каждом ходу</li>
 *   <li>Управление расширением территории AI</li>
 *   <li>Контроль за ресурсами и их распределением</li>
 *   <li>Принятие стратегических решений на основе игровой ситуации</li>
 * </ul>
 *
 * @author Playstall
 * @version 1.0
 * @see GameModel
 * @see Tile
 * @since 1.0
 */
public class AIController {
    private GameModel gameModel;
    private Random random;

    /**
     * Создает новый контроллер AI с привязкой к игровой модели.
     *
     * @param gameModel игровая модель, с которой будет работать контроллер AI
     * @throws NullPointerException если {@code gameModel} равен null
     */
    public AIController(GameModel gameModel) {
        this.gameModel = gameModel;
        this.random = new Random();
    }

    /**
     * Выполняет ход AI с использованием сложной логики принятия решений.
     * <p>
     * Метод реализует приоритетную цепочку действий:
     * <ol>
     *   <li>Проверка возможности постройки дома крестьян</li>
     *   <li>Попытка захвата доступной территории</li>
     *   <li>Сбор воды при её нехватке</li>
     *   <li>Полив риса при достаточном количестве воды</li>
     *   <li>Сбор воды в остальных случаях</li>
     * </ol>
     *
     * @see #shouldBuildPeasantHouse()
     * @see #tryConquerTerritory()
     */
    public void performAITurn() {
        // Сначала проверяем, можем ли построить дом крестьянина
        if (shouldBuildPeasantHouse()) {
            gameModel.performPlayerAction(GameModel.ActionType.BUILD_PEASANT_HOUSE);
            return;
        }

        // Затем пытаемся освоить территорию
        if (tryConquerTerritory()) {
            return;
        }

        // Если мало воды - собираем воду
        if (gameModel.getAi().getResource(main.java.com.turngame.model.ResourceType.WATER) < 20) {
            gameModel.performPlayerAction(GameModel.ActionType.COLLECT_WATER);
            return;
        }

        // Если достаточно воды и мало риса - поливаем рис
        if (gameModel.getAi().getResource(main.java.com.turngame.model.ResourceType.WATER) >= 10) {
            gameModel.performPlayerAction(GameModel.ActionType.WATER_RICE);
            return;
        }

        // Иначе собираем воду
        gameModel.performPlayerAction(GameModel.ActionType.COLLECT_WATER);
    }

    /**
     * Определяет, нужно ли строить дом крестьянина на основе текущих ресурсов.
     * <p>
     * Решение принимается на основе:
     * <ul>
     *   <li>Наличия достаточного количества ресурсов (рис, вода, крестьяне)</li>
     *   <li>Текущего количества крестьян у AI</li>
     * </ul>
     *
     * @return {@code true} если условия для постройки дома крестьянина выполнены,
     *         {@code false} в противном случае
     * @see Constants#HOUSE_RICE_COST
     * @see Constants#HOUSE_WATER_COST
     * @see Constants#HOUSE_PEASANT_COST
     */
    private boolean shouldBuildPeasantHouse() {
        main.java.com.turngame.model.Player ai = gameModel.getAi();

        boolean hasResources = ai.getResource(main.java.com.turngame.model.ResourceType.RICE) >= Constants.HOUSE_RICE_COST &&
                ai.getResource(main.java.com.turngame.model.ResourceType.WATER) >= Constants.HOUSE_WATER_COST &&
                ai.getResource(main.java.com.turngame.model.ResourceType.PEASANTS) >= Constants.HOUSE_PEASANT_COST;

        // Строим, если ресурсов достаточно и мало крестьян
        return hasResources && ai.getResource(main.java.com.turngame.model.ResourceType.PEASANTS) < 10;
    }

    /**
     * Пытается захватить доступную территорию на игровой карте.
     * <p>
     * Метод сканирует все клетки карты и выбирает доступные для захвата
     * (неконтролируемые), для которых у AI достаточно крестьян.
     * Выбор конкретной клетки осуществляется случайным образом из доступных.
     *
     * @return {@code true} если удалось выполнить действие по захвату территории,
     *         {@code false} если нет доступных клеток для захвата
     * @see Tile#isControlled()
     * @see Tile#getRequiredPeasants()
     */
    private boolean tryConquerTerritory() {
        List<Tile> conquerableTiles = new ArrayList<>();
        Tile[][] map = gameModel.getMap();
        main.java.com.turngame.model.Player ai = gameModel.getAi();

        // Ищем все клетки, которые можно захватить
        for (int x = 0; x < Constants.MAP_WIDTH; x++) {
            for (int y = 0; y < Constants.MAP_HEIGHT; y++) {
                Tile tile = map[x][y];
                if (!tile.isControlled() &&
                        ai.getResource(main.java.com.turngame.model.ResourceType.PEASANTS) >= tile.getRequiredPeasants()) {
                    conquerableTiles.add(tile);
                }
            }
        }

        if (!conquerableTiles.isEmpty()) {
            // Выбираем случайную доступную клетку
            Tile targetTile = conquerableTiles.get(random.nextInt(conquerableTiles.size()));
            return gameModel.performPlayerAction(GameModel.ActionType.EXPLORE_TILE,
                    targetTile.getX(), targetTile.getY());
        }

        return false;
    }

    /**
     * Рассчитывает стратегическую ценность клетки для принятия решений AI.
     * <p>
     * В текущей реализации учитываются следующие факторы:
     * <ul>
     *   <li>Близость к центру карты (более ценные клетки)</li>
     *   <li>Требования по количеству крестьян для захвата</li>
     * </ul>
     *
     * @param tile клетка, ценность которой рассчитывается
     * @param ai игрок AI, для которого рассчитывается ценность
     * @return численное значение стратегической ценности клетки
     *         (чем выше значение, тем более ценна клетка)
     */
    private double calculateTileValue(Tile tile, main.java.com.turngame.model.Player ai) {
        double value = 0;

        // Клетки ближе к центру более ценны
        int centerX = Constants.MAP_WIDTH / 2;
        int centerY = Constants.MAP_HEIGHT / 2;
        double distanceToCenter = Math.sqrt(Math.pow(tile.getX() - centerX, 2) +
                Math.pow(tile.getY() - centerY, 2));
        value += (Constants.MAP_WIDTH - distanceToCenter) * 10;

        // Клетки с меньшими требованиями более ценны
        value += (10 - tile.getRequiredPeasants()) * 5;

        return value;
    }
}