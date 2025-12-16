package main.java.com.turngame.model;

import java.io.Serializable;

/**
 * Класс, представляющий клетку игрового поля
 */
public class Tile implements Serializable {
    private int x;
    private int y;
    private Player owner;
    private int requiredPeasants;
    private boolean isControlled;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        this.owner = null;
        this.requiredPeasants = (int) (Math.random() * 5) + 1; // От 1 до 5
        this.isControlled = false;
    }

    public boolean canBeConqueredBy(Player player) {
        return !isControlled && player.getResource(ResourceType.PEASANTS) >= requiredPeasants;
    }

    public boolean conquer(Player player) {
        if (canBeConqueredBy(player)) {
            player.deductResource(ResourceType.PEASANTS, requiredPeasants);
            this.owner = player;
            this.isControlled = true;
            player.incrementControlledTiles();
            return true;
        }
        return false;
    }

    // Геттеры и сеттеры
    public int getX() { return x; }
    public int getY() { return y; }
    public Player getOwner() { return owner; }
    public void setOwner(Player owner) { this.owner = owner; }
    public int getRequiredPeasants() { return requiredPeasants; }
    public boolean isControlled() { return isControlled; }
    public void setControlled(boolean controlled) { isControlled = controlled; }

    @Override
    public String toString() {
        return String.format("Tile[%d,%d]: Требуется крестьян=%d, Контролируется=%s",
                x, y, requiredPeasants, owner != null ? owner.getName() : "никем");
    }
}