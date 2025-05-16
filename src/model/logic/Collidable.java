package model.logic;

import model.geometry.Position;
import model.geometry.Size;
import org.jetbrains.annotations.NotNull;

public interface Collidable {

    Position position();

    Size size();

    /**
     * Проверяет, пересекается ли объект с заданной позицией.
     */
    default boolean intersects(@NotNull Position position) {
        return checkCollision(
                position(),
                size(),
                position,
                new Size(0, 0) // Точечная коллизия
        );
    }

    /**
     * Проверяет, пересекается ли текущий объект с другим Collidable.
     */
    default boolean intersects(@NotNull Collidable other) {
        return checkCollision(
                position(),
                size(),
                other.position(),
                other.size()
        );
    }

    /**
     * Проверяет, пересекается ли текущий объект с позицией и размерами.
     */
    default boolean intersects(@NotNull Position position, @NotNull Size size) {
        return checkCollision(
                position(),
                size(),
                position,
                size
        );
    }

    /**
     * Проверяет столкновение между двумя прямоугольниками.
     */
    private static boolean checkCollision(
            Position pos1, Size size1,
            Position pos2, Size size2
    ) {
        if (pos1 == null || pos2 == null) return false;
        boolean overlapX = pos1.getX() + size1.getWidth() / 2 >= pos2.getX() - size2.getWidth() / 2 &&
                pos1.getX() - size1.getWidth() / 2 <= pos2.getX() + size2.getWidth() / 2;

        boolean overlapY = pos1.getY() + size1.getHeight() / 2 >= pos2.getY() - size2.getHeight() / 2 &&
                pos1.getY() - size1.getHeight() / 2 <= pos2.getY() + size2.getHeight() / 2;

        return overlapX && overlapY;
    }
}