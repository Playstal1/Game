package main.java.com.turngame.controller;

import main.java.com.turngame.model.GameModel;
import main.java.com.turngame.model.GameModel.ActionType;
import main.java.com.turngame.model.GameState;
import main.java.com.turngame.model.Player;
import main.java.com.turngame.model.Tile;
import main.java.com.turngame.util.GameLogger;
import main.java.com.turngame.util.GameSaver;

/**
 * Основной контроллер игры, связывающий модель и представление.
 * <p>
 * Этот класс служит центральным посредником между моделью игры ({@link GameModel})
 * и представлением (UI). Он обрабатывает действия игрока, управляет состоянием игры,
 * координирует работу AI и обеспечивает сохранение/загрузку игрового процесса.
 * <p>
 * Основные функции контроллера:
 * <ul>
 *   <li>Обработка игровых действий пользователя</li>
 *   <li>Управление ходом AI через {@link AIController}</li>
 *   <li>Сохранение и восстановление игрового состояния</li>
 *   <li>Проверка допустимости действий</li>
 *   <li>Предоставление информации о текущем состоянии игры</li>
 * </ul>
 *
 * @author Playstall
 * @version 1.0
 * @see GameModel
 * @see AIController
 * @see GameSaver
 * @since 1.0
 */
public class GameController {
    private GameModel gameModel;
    private GameSaver gameSaver;
    private AIController aiController;

    /**
     * Создает новый экземпляр контроллера игры.
     * <p>
     * Инициализирует все необходимые компоненты:
     * <ul>
     *   <li>Новую игровую модель ({@link GameModel})</li>
     *   <li>Средство сохранения игры ({@link GameSaver})</li>
     *   <li>Контроллер AI ({@link AIController}) для управления противником</li>
     * </ul>
     */
    public GameController() {
        this.gameModel = new GameModel();
        this.gameSaver = new GameSaver();
        this.aiController = new AIController(gameModel);
    }

    /**
     * Выполняет указанное действие игрока.
     * <p>
     * Метод делегирует выполнение действия игровой модели и логирует результат.
     * В случае ошибки перехватывает исключение, логирует его и возвращает {@code false}.
     *
     * @param action тип действия для выполнения
     * @param params параметры действия (зависят от типа действия):
     *               <ul>
     *                 <li>Для {@link ActionType#EXPLORE_TILE}: координаты x и y (Integer, Integer)</li>
     *                 <li>Для других действий: обычно не требуются параметры</li>
     *               </ul>
     * @return {@code true} если действие успешно выполнено, {@code false} в противном случае
     * @throws NullPointerException если {@code action} равен {@code null}
     * @see GameModel#performPlayerAction(ActionType, Object...)
     * @see ActionType
     */
    public boolean performAction(ActionType action, Object... params) {
        try {
            boolean result = gameModel.performPlayerAction(action, params);

            if (result) {
                GameLogger.log("Действие выполнено: " + action.getDescription());
            } else {
                GameLogger.logWarning("Не удалось выполнить действие: " + action.getDescription());
            }

            return result;
        } catch (Exception e) {
            GameLogger.logError("Ошибка при выполнении действия: " + action, e);
            return false;
        }
    }

    /**
     * Сохраняет текущее состояние игры в файл.
     * <p>
     * Создает снимок текущего состояния игры и сохраняет его в указанный файл
     * с использованием {@link GameSaver}.
     *
     * @param filename имя файла для сохранения (может включать путь)
     * @return {@code true} если сохранение успешно завершено,
     *         {@code false} в случае ошибки сохранения
     * @throws NullPointerException если {@code filename} равен {@code null}
     * @see GameSaver#saveGame(GameState, String)
     * @see #createGameState()
     */
    public boolean saveGame(String filename) {
        try {
            GameState gameState = createGameState();
            return gameSaver.saveGame(gameState, filename);
        } catch (Exception e) {
            GameLogger.logError("Ошибка при сохранении игры", e);
            return false;
        }
    }

