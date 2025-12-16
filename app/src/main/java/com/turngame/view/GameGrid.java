package main.java.com.turngame.view;

import main.java.com.turngame.model.Tile;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;

/**
 * Класс для отображения игрового поля в виде сетки
 */
public class GameGrid {
    private GridPane gridPane;
    private int width;
    private int height;

    public GameGrid(int width, int height) {
        this.width = width;
        this.height = height;
        gridPane = new GridPane();
        gridPane.setHgap(2);
        gridPane.setVgap(2);
        gridPane.setAlignment(Pos.CENTER);
    }

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

    public GridPane getGridPane() {
        return gridPane;
    }
}