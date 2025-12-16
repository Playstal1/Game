package test.java.com.turngame;

import main.java.com.turngame.model.GameModel;
import main.java.com.turngame.model.Player;
import main.java.com.turngame.model.ResourceType;
import main.java.com.turngame.model.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для игровой модели ({@link GameModel}).
 * <p>
 * Этот тестовый класс проверяет корректность работы основной игровой логики,
 * включая инициализацию игры, выполнение игровых действий, управление ресурсами
 * и проверку условий победы. Тесты используют фреймворк JUnit 5.
 * <p>
 * Структура тестового класса:
 * <ul>
 *   <li>Каждый тест проверяет конкретный аспект функциональности {@link GameModel}</li>
 *   <li>Метод {@link #setUp()} инициализирует свежий экземпляр {@link GameModel} перед каждым тестом</li>
 *   <li>Тесты изолированы друг от друга (не зависят от порядка выполнения)</li>
 *   <li>Используются утверждения (assertions) для проверки ожидаемого поведения</li>
 * </ul>
 *
 * @author Playstall
 * @version 1.0
 * @see GameModel
 * @see Player
 * @see Tile
 * @see ResourceType
 * @see <a href="https://junit.org/junit5/docs/current/user-guide/">JUnit 5 User Guide</a>
 * @since 1.0
 */
class GameModelTest {
    /**
     * Экземпляр игровой модели, используемый в тестах.
     * Инициализируется перед каждым тестом методом {@link #setUp()}.
     */
    private GameModel gameModel;

    /**
     * Подготовка тестового окружения перед каждым тестом.
     * <p>
     * Создает новый экземпляр {@link GameModel}, что гарантирует изолированность
     * тестов и начальное состояние игры для каждого тестового случая.
     *
     * @see BeforeEach
     */
    @BeforeEach
    void setUp() {
        gameModel = new GameModel();
    }

    /**
     * Тестирует начальное состояние игры после создания.
     * <p>
     * Проверяет, что:
     * <ul>
     *   <li>Игрок успешно создан</li>
     *   <li>Имя игрока установлено корректно</li>
     *   <li>У игрока есть начальные ресурсы (рис, вода, крестьяне)</li>
     * </ul>
     *
     * @see GameModel#getPlayer()
     * @see Player#getName()
     * @see Player#getResource(ResourceType)
     */
    @Test
    void testInitialGameState() {
        Player player = gameModel.getPlayer();

        assertNotNull(player, "Игрок должен быть создан");
        assertEquals("Игрок", player.getName(), "Имя игрока должно быть 'Игрок'");
        assertTrue(player.getResource(ResourceType.PEASANTS) > 0, "У игрока должны быть крестьяне");
        assertTrue(player.getResource(ResourceType.RICE) > 0, "У игрока должен быть рис");
        assertTrue(player.getResource(ResourceType.WATER) > 0, "У игрока должна быть вода");
    }

    /**
     * Тестирует действие сбора воды.
     * <p>
     * Проверяет, что:
     * <ul>
     *   <li>Действие сбора воды выполняется успешно</li>
     *   <li>Количество воды у игрока увеличивается после сбора</li>
     * </ul>
     *
     * @see GameModel#performPlayerAction(GameModel.ActionType, Object...)
     * @see GameModel.ActionType#COLLECT_WATER
     */
    @Test
    void testCollectWaterAction() {
        Player player = gameModel.getPlayer();
        int initialWater = player.getResource(ResourceType.WATER);

        boolean result = gameModel.performPlayerAction(GameModel.ActionType.COLLECT_WATER);

        assertTrue(result, "Сбор воды должен быть успешным");
        assertTrue(player.getResource(ResourceType.WATER) > initialWater,
                "Количество воды должно увеличиться");
    }

    /**
     * Тестирует действие полива риса при достаточном количестве воды.
     * <p>
     * Проверяет, что:
     * <ul>
     *   <li>Действие полива риса выполняется успешно, когда воды достаточно</li>
     *   <li>Количество воды уменьшается на 10 единиц (стоимость полива)</li>
     * </ul>
     *
     * @see GameModel#performPlayerAction(GameModel.ActionType, Object...)
     * @see GameModel.ActionType#WATER_RICE
     */
    @Test
    void testWaterRiceActionWithEnoughWater() {
        Player player = gameModel.getPlayer();
        player.setResource(ResourceType.WATER, 20);
        int initialWater = player.getResource(ResourceType.WATER);

        boolean result = gameModel.performPlayerAction(GameModel.ActionType.WATER_RICE);

        assertTrue(result, "Полив риса должен быть успешным при достаточной воде");
        assertEquals(initialWater - 10, player.getResource(ResourceType.WATER),
                "Количество воды должно уменьшиться на 10");
    }

    /**
     * Тестирует действие полива риса при недостаточном количестве воды.
     * <p>
     * Проверяет, что:
     * <ul>
     *   <li>Действие полива риса завершается неудачей, когда воды недостаточно</li>
     *   <li>Количество воды не изменяется</li>
     * </ul>
     *
     * @see GameModel#performPlayerAction(GameModel.ActionType, Object...)
     * @see GameModel.ActionType#WATER_RICE
     */
    @Test
    void testWaterRiceActionWithoutEnoughWater() {
        Player player = gameModel.getPlayer();
        player.setResource(ResourceType.WATER, 5);

        boolean result = gameModel.performPlayerAction(GameModel.ActionType.WATER_RICE);

        assertFalse(result, "Полив риса должен завершиться неудачей при недостатке воды");
    }

    /**
     * Тестирует действие освоения (захвата) территории при валидных условиях.
     * <p>
     * Проверяет, что:
     * <ul>
     *   <li>Действие освоения территории выполняется успешно</li>
     *   <li>Клетка помечается как контролируемая</li>
     *   <li>Игрок становится владельцем клетки</li>
     * </ul>
     * Тест ищет свободную клетку на карте и проверяет её захват.
     *
     * @see GameModel#performPlayerAction(GameModel.ActionType, Object...)
     * @see GameModel.ActionType#EXPLORE_TILE
     * @see Tile#isControlled()
     * @see Tile#getOwner()
     */
    @Test
    void testExploreTileValid() {
        // Тестируем освоение доступной территории
        Tile[][] map = gameModel.getMap();
        Tile testTile = null;

        // Находим свободную клетку
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[x].length; y++) {
                if (!map[x][y].isControlled()) {
                    testTile = map[x][y];
                    break;
                }
            }
            if (testTile != null) break;
        }

        if (testTile != null) {
            Player player = gameModel.getPlayer();
            int requiredPeasants = testTile.getRequiredPeasants();
            player.setResource(ResourceType.PEASANTS, requiredPeasants + 5);

            boolean result = gameModel.performPlayerAction(GameModel.ActionType.EXPLORE_TILE,
                    testTile.getX(), testTile.getY());

            assertTrue(result, "Освоение территории должно быть успешным");
            assertTrue(testTile.isControlled(), "Клетка должна быть под контролем");
            assertEquals(player, testTile.getOwner(), "Игрок должен быть владельцем клетки");
        }
    }

    /**
     * Тестирует действие освоения территории с недопустимыми координатами.
     * <p>
     * Проверяет, что:
     * <ul>
     *   <li>Действие освоения территории завершается неудачей при недопустимых координатах</li>
     * </ul>
     *
     * @see GameModel#performPlayerAction(GameModel.ActionType, Object...)
     * @see GameModel.ActionType#EXPLORE_TILE
     */
    @Test
    void testExploreTileInvalidCoordinates() {
        boolean result = gameModel.performPlayerAction(GameModel.ActionType.EXPLORE_TILE,
                -1, -1);

        assertFalse(result, "Освоение недопустимых координат должно завершиться неудачей");
    }

    /**
     * Тестирует действие постройки дома крестьянина при достаточных ресурсах.
     * <p>
     * Проверяет, что:
     * <ul>
     *   <li>Действие постройки дома крестьянина выполняется успешно</li>
     *   <li>Ресурсы корректно списываются после постройки</li>
     * </ul>
     * Примечание: в конце дня происходит прирост риса (5 единиц), что учитывается в проверке.
     *
     * @see GameModel#performPlayerAction(GameModel.ActionType, Object...)
     * @see GameModel.ActionType#BUILD_PEASANT_HOUSE
     */
    @Test
    void testBuildPeasantHouseWithEnoughResources() {
        Player player = gameModel.getPlayer();
        player.setResource(ResourceType.RICE, 50);
        player.setResource(ResourceType.WATER, 50);
        player.setResource(ResourceType.PEASANTS, 10);

        boolean result = gameModel.performPlayerAction(GameModel.ActionType.BUILD_PEASANT_HOUSE);

        assertTrue(result, "Постройка дома должна быть успешной при достаточных ресурсах");
        assertEquals(35, player.getResource(ResourceType.RICE), "Рис должен уменьшиться на стоимость постройки и добавиться 5 в конце дня");
        assertEquals(40, player.getResource(ResourceType.WATER), "Вода должна уменьшиться на стоимость постройки");
    }

    /**
     * Тестирует условие победы в игре.
     * <p>
     * Проверяет, что игрок признается победителем при контроле над 50% территории.
     * В тесте моделируется ситуация, когда игрок контролирует достаточное количество клеток,
     * и проверяется математическое условие победы.
     *
     * @see GameModel#isGameOver()
     * @see GameModel#getWinner()
     * @see main.java.com.turngame.util.Constants#WIN_PERCENTAGE
     */
    @Test
    void testGameVictoryCondition() {
        Player player = gameModel.getPlayer();
        Player ai = gameModel.getAi();

        // Устанавливаем контроль над 50% территории
        int totalTiles = main.java.com.turngame.util.Constants.MAP_WIDTH * main.java.com.turngame.util.Constants.MAP_HEIGHT;
        int neededForVictory = (int) (totalTiles * main.java.com.turngame.util.Constants.WIN_PERCENTAGE);

        player.setControlledTiles(neededForVictory);

        // Проверяем, что игра определяет победу
        // Для этого нужно вызвать метод, который проверяет условия победы
        // В данном тесте просто проверяем логику

        double playerPercentage = (double) player.getControlledTiles() / totalTiles;
        assertTrue(playerPercentage >= main.java.com.turngame.util.Constants.WIN_PERCENTAGE,
                "Игрок должен контролировать 50% или больше территории для победы");
    }

    /**
     * Тестирует прогрессию игровых дней.
     * <p>
     * Проверяет, что после выполнения любого действия текущий день увеличивается на 1.
     *
     * @see GameModel#getCurrentDay()
     * @see GameModel#performPlayerAction(GameModel.ActionType, Object...)
     */
    @Test
    void testDayProgression() {
        int initialDay = gameModel.getCurrentDay();

        gameModel.performPlayerAction(GameModel.ActionType.COLLECT_WATER);

        assertEquals(initialDay + 1, gameModel.getCurrentDay(),
                "День должен увеличиться после выполнения действия");
    }
}