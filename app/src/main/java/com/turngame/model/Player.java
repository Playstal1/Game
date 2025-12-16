package main.java.com.turngame.model;

import main.java.com.turngame.util.Constants;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;

/**
 * Класс, представляющий игрока в игре, как человека, так и компьютерного противника (AI).
 * <p>
 * Управляет всеми аспектами игрока, включая:
 * <ul>
 *   <li>Имя игрока</li>
 *   <li>Ресурсы всех типов ({@link ResourceType})</li>
 *   <li>Количество контролируемых территорий</li>
 *   <li>Операции с ресурсами (добавление, вычитание, получение)</li>
 * </ul>
 * <p>
 * Класс использует {@link EnumMap} для эффективного хранения ресурсов,
 * что обеспечивает быстрый доступ и обновление значений ресурсов.
 * <p>
 * Класс реализует интерфейс {@link Serializable}, что позволяет сериализовать
 * состояние игрока для сохранения и загрузки игры.
 *
 * @author Playstall
 * @version 1.0
 * @see ResourceType
 * @see Constants
 * @see Serializable
 * @since 1.0
 */
public class Player implements Serializable {
    /**
     * Имя игрока. Используется для идентификации в игре и отображения в интерфейсе.
     */
    private String name;

    /**
     * Карта ресурсов игрока, где ключ - тип ресурса, значение - текущее количество.
     * <p>
     * Используется {@link EnumMap} для эффективного хранения и доступа.
     */
    private Map<ResourceType, Integer> resources;

    /**
     * Количество территорий, контролируемых игроком на карте.
     */
    private int controlledTiles;

    /**
     * Создает нового игрока с указанным именем и начальными ресурсами.
     * <p>
     * Инициализирует все ресурсы значениями из {@link Constants}:
     * <ul>
     *   <li>Рис: {@link Constants#INITIAL_RICE}</li>
     *   <li>Вода: {@link Constants#INITIAL_WATER}</li>
     *   <li>Крестьяне: {@link Constants#INITIAL_PEASANTS}</li>
     * </ul>
     * Количество контролируемых территорий устанавливается в 0.
     *
     * @param name имя игрока (не может быть пустым или {@code null})
     * @throws NullPointerException если {@code name} равен {@code null}
     * @throws IllegalArgumentException если {@code name} пустая строка
     * @see #initializeResources()
     */
    public Player(String name) {
        this.name = name;
        this.resources = new EnumMap<>(ResourceType.class);
        initializeResources();
        this.controlledTiles = 0;
    }

    /**
     * Инициализирует ресурсы игрока начальными значениями.
     * <p>
     * Устанавливает начальные значения для всех типов ресурсов
     * согласно константам, определенным в классе {@link Constants}.
     *
     * @see Constants#INITIAL_RICE
     * @see Constants#INITIAL_WATER
     * @see Constants#INITIAL_PEASANTS
     */
    private void initializeResources() {
        resources.put(ResourceType.RICE, Constants.INITIAL_RICE);
        resources.put(ResourceType.WATER, Constants.INITIAL_WATER);
        resources.put(ResourceType.PEASANTS, Constants.INITIAL_PEASANTS);
    }

    /**
     * Добавляет указанное количество ресурса заданного типа.
     * <p>
     * Увеличивает текущее значение ресурса на указанное количество.
     * Если ресурс не был инициализирован, он будет создан с заданным значением.
     *
     * @param type тип добавляемого ресурса
     * @param amount количество для добавления (должно быть положительным или нулем)
     * @throws NullPointerException если {@code type} равен {@code null}
     * @throws IllegalArgumentException если {@code amount} отрицательное
     */
    public void addResource(ResourceType type, int amount) {
        resources.put(type, resources.get(type) + amount);
    }

    /**
     * Вычитает указанное количество ресурса заданного типа.
     * <p>
     * Проверяет, достаточно ли ресурса у игрока, и если да, уменьшает его количество.
     *
     * @param type тип вычитаемого ресурса
     * @param amount количество для вычитания (должно быть положительным или нулем)
     * @return {@code true} если у игрока достаточно ресурса и операция выполнена,
     *         {@code false} если ресурса недостаточно
     * @throws NullPointerException если {@code type} равен {@code null}
     * @throws IllegalArgumentException если {@code amount} отрицательное
     */
    public boolean deductResource(ResourceType type, int amount) {
        int current = resources.get(type);
        if (current >= amount) {
            resources.put(type, current - amount);
            return true;
        }
        return false;
    }

    /**
     * Возвращает текущее количество ресурса заданного типа.
     * <p>
     * Если ресурс не был инициализирован, возвращает 0.
     *
     * @param type тип запрашиваемого ресурса
     * @return текущее количество ресурса, или 0 если ресурс не инициализирован
     * @throws NullPointerException если {@code type} равен {@code null}
     */
    public int getResource(ResourceType type) {
        return resources.getOrDefault(type, 0);
    }

    /**
     * Устанавливает абсолютное значение ресурса заданного типа.
     * <p>
     * Заменяет текущее значение ресурса на указанное.
     *
     * @param type тип устанавливаемого ресурса
     * @param value новое значение ресурса (должно быть неотрицательным)
     * @throws NullPointerException если {@code type} равен {@code null}
     * @throws IllegalArgumentException если {@code value} отрицательное
     */
    public void setResource(ResourceType type, int value) {
        resources.put(type, value);
    }

    /**
     * Возвращает имя игрока.
     *
     * @return имя игрока
     */
    public String getName() { return name; }

    /**
     * Устанавливает новое имя игрока.
     *
     * @param name новое имя игрока
     * @throws NullPointerException если {@code name} равен {@code null}
     * @throws IllegalArgumentException если {@code name} пустая строка
     */
    public void setName(String name) { this.name = name; }

    /**
     * Возвращает количество территорий, контролируемых игроком.
     *
     * @return количество контролируемых территорий
     */
    public int getControlledTiles() { return controlledTiles; }

    /**
     * Устанавливает количество контролируемых территорий.
     * <p>
     * Используется при загрузке игры или сбросе состояния.
     *
     * @param controlledTiles новое количество контролируемых территорий
     * @throws IllegalArgumentException если {@code controlledTiles} отрицательное
     */
    public void setControlledTiles(int controlledTiles) {
        this.controlledTiles = controlledTiles;
    }

    /**
     * Увеличивает количество контролируемых территорий на 1.
     * <p>
     * Вызывается при успешном захвате новой территории.
     */
    public void incrementControlledTiles() {
        this.controlledTiles++;
    }

    /**
     * Возвращает строковое представление игрока.
     * <p>
     * Формат строки: "Имя: Рис=X, Вода=Y, Крестьяне=Z, Территории=W"
     *
     * @return строковое представление текущего состояния игрока
     */
    @Override
    public String toString() {
        return String.format("%s: Рис=%d, Вода=%d, Крестьяне=%d, Территории=%d",
                name,
                getResource(ResourceType.RICE),
                getResource(ResourceType.WATER),
                getResource(ResourceType.PEASANTS),
                controlledTiles);
    }
}