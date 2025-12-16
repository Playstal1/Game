package main.java.com.turngame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.java.com.turngame.view.GameView;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Инициализация логгера
        System.setProperty("log4j.configurationFile", "log4j2.xml");

        try {
            GameView gameView = new GameView();
            Scene scene = new Scene(gameView.createMainView(), 1200, 800);

            // Загружаем CSS стили
            try {
                scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
            } catch (Exception e) {
                System.err.println("Не удалось загрузить CSS файл: " + e.getMessage());
            }

            primaryStage.setTitle("Пошаговая стратегия: Вода и Рис");
            primaryStage.setScene(scene);
            primaryStage.show();

            System.out.println("Приложение успешно запущено");

        } catch (Exception e) {
            System.err.println("Ошибка при запуске приложения: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}