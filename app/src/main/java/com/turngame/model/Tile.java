package main.java.com.turngame.model;

import java.io.Serializable;

/**
 * Класс, представляющий клетку (тайл) игрового поля.
 * <p>
 * Каждая клетка является единицей территории, которую можно контролировать.
 * Клетки имеют координаты на карте, требования для захвата и могут принадлежать
 * определенному игроку. Захват клеток является основной целью игры.
 * <p>
 * Класс реализует интерфейс {@link Serializable}, что позволяет сохранять
 * состояние карты при сериализации игрового состояния.
 *
 * @author Playstall
 * @version 1.0
 * @see Player
 * @see Serializable
 * @since 1.0
 */
public class Tile implements Serializable {
    /**
     * Координата X клетки на игровой карте.
     * <p>
     * Значение от 0 до {@link main.java.com.turngame.util.Constants#MAP_WIDTH}-1.
     */
    private int x;

    /**
     * Координата Y клетки на игровой карте.
     * <p>
     * Значение от 0 до {@link main.java.com.turngame.util.Constants#MAP_HEIGHT}-1.
     */
    private int y;

    /**
     * Игрок, который контролирует эту клетку.
     * <p>
     * Значение {@code null} означает, что клетка никому не принадлежит.
     */
    private Player owner;

    /**
     * Количество крестьян, необходимое для захвата этой клетки.
     * <p>
     * Генерируется случайным образом в диапазоне от 1 до 5 при создании клетки.
     * Более высокие значения делают клетку труднодоступной для захвата.
     */
    private int requiredPeasants;

    /**
     * Флаг, указывающий, контролируется ли клетка каким-либо игроком.
     * <p>
     * Связан с полем {@link #owner}: если {@code owner} не {@code null},
     * то {@code isControlled} должно быть {@code true}.
     */
    private boolean isControlled;

    /**
     * Создает новую клетку с указанными координатами.
     * <p>
     * Инициализирует клетку как неконтролируемую (ничью) и случайным образом
     * устанавливает количество крестьян, необходимое для захвата.
     *
     * @param x координата X клетки на карте
     * @param y координата Y клетки на карте
     * @throws IllegalArgumentException если координаты выходят за допустимые пределы
     */
    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        this.owner = null;
        this.requiredPeasants = (int) (Math.random() * 5) + 1; // От 1 до 5
        this.isControlled = false;
    }

    /**
     * Проверяет, может ли указанный игрок захватить эту клетку.
     * <p>
     * Клетка может быть захвачена, если:
     * <ul>
     *   <li>Она еще не контролируется другим игроком</li>
     *   <li>У игрока достаточно крестьян для захвата (не меньше {@link #requiredPeasants})</li>
     * </ul>
     *
     * @param player игрок, который пытается захватить клетку
     * @return {@code true} если игрок может захватить клетку,
     *         {@code false} в противном случае
     * @throws NullPointerException если {@code player} равен {@code null}
     * @see #conquer(Player)
     */
    public boolean canBeConqueredBy(Player player) {
        return !isControlled && player.getResource(ResourceType.PEASANTS) >= requiredPeasants;
    }

    /**
     * Попытка захвата клетки указанным игроком.
     * <p>
     * Если условия захвата выполняются (проверка через {@link #canBeConqueredBy(Player)}):
     * <ul>
     *   <li>У игрока вычитается необходимое количество крестьян</li>
     *   <li>Игрок становится владельцем клетки</li>
     *   <li>Клетка помечается как контролируемая</li>
     *   <li>Увеличивается счетчик контролируемых территорий игрока</li>
     * </ul>
     *
     * @param player игрок, который пытается захватить клетку
     * @return {@code true} если клетка успешно захвачена,
     *         {@code false} если захват не удался
     * @throws NullPointerException если {@code player} равен {@code null}
     * @see #canBeConqueredBy(Player)
     * @see Player#deductResource(ResourceType, int)
     * @see Player#incrementControlledTiles()
     */
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

    /**
     * Возвращает координату X клетки.
     *
     * @return координата X
     */
    public int getX() { return x; }

    /**
     * Возвращает координату Y клетки.
     *
     * @return координата Y
     */
    public int getY() { return y; }

    /**
     * Возвращает владельца клетки.
     *
     * @return объект {@link Player}, контролирующий клетку,
     *         или {@code null} если клетка свободна
     */
    public Player getOwner() { return owner; }

    /**
     * Устанавливает владельца клетки.
     * <p>
     * Обычно используется при загрузке игры. Для обычного захвата
     * используйте метод {@link #conquer(Player)}.
     *
     * @param owner новый владелец клетки, может быть {@code null}
     */
    public void setOwner(Player owner) { this.owner = owner; }

    /**
     * Возвращает количество крестьян, необходимое для захвата клетки.
     *
     * @return требуемое количество крестьян
     */
    public int getRequiredPeasants() { return requiredPeasants; }

    /**
     * Проверяет, контролируется ли клетка каким-либо игроком.
     *
     * @return {@code true} если клетка контролируется,
     *         {@code false} если клетка свободна
     */
    public boolean isControlled() { return isControlled; }

    /**
     * Устанавливает флаг контроля клетки.
     * <p>
     * Обычно используется при загрузке игры. Для обычного захвата
     * используйте метод {@link #conquer(Player)}.
     *
     * @param controlled новое значение флага контроля
     */
    public void setControlled(boolean controlled) { isControlled = controlled; }

    /**
     * Возвращает строковое представление клетки.
     * <p>
     * Формат строки: "Tile[X,Y]: Требуется крестьян=N, Контролируется=ИМЯ"
     * Если клетка не контролируется, вместо имени будет "никем".
     *
     * @return строковое представление текущего состояния клетки
     */
    @Override
    public String toString() {
        return String.format("Tile[%d,%d]: Требуется крестьян=%d, Контролируется=%s",
                x, y, requiredPeasants, owner != null ? owner.getName() : "никем");
    }
}