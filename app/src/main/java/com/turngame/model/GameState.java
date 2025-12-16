package main.java.com.turngame.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, представляющий полное состояние игры для сохранения и загрузки.
 * <p>
 * Этот класс служит контейнером для всех данных, необходимых для сохранения
 * текущего состояния игры и последующего его восстановления. Он содержит
 * все ключевые игровые объекты и их состояние на момент сохранения.
 * <p>
 * Класс реализует интерфейс {@link Serializable}, что позволяет сериализовать
 * его в поток байтов для сохранения в файл или передачи по сети.
 * <p>
 * Все поля класса должны быть сериализуемыми или иметь возможность быть
 * сериализованными через специальную обработку.
 *
 * @author Playstall
 * @version 1.0
 * @see Serializable
 * @see GameModel
 * @see Player
 * @see Tile
 * @see Building
 * @since 1.0
 */
public class GameState implements Serializable {
    /**
     * Идентификатор версии для сериализации.
     * <p>
     * Используется для контроля совместимости сохраненных игровых состояний.
     * При изменении структуры класса следует изменить этот идентификатор.
     *
     * @see Serializable
     */
    private static final long serialVersionUID = 1L;

    /**
     * Текущий игровой день на момент сохранения.
     */
    private int currentDay;

    /**
     * Объект игрока-человека.
     */
    private Player player;

    /**
     * Объект компьютерного игрока (AI).
     */
    private Player ai;

    /**
     * Игровая карта в виде двумерного массива клеток.
     */
    private Tile[][] map;

    /**
     * Список всех построенных зданий.
     * <p>
     * Инициализируется пустым списком при создании объекта.
     */
    private List<Building> buildings;

    /**
     * Текущий активный игрок на момент сохранения.
     * <p>
     * Может принимать значения:
     * <ul>
     *   <li>"PLAYER" - ход игрока-человека</li>
     *   <li>"AI" - ход компьютерного игрока</li>
     * </ul>
     */
    private String currentPlayer; // "PLAYER" или "AI"

    /**
     * Создает новое пустое состояние игры.
     * <p>
     * Инициализирует список зданий пустым списком.
     * Остальные поля должны быть установлены через сеттеры.
     *
     * @see #setCurrentDay(int)
     * @see #setPlayer(Player)
     * @see #setAi(Player)
     * @see #setMap(Tile[][])
     * @see #setCurrentPlayer(String)
     */
    public GameState() {
        this.buildings = new ArrayList<>();
    }

    /**
     * Возвращает текущий игровой день на момент сохранения.
     *
     * @return номер текущего дня (начинается с 1)
     */
    public int getCurrentDay() { return currentDay; }

    /**
     * Устанавливает текущий игровой день.
     *
     * @param currentDay номер дня для установки (должен быть положительным)
     */
    public void setCurrentDay(int currentDay) { this.currentDay = currentDay; }


    /**
     * Возвращает объект игрока-человека.
     *
     * @return игрок-человек, может быть {@code null} если не установлен
     */
    public Player getPlayer() { return player; }

    /**
     * Устанавливает объект игрока-человека.
     *
     * @param player объект игрока для установки
     * @throws NullPointerException если {@code player} равен {@code null}
     */
    public void setPlayer(Player player) { this.player = player; }


    /**
     * Возвращает объект компьютерного игрока (AI).
     *
     * @return компьютерный игрок, может быть {@code null} если не установлен
     */
    public Player getAi() { return ai; }

    /**
     * Устанавливает объект компьютерного игрока.
     *
     * @param ai объект AI для установки
     * @throws NullPointerException если {@code ai} равен {@code null}
     */
    public void setAi(Player ai) { this.ai = ai; }


    /**
     * Возвращает игровую карту.
     *
     * @return двумерный массив клеток карты, может быть {@code null}
     */
    public Tile[][] getMap() { return map; }

    /**
     * Устанавливает игровую карту.
     *
     * @param map двумерный массив клеток для установки
     * @throws NullPointerException если {@code map} равен {@code null}
     */
    public void setMap(Tile[][] map) { this.map = map; }


    /**
     * Возвращает список всех построенных зданий.
     *
     * @return список зданий (не может быть {@code null}, может быть пустым)
     */
    public List<Building> getBuildings() { return buildings; }

    /**
     * Устанавливает список зданий.
     *
     * @param buildings список зданий для установки
     * @throws NullPointerException если {@code buildings} равен {@code null}
     */
    public void setBuildings(List<Building> buildings) { this.buildings = buildings; }


    /**
     * Возвращает текущего активного игрока.
     *
     * @return строка, идентифицирующая текущего игрока:
     *         "PLAYER" или "AI", может быть {@code null}
     */
    public String getCurrentPlayer() { return currentPlayer; }

    /**
     * Устанавливает текущего активного игрока.
     *
     * @param currentPlayer строка, идентифицирующая текущего игрока.
     *                      Должна быть "PLAYER" или "AI" для корректной работы.
     * @throws NullPointerException если {@code currentPlayer} равен {@code null}
     * @throws IllegalArgumentException если значение не "PLAYER" или "AI"
     */
    public void setCurrentPlayer(String currentPlayer) { this.currentPlayer = currentPlayer; }
}