    /**
     * Загружает состояние игры из файла.
     * <p>
     * Восстанавливает игровое состояние из указанного файла и обновляет
     * внутреннюю модель для продолжения игры.
     *
     * @param filename имя файла для загрузки (может включать путь)
     * @return {@code true} если загрузка успешно завершена,
     *         {@code false} если файл не найден или произошла ошибка
     * @throws NullPointerException если {@code filename} равен {@code null}
     * @see GameSaver#loadGame(String)
     * @see #restoreFromGameState(GameState)
     */
    public boolean loadGame(String filename) {
        try {
            GameState gameState = gameSaver.loadGame(filename);
            if (gameState != null) {
                restoreFromGameState(gameState);
                return true;
            }
            return false;
        } catch (Exception e) {
            GameLogger.logError("Ошибка при загрузке игры", e);
            return false;
        }
    }

    /**
     * Создает объект состояния игры из текущей модели.
     * <p>
     * Извлекает все необходимые данные из текущей игровой модели
     * и упаковывает их в объект {@link GameState} для последующего сохранения.
     *
     * @return новый объект {@link GameState}, содержащий текущее состояние игры
     * @see GameState
     */
    private GameState createGameState() {
        GameState state = new GameState();
        state.setCurrentDay(gameModel.getCurrentDay());
        state.setPlayer(gameModel.getPlayer());
        state.setAi(gameModel.getAi());
        state.setMap(gameModel.getMap());
        state.setBuildings(gameModel.getBuildings());
        state.setCurrentPlayer("PLAYER");
        return state;
    }

    /**
     * Восстанавливает модель из состояния игры.
     * <p>
     * Обновляет внутреннюю игровую модель на основе данных из объекта
     * {@link GameState}. Используется после загрузки сохраненной игры.
     *
             * @param state объект состояния игры, из которого нужно восстановить данные
     * @throws NullPointerException если {@code state} равен {@code null}
     * @see GameState
     */
    private void restoreFromGameState(GameState state) {
        this.gameModel = new GameModel();

        this.gameModel.initializeGame(state);

        GameLogger.log("Загрузка игры из файла...");
    }

    /**
     * Получает текстовую информацию о указанной клетке карты.
     *
     * @param x координата X клетки (начинается с 0)
     * @param y координата Y клетки (начинается с 0)
     * @return строковое описание клетки, включающее:
     *         <ul>
     *           <li>Координаты</li>
     *           <li>Требуемое количество крестьян для захвата</li>
     *           <li>Статус контроля (контролируемая или свободная)</li>
     *           <li>Имя владельца (если клетка контролируется)</li>
     *         </ul>
     *         Если клетка не существует, возвращает "Неизвестная территория"
     * @see Tile
     */
    public String getTileInfo(int x, int y) {
        Tile tile = gameModel.getTile(x, y);
        if (tile == null) {
            return "Неизвестная территория";
        }

        StringBuilder info = new StringBuilder();
        info.append("Клетка [").append(x).append(",").append(y).append("]\n");
        info.append("Требуется крестьян: ").append(tile.getRequiredPeasants()).append("\n");

        if (tile.isControlled()) {
            info.append("Контролирует: ").append(tile.getOwner().getName());
        } else {
            info.append("Свободная территория");
        }

        return info.toString();
    }

    /**
     * Получает информацию о текущих ресурсах игрока.
     *
     * @return форматированная строка, содержащая:
     *         <ul>
     *           <li>Количество риса</li>
     *           <li>Количество воды</li>
     *           <li>Количество крестьян</li>
     *           <li>Количество контролируемых территорий</li>
     *         </ul>
     */
    public String getPlayerResourcesInfo() {
        Player player = gameModel.getPlayer();
        return String.format(
                "Игрок: Рис=%d, Вода=%d, Крестьяне=%d, Территории=%d",
                player.getResource(main.java.com.turngame.model.ResourceType.RICE),
                player.getResource(main.java.com.turngame.model.ResourceType.WATER),
                player.getResource(main.java.com.turngame.model.ResourceType.PEASANTS),
                player.getControlledTiles()
        );
    }

