package main.java.com.turngame.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Утилитарный класс для централизованного логирования игровых событий.
 * <p>
 * Предоставляет статические методы для записи сообщений различного уровня
 * важности с использованием библиотеки Log4j 2. Все сообщения автоматически
 * записываются в файл, указанный в {@link Constants#LOG_FILE}.
 * <p>
 * Класс реализует следующие уровни логирования:
 * <ul>
 *   <li><strong>INFO</strong> - общая информация о ходе игры</li>
 *   <li><strong>WARN</strong> - предупреждения о нестандартных ситуациях</li>
 *   <li><strong>ERROR</strong> - ошибки выполнения с указанием исключений</li>
 * </ul>
 * <p>
 * Класс является утилитарным (все методы статические), поэтому создание
 * экземпляров запрещено.
 *
 * @author Playstall
 * @version 1.0
 * @see Logger
 * @see Constants#LOG_FILE
 * @since 1.0
 */
public class GameLogger {
    /**
     * Экземпляр логгера Log4j 2 для этого класса.
     * <p>
     * Используется для всех операций логирования. Настраивается через
     * конфигурационный файл Log4j 2 (обычно log4j2.xml).
     */
    private static final Logger logger = LogManager.getLogger(GameLogger.class);

    /**
     * Записывает информационное сообщение в лог.
     * <p>
     * Используется для регистрации нормальных событий игры, таких как:
     * <ul>
     *   <li>Выполнение игровых действий</li>
     *   <li>Изменение состояния игры</li>
     *   <li>Запуск и завершение игровых сессий</li>
     *   <li>Прочие значимые события</li>
     * </ul>
     *
     * @param message информационное сообщение для записи в лог
     * @throws NullPointerException если {@code message} равен {@code null}
     * @see Logger#info(String)
     */
    public static void log(String message) {
        logger.info(message);
    }

    /**
     * Записывает сообщение об ошибке в лог с указанием исключения.
     * <p>
     * Используется для регистрации критических ошибок, которые могут
     * привести к некорректной работе или завершению игры:
     * <ul>
     *   <li>Исключения во время выполнения игровых действий</li>
     *   <li>Ошибки ввода-вывода при работе с файлами</li>
     *   <li>Некорректные состояния игры</li>
     *   <li>Прочие необработанные исключения</li>
     * </ul>
     *
     * @param message описание ошибки для записи в лог
     * @param throwable исключение, вызвавшее ошибку (может быть {@code null})
     * @throws NullPointerException если {@code message} равен {@code null}
     * @see Logger#error(String, Throwable)
     */
    public static void logError(String message, Throwable throwable) {
        logger.error(message, throwable);
    }

    /**
     * Записывает предупреждающее сообщение в лог.
     * <p>
     * Используется для регистрации нестандартных ситуаций, которые
     * не являются критическими ошибками, но требуют внимания:
     * <ul>
     *   <li>Неудачные попытки выполнения действий (недостаточно ресурсов и т.д.)</li>
     *   <li>Неоптимальные или подозрительные условия игры</li>
     *   <li>Устаревшие или нерекомендуемые операции</li>
     *   <li>Прочие ситуации, требующие внимания разработчика или игрока</li>
     * </ul>
     *
     * @param message предупреждающее сообщение для записи в лог
     * @throws NullPointerException если {@code message} равен {@code null}
     * @see Logger#warn(String)
     */
    public static void logWarning(String message) {
        logger.warn(message);
    }
}