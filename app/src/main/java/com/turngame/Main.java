package main.java.com.turngame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.java.com.turngame.view.GameView;

/**
 * Главный класс приложения, являющийся точкой входа в игру.
 * <p>
 * Этот класс наследуется от {@link Application} и отвечает за инициализацию
 * и запуск графического интерфейса игры на платформе JavaFX. Он выполняет:
 * <ul>
 *   <li>Настройку и конфигурацию окружения JavaFX</li>
 *   <li>Инициализацию системы логирования Log4j 2</li>
 *   <li>Создание и отображение основного игрового окна</li>
 *   <li>Загрузку CSS-стилей для оформления интерфейса</li>
 *   <li>Обработку ошибок запуска приложения</li>
 * </ul>
 * <p>
 * Класс следует стандартному шаблону JavaFX-приложений, где метод {@link #start(Stage)}
 * является основной точкой входа для графического интерфейса, а метод {@link #main(String[])}
 * обеспечивает совместимость с обычными Java-приложениями.
 *
 * @author Playstall
 * @version 1.0
 * @see Application
 * @see GameView
 * @see <a href="https://openjfx.io/javadoc/17/javafx.graphics/javafx/application/Application.html">JavaFX Application Documentation</a>
 * @since 1.0
 */
public class Main extends Application {

    /**
     * Основной метод запуска JavaFX-приложения.
     * <p>
     * Этот метод вызывается платформой JavaFX и выполняет инициализацию
     * пользовательского интерфейса игры. Он отвечает за:
     * <ol>
     *   <li>Настройку системы логирования Log4j 2</li>
     *   <li>Создание экземпляра основного представления игры ({@link GameView})</li>
     *   <li>Создание сцены (Scene) с размерами 1200x800 пикселей</li>
     *   <li>Загрузку CSS-стилей из ресурсов приложения</li>
     *   <li>Настройку и отображение главного окна (Stage)</li>
     *   <li>Обработку и логирование возможных ошибок инициализации</li>
     * </ol>
     *
     * @param primaryStage главное окно приложения, предоставляемое платформой JavaFX
     * @throws IllegalStateException если не удалось загрузить конфигурацию Log4j 2
     * @throws RuntimeException если произошла критическая ошибка при создании интерфейса
     *
     * @see GameView#createMainView()
     * @see Scene
     * @see Stage
     */
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

    /**
     * Традиционная точка входа для Java-приложений.
     * <p>
     * Этот метод обеспечивает совместимость с обычными Java-приложениями
     * и запускает JavaFX-приложение. Он делегирует управление запуском
     * методу {@link Application#launch(String...)} платформы JavaFX.
     * <p>
     * Метод обрабатывает аргументы командной строки, которые могут быть
     * использованы для настройки приложения (например, для отладки или
     * специальных режимов работы).
     *
     * @param args аргументы командной строки, передаваемые приложению.
     *             Могут включать:
     *             <ul>
     *               <li>Параметры настройки логирования</li>
     *               <li>Флаги отладки</li>
     *               <li>Пути к пользовательским конфигурационным файлам</li>
     *               <li>Имена файлов сохранений для автоматической загрузки</li>
     *             </ul>
     *
     * @see Application#launch(String...)
     */
    public static void main(String[] args) {
        launch(args);
    }
}