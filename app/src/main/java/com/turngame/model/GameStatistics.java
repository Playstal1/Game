package main.java.com.turngame.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для сбора, хранения и предоставления статистических данных об игре.
 * <p>
 * Собирает исторические данные о ресурсах и прогрессе обоих игроков
 * (игрока-человека и компьютерного AI) на протяжении всей игровой сессии.
 * Статистика записывается в конце каждого игрового дня и может быть использована для:
 * <ul>
 *   <li>Анализа стратегии игроков</li>
 *   <li>Построения графиков прогресса</li>
 *   <li>Определения тенденций развития игры</li>
 *   <li>Отображения истории игры в интерфейсе</li>
 * </ul>
 * <p>
 * Класс хранит отдельные временные ряды для каждого типа ресурса
 * и контролируемых территорий, что позволяет анализировать динамику игры.
 *
 * @author Playstall
 * @version 1.0
 * @see Player
 * @see ResourceType
 * @since 1.0
 */
public class GameStatistics {
    /**
     * История количества риса у игрока-человека по дням.
     * <p>
     * Каждый элемент списка соответствует количеству риса на конец соответствующего дня.
     * Индекс элемента соответствует дню игры (с учетом того, что первый день имеет индекс 0).
     */
    private List<Integer> playerRiceHistory = new ArrayList<>();

    /**
     * История количества воды у игрока-человека по дням.
     */
    private List<Integer> playerWaterHistory = new ArrayList<>();

    /**
     * История количества крестьян у игрока-человека по дням.
     */
    private List<Integer> playerPeasantsHistory = new ArrayList<>();

    /**
     * История количества контролируемых территорий у игрока-человека по дням.
     */
    private List<Integer> playerTilesHistory = new ArrayList<>();


    /**
     * История количества риса у компьютерного игрока (AI) по дням.
     */
    private List<Integer> aiRiceHistory = new ArrayList<>();

    /**
     * История количества воды у компьютерного игрока (AI) по дням.
     */
    private List<Integer> aiWaterHistory = new ArrayList<>();

    /**
     * История количества крестьян у компьютерного игрока (AI) по дням.
     */
    private List<Integer> aiPeasantsHistory = new ArrayList<>();

    /**
     * История количества контролируемых территорий у компьютерного игрока (AI) по дням.
     */
    private List<Integer> aiTilesHistory = new ArrayList<>();


    /**
     * Записывает текущую статистику игрока-человека в историю.
     * <p>
     * Метод вызывается в конце каждого игрового дня для фиксации
     * текущего состояния игрока. Записываются следующие показатели:
     * <ul>
     *   <li>Количество риса</li>
     *   <li>Количество воды</li>
     *   <li>Количество крестьян</li>
     *   <li>Количество контролируемых территорий</li>
     * </ul>
     * Каждый показатель добавляется в соответствующий список истории.
     *
     * @param player игрок-человек, чью статистику нужно записать
     * @throws NullPointerException если {@code player} равен {@code null}
     * @see Player#getResource(ResourceType)
     * @see Player#getControlledTiles()
     */
    public void recordPlayerStats(Player player) {
        playerRiceHistory.add(player.getResource(ResourceType.RICE));
        playerWaterHistory.add(player.getResource(ResourceType.WATER));
        playerPeasantsHistory.add(player.getResource(ResourceType.PEASANTS));
        playerTilesHistory.add(player.getControlledTiles());
    }

    /**
     * Записывает текущую статистику компьютерного игрока (AI) в историю.
     * <p>
     * Метод вызывается в конце каждого игрового дня для фиксации
     * текущего состояния AI. Записываются те же показатели, что и для игрока-человека.
     *
     * @param ai компьютерный игрок (AI), чью статистику нужно записать
     * @throws NullPointerException если {@code ai} равен {@code null}
     * @see Player#getResource(ResourceType)
     * @see Player#getControlledTiles()
     */
    public void recordAiStats(Player ai) {
        aiRiceHistory.add(ai.getResource(ResourceType.RICE));
        aiWaterHistory.add(ai.getResource(ResourceType.WATER));
        aiPeasantsHistory.add(ai.getResource(ResourceType.PEASANTS));
        aiTilesHistory.add(ai.getControlledTiles());
    }

    /**
     * Возвращает историю количества риса у игрока-человека.
     *
     * @return список, содержащий количество риса на каждый записанный день.
     *         Список не может быть {@code null}, но может быть пустым.
     */
    public List<Integer> getPlayerRiceHistory() { return playerRiceHistory; }

    /**
     * Возвращает историю количества воды у игрока-человека.
     *
     * @return список, содержащий количество воды на каждый записанный день
     */
    public List<Integer> getPlayerWaterHistory() { return playerWaterHistory; }

    /**
     * Возвращает историю количества крестьян у игрока-человека.
     *
     * @return список, содержащий количество крестьян на каждый записанный день
     */
    public List<Integer> getPlayerPeasantsHistory() { return playerPeasantsHistory; }

    /**
     * Возвращает историю количества контролируемых территорий у игрока-человека.
     *
     * @return список, содержащий количество территорий на каждый записанный день
     */
    public List<Integer> getPlayerTilesHistory() { return playerTilesHistory; }


    /**
     * Возвращает историю количества риса у компьютерного игрока (AI).
     *
     * @return список, содержащий количество риса на каждый записанный день
     */
    public List<Integer> getAiRiceHistory() { return aiRiceHistory; }

    /**
     * Возвращает историю количества воды у компьютерного игрока (AI).
     *
     * @return список, содержащий количество воды на каждый записанный день
     */
    public List<Integer> getAiWaterHistory() { return aiWaterHistory; }

    /**
     * Возвращает историю количества крестьян у компьютерного игрока (AI).
     *
     * @return список, содержащий количество крестьян на каждый записанный день
     */
    public List<Integer> getAiPeasantsHistory() { return aiPeasantsHistory; }

    /**
     * Возвращает историю количества контролируемых территорий у компьютерного игрока (AI).
     *
     * @return список, содержащий количество территорий на каждый записанный день
     */
    public List<Integer> getAiTilesHistory() { return aiTilesHistory; }

    /**
     * Возвращает общее количество дней, за которые собрана статистика.
     * <p>
     * Метод определяет, сколько дней статистики было записано,
     * на основе размера одного из списков истории игрока.
     *
     * @return количество записанных дней статистики.
     *         Возвращает 0, если статистика еще не записывалась.
     * @throws IllegalStateException если списки истории имеют разную длину
     *         (что указывает на ошибку в логике записи статистики)
     */
    public int getTotalDays() {
        return playerRiceHistory.size();
    }
}