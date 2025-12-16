package main.java.com.turngame.model;

import main.java.com.turngame.util.Constants;

import java.io.Serializable;

/**
 * Класс, представляющий здание в игре.
 * <p>
 * Здания являются основными игровыми объектами, которые могут быть построены
 * игроками для получения различных преимуществ и производства ресурсов.
 * Каждое здание имеет тип, определяющий его функциональность и стоимость,
 * и принадлежит определенному игроку.
 * <p>
 * Класс реализует интерфейс {@link Serializable}, что позволяет сохранять
 * и загружать состояние зданий при сериализации игрового состояния.
 *
 * @author Playstall
 * @version 1.0
 * @see BuildingType
 * @see Player
 * @see Serializable
 * @since 1.0
 */
public class Building implements Serializable {

    /**
     * Перечисление, определяющее типы зданий, доступных в игре.
     * <p>
     * Каждый тип здания имеет:
     * <ul>
     *   <li>Название для отображения в интерфейсе</li>
     *   <li>Стоимость строительства в различных ресурсах</li>
     *   <li>Уникальные характеристики и функциональность</li>
     * </ul>
     *
     * @see Constants
     */
    public enum BuildingType {
        /**
         * Дом крестьянина - базовое жилое здание, которое производит крестьян.
         * <p>
         * Производит крестьян в конце каждого игрового дня.
         * Является основой для расширения населения и контроля территории.
         */
        PEASANT_HOUSE("Дом крестьянина", Constants.HOUSE_RICE_COST,
                Constants.HOUSE_WATER_COST, Constants.HOUSE_PEASANT_COST);

        private final String name;
        private final int riceCost;
        private final int waterCost;
        private final int peasantCost;

        /**
         * Создает новый тип здания с указанными характеристиками.
         *
         * @param name название здания для отображения в интерфейсе
         * @param riceCost стоимость строительства в рисе
         * @param waterCost стоимость строительства в воде
         * @param peasantCost стоимость строительства в крестьянах
         * @throws NullPointerException если {@code name} равен {@code null}
         */
        BuildingType(String name, int riceCost, int waterCost, int peasantCost) {
            this.name = name;
            this.riceCost = riceCost;
            this.waterCost = waterCost;
            this.peasantCost = peasantCost;
        }

        /**
         * Возвращает отображаемое название типа здания.
         *
         * @return название здания
         */
        public String getName() { return name; }

        /**
         * Возвращает стоимость строительства здания в рисе.
         *
         * @return количество риса, необходимое для строительства
         */
        public int getRiceCost() { return riceCost; }

        /**
         * Возвращает стоимость строительства здания в воде.
         *
         * @return количество воды, необходимое для строительства
         */
        public int getWaterCost() { return waterCost; }

        /**
         * Возвращает стоимость строительства здания в крестьянах.
         *
         * @return количество крестьян, необходимое для строительства
         */
        public int getPeasantCost() { return peasantCost; }
    }

    private BuildingType type;
    private Player owner;

    /**
     * Создает новое здание указанного типа, принадлежащее указанному игроку.
     *
     * @param type тип создаваемого здания
     * @param owner игрок-владелец здания
     * @throws NullPointerException если {@code type} или {@code owner} равны {@code null}
     */
    public Building(BuildingType type, Player owner) {
        this.type = type;
        this.owner = owner;
    }

    /**
     * Возвращает тип этого здания.
     *
     * @return тип здания
     */
    public BuildingType getType() { return type; }

    /**
     * Возвращает игрока-владельца этого здания.
     *
     * @return владелец здания
     */
    public Player getOwner() { return owner; }

    /**
     * Производит ресурсы (крестьян) в конце игрового дня.
     * <p>
     * Метод вызывается в конце каждого дня для всех зданий на карте.
     * В зависимости от типа здания производится соответствующий ресурс:
     * <ul>
     *   <li>Для {@link BuildingType#PEASANT_HOUSE} - добавляет крестьян владельцу</li>
     * </ul>
     * Количество производимых ресурсов определяется константами в классе {@link Constants}.
     *
     * @see Constants#PEASANT_PRODUCTION_RATE
     * @see Player#addResource(ResourceType, int)
     */
    public void producePeasants() {
        if (type == BuildingType.PEASANT_HOUSE) {
            owner.addResource(ResourceType.PEASANTS, Constants.PEASANT_PRODUCTION_RATE);
        }
    }
}