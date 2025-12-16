package main.java.com.turngame.util;

import main.java.com.turngame.model.GameState;
import main.java.com.turngame.exception.SaveLoadException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Утилита для сохранения и загрузки состояния игры
 */
public class GameSaver {

    /**
     * Сохраняет состояние игры в файл
     * @param gameState состояние игры
     * @param filename имя файла
     * @return true если сохранение успешно
     * @throws SaveLoadException если произошла ошибка
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
     * Загружает состояние игры из файла
     * @param filename имя файла
     * @return объект состояния игры или null при ошибке
     * @throws SaveLoadException если произошла ошибка
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
     * Сохраняет игру в стандартный файл
     */
    public boolean saveGame(GameState gameState) throws SaveLoadException {
        return saveGame(gameState, Constants.SAVE_FILE);
    }

    /**
     * Загружает игру из стандартного файла
     */
    public GameState loadGame() throws SaveLoadException {
        return loadGame(Constants.SAVE_FILE);
    }

    /**
     * Проверяет существование файла сохранения
     */
    public boolean saveFileExists() {
        return Files.exists(Paths.get(Constants.SAVE_FILE));
    }

    /**
     * Удаляет файл сохранения
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
     * Получает список всех файлов сохранения
     */
    public File[] listSaveFiles() {
        File savesDir = new File("saves");
        if (!savesDir.exists()) {
            savesDir.mkdir();
        }
        return savesDir.listFiles((dir, name) -> name.endsWith(".sav"));
    }
}