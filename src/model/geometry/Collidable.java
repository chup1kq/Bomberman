package model.geometry;

import org.jetbrains.annotations.NotNull;
import java.util.Map;

public interface Collidable {

    Map<Position, Size> getBounds();

    default boolean intersects(@NotNull Position position) {

        Map<Position, Size> bounds = getBounds();
        for (Map.Entry<Position, Size> entry : bounds.entrySet()) {
            Position thisPos = entry.getKey();
            Size thisSize = entry.getValue() != null ? entry.getValue() : new Size(0, 0);

            if (checkCollision(thisPos, thisSize, position, new Size(0, 0))) {
                return true;
            }
        }

        return false;
    }

    default boolean intersects(@NotNull Collidable other) {

        Map<Position, Size> thisBounds = getBounds();
        Map<Position, Size> otherBounds = other.getBounds();

        for (Map.Entry<Position, Size> thisEntry : thisBounds.entrySet()) {
            Position thisPos = thisEntry.getKey();
            Size thisSize = thisEntry.getValue() != null ? thisEntry.getValue() : new Size(0, 0);

            for (Map.Entry<Position, Size> otherEntry : otherBounds.entrySet()) {
                Position otherPos = otherEntry.getKey();
                Size otherSize = otherEntry.getValue() != null ? otherEntry.getValue() : new Size(0, 0);

                if (checkCollision(thisPos, thisSize, otherPos, otherSize)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean checkCollision(Position pos1, Size size1, Position pos2, Size size2) {
        return pos1.getX() + size1.getWidth() / 2 >= pos2.getX() - size2.getWidth() / 2 &&
                pos1.getX() - size1.getWidth() / 2 <= pos2.getX() + size2.getWidth() / 2 &&
                pos1.getY() + size1.getHeight() / 2 >= pos2.getY() - size2.getHeight() / 2 &&
                pos1.getY() - size1.getHeight() / 2 <= pos2.getY() + size2.getHeight() / 2;
    }
}