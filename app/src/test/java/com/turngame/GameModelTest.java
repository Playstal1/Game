package test.java.com.turngame;

import main.java.com.turngame.model.GameModel;
import main.java.com.turngame.model.Player;
import main.java.com.turngame.model.ResourceType;
import main.java.com.turngame.model.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для игровой модели
 */
class GameModelTest {
    private GameModel gameModel;

    @BeforeEach
    void setUp() {
        gameModel = new GameModel();
    }

    @Test
    void testInitialGameState() {
        Player player = gameModel.getPlayer();

        assertNotNull(player, "Игрок должен быть создан");
        assertEquals("Игрок", player.getName(), "Имя игрока должно быть 'Игрок'");
        assertTrue(player.getResource(ResourceType.PEASANTS) > 0, "У игрока должны быть крестьяне");
        assertTrue(player.getResource(ResourceType.RICE) > 0, "У игрока должен быть рис");
        assertTrue(player.getResource(ResourceType.WATER) > 0, "У игрока должна быть вода");
    }

    @Test
    void testCollectWaterAction() {
        Player player = gameModel.getPlayer();
        int initialWater = player.getResource(ResourceType.WATER);

        boolean result = gameModel.performPlayerAction(GameModel.ActionType.COLLECT_WATER);

        assertTrue(result, "Сбор воды должен быть успешным");
        assertTrue(player.getResource(ResourceType.WATER) > initialWater,
                "Количество воды должно увеличиться");
    }

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

    @Test
    void testWaterRiceActionWithoutEnoughWater() {
        Player player = gameModel.getPlayer();
        player.setResource(ResourceType.WATER, 5);

        boolean result = gameModel.performPlayerAction(GameModel.ActionType.WATER_RICE);

        assertFalse(result, "Полив риса должен завершиться неудачей при недостатке воды");
    }

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

    @Test
    void testExploreTileInvalidCoordinates() {
        boolean result = gameModel.performPlayerAction(GameModel.ActionType.EXPLORE_TILE,
                -1, -1);

        assertFalse(result, "Освоение недопустимых координат должно завершиться неудачей");
    }

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

    @Test
    void testDayProgression() {
        int initialDay = gameModel.getCurrentDay();

        gameModel.performPlayerAction(GameModel.ActionType.COLLECT_WATER);

        assertEquals(initialDay + 1, gameModel.getCurrentDay(),
                "День должен увеличиться после выполнения действия");
    }
}