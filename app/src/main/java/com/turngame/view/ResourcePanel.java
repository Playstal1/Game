package main.java.com.turngame.view;

import main.java.com.turngame.model.Player;
import main.java.com.turngame.model.ResourceType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Панель для отображения текущих ресурсов игрока в пользовательском интерфейсе.
 * <p>
 * Этот класс представляет собой компонент пользовательского интерфейса, который
 * визуализирует текущее состояние ресурсов игрока в виде группы меток.
 * Панель отображает четыре ключевых показателя:
 * <ul>
 *   <li>Количество риса ({@link ResourceType#RICE})</li>
 *   <li>Количество воды ({@link ResourceType#WATER})</li>
 *   <li>Количество крестьян ({@link ResourceType#PEASANTS})</li>
 *   <li>Количество контролируемых территорий</li>
 * </ul>
 * <p>
 * Панель реализована как контейнер {@link VBox}, содержащий заголовок
 * и четыре метки {@link Label} для отображения значений ресурсов.
 * Предоставляет метод {@link #updateResources(Player)} для динамического
 * обновления отображаемых значений при изменении состояния игры.
 *
 * @author Playstall
 * @version 1.0
 * @see Player
 * @see ResourceType
 * @see VBox
 * @see Label
 * @since 1.0
 */
public class ResourcePanel {
    /**
     * Основной контейнер панели ресурсов, организованный по вертикали.
     * <p>
     * Содержит все элементы интерфейса панели: заголовок и метки ресурсов.
     */
    private VBox panel;

    /**
     * Метка для отображения текущего количества риса у игрока.
     */
    private Label riceLabel;

    /**
     * Метка для отображения текущего количества риса у игрока.
     */
    private Label waterLabel;

    /**
     * Метка для отображения текущего количества крестьян у игрока.
     */
    private Label peasantsLabel;

    /**
     * Метка для отображения текущего количества контролируемых территорий у игрока.
     */
    private Label tilesLabel;

    /**
     * Создает новую панель ресурсов с указанным заголовком.
     * <p>
     * Инициализирует все компоненты интерфейса панели и устанавливает
     * начальные значения ресурсов в 0. Заголовок панели используется
     * для идентификации игрока (например, "Ресурсы игрока" или "Ресурсы компьютера").
     *
     * @param title заголовок панели, отображаемый в верхней части
     * @throws NullPointerException если {@code title} равен {@code null}
     * @throws IllegalArgumentException если {@code title} пустая строка
     *
     * @see #createPanel(String)
     */
    public ResourcePanel(String title) {
        createPanel(title);
    }

    /**
     * Создает и настраивает все компоненты панели ресурсов.
     * <p>
     * Метод выполняет следующие действия:
     * <ol>
     *   <li>Создает основной контейнер {@link VBox}</li>
     *   <li>Настраивает отступы и выравнивание</li>
     *   <li>Добавляет CSS-класс для стилизации</li>
     *   <li>Создает заголовок панели</li>
     *   <li>Создает четыре метки для ресурсов с начальными значениями "0"</li>
     *   <li>Добавляет все компоненты в контейнер</li>
     * </ol>
     *
     * @param title заголовок панели, передаваемый из конструктора
     *
     * @see VBox
     * @see Label
     * @see #createResourceLabel(String)
     */
    private void createPanel(String title) {
        panel = new VBox(5);
        panel.setPadding(new Insets(10));
        panel.setAlignment(Pos.TOP_LEFT);
        panel.getStyleClass().add("resource-panel");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        riceLabel = createResourceLabel("Рис: 0");
        waterLabel = createResourceLabel("Вода: 0");
        peasantsLabel = createResourceLabel("Крестьяне: 0");
        tilesLabel = createResourceLabel("Территории: 0");

        panel.getChildren().addAll(titleLabel, riceLabel, waterLabel, peasantsLabel, tilesLabel);
    }

    /**
     * Создает и настраивает метку для отображения значения ресурса.
     * <p>
     * Метод создает новый объект {@link Label} с указанным текстом
     * и применяет стандартные стили для всех меток ресурсов.
     *
     * @param text начальный текст метки (например, "Рис: 0")
     * @return настроенная метка {@link Label} для отображения ресурса
     * @throws NullPointerException если {@code text} равен {@code null}
     */
    private Label createResourceLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 12px;");
        return label;
    }

    /**
     * Обновляет значения ресурсов на панели на основе текущего состояния игрока.
     * <p>
     * Метод извлекает текущие значения ресурсов из объекта {@link Player}
     * и обновляет текст соответствующих меток. Если переданный игрок
     * равен {@code null}, значения не обновляются.
     * <p>
     * Обновляются следующие ресурсы:
     * <ul>
     *   <li>Рис - через {@link Player#getResource(ResourceType)} с параметром {@link ResourceType#RICE}</li>
     *   <li>Вода - через {@link Player#getResource(ResourceType)} с параметром {@link ResourceType#WATER}</li>
     *   <li>Крестьяне - через {@link Player#getResource(ResourceType)} с параметром {@link ResourceType#PEASANTS}</li>
     *   <li>Территории - через {@link Player#getControlledTiles()}</li>
     * </ul>
     *
     * @param player объект игрока, чьи ресурсы нужно отобразить
     *
     * @see Player#getResource(ResourceType)
     * @see Player#getControlledTiles()
     */
    public void updateResources(Player player) {
        if (player != null) {
            riceLabel.setText("Рис: " + player.getResource(ResourceType.RICE));
            waterLabel.setText("Вода: " + player.getResource(ResourceType.WATER));
            peasantsLabel.setText("Крестьяне: " + player.getResource(ResourceType.PEASANTS));
            tilesLabel.setText("Территории: " + player.getControlledTiles());
        }
    }

    /**
     * Возвращает основной контейнер панели ресурсов.
     * <p>
     * Этот метод предоставляет доступ к корневому элементу панели,
     * который может быть добавлен в любую компоновку JavaFX.
     *
     * @return контейнер {@link VBox}, содержащий все элементы панели ресурсов
     */
    public VBox getPanel() {
        return panel;
    }
}