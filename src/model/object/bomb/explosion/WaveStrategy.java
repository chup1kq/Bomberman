package model.object.bomb.explosion;

import model.enums.Direction;
import model.enums.Orientation;
import model.field.Cell;
import model.logic.Damageable;
import model.object.GameObject;
import model.object.bomb.Explosion;

import java.util.EnumMap;
import java.util.Map;

public class WaveStrategy implements ExplosionStrategy {

    protected static final Map<Direction, Orientation> DIRECTIONS = new EnumMap<>(Direction.class){{
        put(Direction.NORTH, Orientation.VERTICAL);
        put(Direction.SOUTH, Orientation.VERTICAL);
        put(Direction.WEST, Orientation.HORIZONTAL);
        put(Direction.EAST, Orientation.HORIZONTAL);
    }};

    @Override
    public void createExplosion(Cell cell, int radius, int damage) {
        for (Direction dir : DIRECTIONS.keySet()) {
            Cell current = cell;
            for (int i = 1; i <= radius + 1; i++) {
                current = current.getNeighbor(dir);
                if (current == null) break;

                GameObject obj = current.getObject();
                if (obj != null) {
                    if (obj instanceof Damageable) {
                        ((Damageable)obj).takeDamage(damage);
                    }
                    break;
                }

                Orientation orientation = DIRECTIONS.get(dir);
                new Explosion(current, orientation, damage);
            }
        }
    }
}
