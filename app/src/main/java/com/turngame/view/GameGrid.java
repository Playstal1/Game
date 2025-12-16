package main.java.com.turngame.view;

import main.java.com.turngame.model.Tile;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;

/**
 * Класс для отображения игрового поля в виде интерактивной сетки клеток.
 * <p>
 * Этот класс отвечает за визуализацию игровой карты ({@link Tile}[][]) в
 * пользовательском интерфейсе с использованием JavaFX {@link GridPane}.
 * Каждая клетка представляется в виде {@link Button}, что позволяет
 * пользователю взаимодействовать с игровым полем.
 * <p>
 * Основные функции:
 * <ul>
 *   <li>Динамическое обновление отображения при изменении состояния игры</li>
 *   <li>Визуальное различение контролируемых и свободных клеток</li>
 *   <li>Отображение информации о клетках через всплывающие подсказки ({@link Tooltip})</li>
 *   <li>Настройка внешнего вида клеток через CSS-классы</li>
 * </ul>
 * <p>
 * Класс предназначен для использования в составе пользовательского интерфейса
 * и должен интегрироваться с основным представлением игры ({@link GameView}).
 *
 * @author Playstall
 * @version 1.0
 * @see Tile
 * @see GridPane
 * @see Button
 * @see Tooltip
 * @since 1.0
 */
public class GameGrid {
    /**
     * Основной контейнер JavaFX для размещения клеток в виде сетки.
     */
    private GridPane gridPane;
    /**
     * Ширина игровой сетки (количество столбцов).
     */
    private int width;
    /**
     * Высота игровой сетки (количество строк).
     */
    private int height;

    /**
     * Создает новый экземпляр игровой сетки с указанными размерами.
     * <p>
     * Инициализирует {@link GridPane} с заданными размерами и настраивает
     * его внешний вид: устанавливает отступы между клетками и выравнивание
     * по центру.
     *
     * @param width  количество столбцов в сетке (должно быть положительным)
     * @param height количество строк в сетке (должно быть положительным)
     * @throws IllegalArgumentException если {@code width} или {@code height} меньше или равно 0
     *
     * @see GridPane
     * @see GridPane#setHgap(double)
     * @see GridPane#setVgap(double)
     * @see Pos#CENTER
     */
    public GameGrid(int width, int height) {
        this.width = width;
        this.height = height;
        gridPane = new GridPane();
        gridPane.setHgap(2);
        gridPane.setVgap(2);
        gridPane.setAlignment(Pos.CENTER);
    }

    /**
     * Обновляет отображение игровой сетки на основе текущего состояния карты.
     * <p>
     * Метод полностью очищает текущее содержимое сетки и перерисовывает
     * все клетки в соответствии с переданной картой. Для каждой клетки
     * создается кнопка с соответствующей визуализацией и информацией.
     *
     * @param map двумерный массив клеток, представляющий текущее состояние игровой карты
     * @throws NullPointerException если {@code map} равен {@code null}
     * @throws IllegalArgumentException если размеры массива не соответствуют
     *         установленным {@code width} и {@code height}
     *
     * @see #createTileButton(Tile)
     * @see GridPane#getChildren()
     * @see GridPane#add(javafx.scene.Node, int, int)
     */
    public void updateGrid(Tile[][] map) {
        gridPane.getChildren().clear();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (map[x][y] != null) {
                    Button tileButton = createTileButton(map[x][y]);
                    gridPane.add(tileButton, x, y);
                }
            }
        }
    }

    /**
     * Создает кнопку для представления одной клетки на игровой сетке.
     * <p>
     * Метод настраивает внешний вид и поведение кнопки в зависимости от
     * состояния клетки:
     * <ul>
     *   <li>Для свободных клеток отображается количество требуемых крестьян</li>
     *   <li>Для контролируемых клеток отображается буква "C" (Controlled)</li>
     *   <li>Добавляется всплывающая подсказка с подробной информацией</li>
     *   <li>Применяются соответствующие CSS-классы для стилизации</li>
     * </ul>
     *
     * @param tile клетка, для которой создается кнопка
     * @return настроенная кнопка {@link Button}, представляющая клетку
     * @throws NullPointerException если {@code tile} равен {@code null}
     *
     * @see Button
     * @see Tooltip
     * @see Tile#isControlled()
     * @see Tile#getRequiredPeasants()
     * @see Tile#getX()
     * @see Tile#getY()
     */
    private Button createTileButton(Tile tile) {
        Button button = new Button();
        button.setMinSize(40, 40);
        button.setMaxSize(40, 40);
        button.getStyleClass().add("grid-cell");

        // Настраиваем внешний вид в зависимости от состояния клетки
        if (tile.isControlled()) {
            button.getStyleClass().add("controlled-cell");
            button.setText("C");
        } else {
            button.setText(String.valueOf(tile.getRequiredPeasants()));
        }

        // Добавляем всплывающую подсказку
        String tooltipText = String.format("Клетка [%d,%d]\nТребуется крестьян: %d\n%s",
                tile.getX(), tile.getY(),
                tile.getRequiredPeasants(),
                tile.isControlled() ? "Контролируется" : "Свободна");

        Tooltip tooltip = new Tooltip(tooltipText);
        Tooltip.install(button, tooltip);

        return button;
    }

    /**
     * Возвращает основной контейнер сетки для использования в пользовательском интерфейсе.
     * <p>
     * Метод предоставляет доступ к {@link GridPane}, который может быть
     * добавлен в любую компоновку JavaFX. Изменения в сетке (добавление,
     * удаление элементов) должны выполняться через методы этого класса.
     *
     * @return контейнер {@link GridPane} с текущим состоянием игровой сетки
     */
    public GridPane getGridPane() {
        return gridPane;
    }
}