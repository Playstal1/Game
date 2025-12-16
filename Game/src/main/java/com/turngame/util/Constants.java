package main.java.com.turngame.util;

public class Constants {
    // Размеры карты
    public static final int MAP_WIDTH = 10;
    public static final int MAP_HEIGHT = 10;

    // Ресурсы
    public static final int INITIAL_RICE = 50;
    public static final int INITIAL_WATER = 50;
    public static final int INITIAL_PEASANTS = 3;

    // Стоимости
    public static final int HOUSE_RICE_COST = 20;
    public static final int HOUSE_WATER_COST = 10;
    public static final int HOUSE_PEASANT_COST = 1;

    // Производство
    public static final int RICE_GROWTH_PER_DAY = 5;
    public static final int WATER_PER_DAY = 10;
    public static final int PEASANT_PRODUCTION_RATE = 1;

    // Победа
    public static final double WIN_PERCENTAGE = 0.5;

    // Файлы
    public static final String SAVE_FILE = "game_save.dat";
    public static final String LOG_FILE = "game_log.log";
}