    /**
     * Получает информацию о текущих ресурсах AI (компьютерного противника).
     *
     * @return форматированная строка, содержащая:
     *         <ul>
     *           <li>Количество риса</li>
     *           <li>Количество воды</li>
     *           <li>Количество крестьян</li>
     *           <li>Количество контролируемых территорий</li>
     *         </ul>
     */
    public String getAIResourcesInfo() {
        Player ai = gameModel.getAi();
        return String.format(
                "Компьютер: Рис=%d, Вода=%d, Крестьяне=%d, Территории=%d",
                ai.getResource(main.java.com.turngame.model.ResourceType.RICE),
                ai.getResource(main.java.com.turngame.model.ResourceType.WATER),
                ai.getResource(main.java.com.turngame.model.ResourceType.PEASANTS),
                ai.getControlledTiles()
        );
    }

    /**
     * Проверяет, может ли игрок построить дом крестьянина.
     * <p>
     * Проверяет, достаточно ли у игрока всех необходимых ресурсов:
     * риса, воды и крестьян согласно константам {@link Constants}.
     *
     * @return {@code true} если у игрока достаточно ресурсов для постройки,
     *         {@code false} в противном случае
     * @see main.java.com.turngame.util.Constants#HOUSE_RICE_COST
     * @see main.java.com.turngame.util.Constants#HOUSE_WATER_COST
     * @see main.java.com.turngame.util.Constants#HOUSE_PEASANT_COST
     */
    public boolean canBuildPeasantHouse() {
        Player player = gameModel.getPlayer();
        return player.getResource(main.java.com.turngame.model.ResourceType.RICE) >= main.java.com.turngame.util.Constants.HOUSE_RICE_COST &&
                player.getResource(main.java.com.turngame.model.ResourceType.WATER) >= main.java.com.turngame.util.Constants.HOUSE_WATER_COST &&
                player.getResource(main.java.com.turngame.model.ResourceType.PEASANTS) >= main.java.com.turngame.util.Constants.HOUSE_PEASANT_COST;
    }

    /**
     * Проверяет, может ли игрок полить рис.
     * <p>
     * Для полива риса требуется минимум 10 единиц воды.
     *
     * @return {@code true} если у игрока достаточно воды для полива,
     *         {@code false} в противном случае
     */
    public boolean canWaterRice() {
        Player player = gameModel.getPlayer();
        return player.getResource(main.java.com.turngame.model.ResourceType.WATER) >= 10;
    }

    /**
     * Проверяет, может ли игрок захватить указанную клетку.
     * <p>
     * Проверяет существование клетки и возможность её захвата
     * текущим игроком на основе его ресурсов и требований клетки.
     *
     * @param x координата X клетки для проверки
     * @param y координата Y клетки для проверки
     * @return {@code true} если клетка существует и может быть захвачена игроком,
     *         {@code false} в противном случае
     * @see Tile#canBeConqueredBy(Player)
     */
    public boolean canConquerTile(int x, int y) {
        Tile tile = gameModel.getTile(x, y);
        Player player = gameModel.getPlayer();
        return tile != null && tile.canBeConqueredBy(player);
    }

    /**
     * Возвращает текущую игровую модель.
     *
     * @return текущий экземпляр {@link GameModel}
     */
    public GameModel getGameModel() { return gameModel; }

    /**
     * Возвращает текущий игровой день.
     *
     * @return номер текущего дня в игре
     */
    public int getCurrentDay() { return gameModel.getCurrentDay(); }

    /**
     * Проверяет, завершена ли игра.
     *
     * @return {@code true} если игра завершена, {@code false} в противном случае
     */
    public boolean isGameOver() { return gameModel.isGameOver(); }

    /**
     * Возвращает победителя игры.
     *
     * @return объект {@link Player}, представляющий победителя,
     *         или {@code null}, если игра еще не завершена или нет победителя
     */
    public Player getWinner() { return gameModel.getWinner(); }

    /**
     * Начинает новую игру.
     * <p>
     * Создает новую игровую модель, сбрасывая все текущее состояние.
     * Логирует начало новой игры.
     *
     * @see GameModel
     */
    public void newGame() {
        this.gameModel = new GameModel();
        GameLogger.log("Начата новая игра");
    }
}