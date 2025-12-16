package main.java.com.turngame.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Утилитарный класс для логирования игровых событий
 */
public class GameLogger {
    private static final Logger logger = LogManager.getLogger(GameLogger.class);

    public static void log(String message) {
        logger.info(message);
    }

    public static void logError(String message, Throwable throwable) {
        logger.error(message, throwable);
    }

    public static void logWarning(String message) {
        logger.warn(message);
    }
}