package main.java.com.turngame.util;

import main.java.com.turngame.model.GameState;
import main.java.com.turngame.exception.SaveLoadException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Утилита для сохранения и загрузки состояния игры с использованием сериализации Java.
 * <p>
 * Этот класс предоставляет методы для сохранения текущего состояния игры в файл
 * и восстановления состояния из файла. Использует стандартную сериализацию Java
 * ({@link ObjectOutputStream} и {@link ObjectInputStream}) для работы с объектами.
 * <p>
 * Основные возможности:
 * <ul>
 *   <li>Сохранение состояния игры в указанный файл</li>
 *   <li>Загрузка состояния игры из файла</li>
 *   <li>Работа с сохранениями через стандартные пути</li>
 *   <li>Управление файлами сохранений (проверка, удаление, получение списка)</li>
 * </ul>
 * <p>
 * Все операции с файлами выполняются с использованием современных API NIO.2 ({@link Path}, {@link Files}).
 *
 * @author Playstall
 * @version 1.0
 * @see GameState
 * @see SaveLoadException
 * @see ObjectOutputStream
 * @see ObjectInputStream
 * @see Files
 * @since 1.0
 */
public class GameSaver {

    /**
     * Сохраняет состояние игры в указанный файл с использованием сериализации.
     * <p>
     * Метод создает или перезаписывает файл по указанному пути и записывает
     * сериализованный объект {@link GameState}. В случае успеха логируется
     * событие сохранения.
     *
     * @param gameState объект состояния игры для сохранения
     * @param filename  имя файла (может включать путь) для сохранения
     * @return {@code true} если сохранение успешно завершено
     * @throws SaveLoadException если произошла ошибка ввода-вывода при записи файла
     * @throws NullPointerException если {@code gameState} или {@code filename} равны {@code null}
     * @throws IllegalArgumentException если {@code filename} пустая строка
     * @see ObjectOutputStream
     */
    public boolean saveGame(GameState gameState, String filename) throws SaveLoadException {
        Path filePath = Paths.get(filename);

        try (ObjectOutputStream oos = new ObjectOutputStream(
                Files.newOutputStream(filePath))) {

            oos.writeObject(gameState);
            GameLogger.log("Игра сохранена в файл: " + filename);
            oos.flush();
            return true;

        } catch (IOException e) {
            throw new SaveLoadException("Ошибка при сохранении игры", e);
        }
    }

    /**
     * Загружает состояние игры из указанного файла.
     * <p>
     * Метод проверяет существование файла, затем десериализует объект
     * {@link GameState} из файла. В случае успеха логируется событие загрузки.
     *
     * @param filename имя файла (может включать путь) для загрузки
     * @return объект {@link GameState}, восстановленный из файла
     * @throws SaveLoadException если файл не найден или произошла ошибка
     *         ввода-вывода/десериализации
     * @throws NullPointerException если {@code filename} равен {@code null}
     * @throws IllegalArgumentException если {@code filename} пустая строка
     * @see ObjectInputStream
     */
    public GameState loadGame(String filename) throws SaveLoadException {
        Path filePath = Paths.get(filename);

        if (!Files.exists(filePath)) {
            throw new SaveLoadException("Файл не найден: " + filename);
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                Files.newInputStream(filePath))) {

            GameState gameState = (GameState) ois.readObject();
            GameLogger.log("Игра загружена из файла: " + filename);
            return gameState;

        } catch (IOException | ClassNotFoundException e) {
            throw new SaveLoadException("Ошибка при загрузке игры", e);
        }
    }

    /**
     * Сохраняет игру в стандартный файл, указанный в {@link Constants#SAVE_FILE}.
     * <p>
     * Удобный метод для быстрого сохранения в файл по умолчанию.
     *
     * @param gameState объект состояния игры для сохранения
     * @return {@code true} если сохранение успешно завершено
     * @throws SaveLoadException если произошла ошибка при сохранении
     * @see #saveGame(GameState, String)
     * @see Constants#SAVE_FILE
     */
    public boolean saveGame(GameState gameState) throws SaveLoadException {
        return saveGame(gameState, Constants.SAVE_FILE);
    }

    /**
     * Загружает игру из стандартного файла, указанного в {@link Constants#SAVE_FILE}.
     * <p>
     * Удобный метод для быстрой загрузки из файла по умолчанию.
     *
     * @return объект {@link GameState}, восстановленный из стандартного файла
     * @throws SaveLoadException если произошла ошибка при загрузке
     * @see #loadGame(String)
     * @see Constants#SAVE_FILE
     */
    public GameState loadGame() throws SaveLoadException {
        return loadGame(Constants.SAVE_FILE);
    }

    /**
     * Проверяет существование стандартного файла сохранения.
     * <p>
     * Используется для определения, можно ли загрузить игру без попытки
     * загрузки (например, для отображения кнопки "Загрузить игру").
     *
     * @return {@code true} если стандартный файл сохранения существует,
     *         {@code false} в противном случае
     * @see Constants#SAVE_FILE
     */
    public boolean saveFileExists() {
        return Files.exists(Paths.get(Constants.SAVE_FILE));
    }

    /**
     * Удаляет стандартный файл сохранения, если он существует.
     * <p>
     * Используется для очистки сохранений (например, при начале новой игры
     * или удалении поврежденного сохранения). В случае ошибки удаления
     * логирует ошибку, но не выбрасывает исключение.
     *
     * @return {@code true} если файл был успешно удален или не существовал,
     *         {@code false} если произошла ошибка при удалении
     * @see Constants#SAVE_FILE
     */
    public boolean deleteSaveFile() {
        try {
            return Files.deleteIfExists(Paths.get(Constants.SAVE_FILE));
        } catch (IOException e) {
            GameLogger.logError("Ошибка при удалении файла сохранения", e);
            return false;
        }
    }

    /**
     * Получает список всех файлов сохранений в специальной папке "saves".
     * <p>
     * Создает папку "saves", если она не существует. Возвращает только файлы
     * с расширением ".sav". Используется для реализации менеджера сохранений
     * с возможностью выбора конкретного файла.
     *
     * @return массив объектов {@link File}, представляющих файлы сохранений,
     *         или пустой массив, если файлов нет или произошла ошибка
     */
    public File[] listSaveFiles() {
        File savesDir = new File("saves");
        if (!savesDir.exists()) {
            savesDir.mkdir();
        }
        return savesDir.listFiles((dir, name) -> name.endsWith(".sav"));
    }
}