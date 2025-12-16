package main.java.com.turngame.view;

import main.java.com.turngame.model.Player;
import main.java.com.turngame.model.ResourceType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Панель для отображения ресурсов игрока
 */
public class ResourcePanel {
    private VBox panel;
    private Label riceLabel;
    private Label waterLabel;
    private Label peasantsLabel;
    private Label tilesLabel;

    public ResourcePanel(String title) {
        createPanel(title);
    }

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

    private Label createResourceLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 12px;");
        return label;
    }

    public void updateResources(Player player) {
        if (player != null) {
            riceLabel.setText("Рис: " + player.getResource(ResourceType.RICE));
            waterLabel.setText("Вода: " + player.getResource(ResourceType.WATER));
            peasantsLabel.setText("Крестьяне: " + player.getResource(ResourceType.PEASANTS));
            tilesLabel.setText("Территории: " + player.getControlledTiles());
        }
    }

    public VBox getPanel() {
        return panel;
    }
}