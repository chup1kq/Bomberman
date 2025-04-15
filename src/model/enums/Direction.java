package model.enums;

import java.util.List;

public enum Direction {
    NORTH, NORTH_EAST,
    EAST, SOUTH_EAST,
    SOUTH, SOUTH_WEST,
    WEST, NORTH_WEST;

    private Direction opposite;

    private List<Direction> subdirection;

    static {
        NORTH.opposite = SOUTH;
        SOUTH.opposite = NORTH;
        WEST.opposite = EAST;
        EAST.opposite = WEST;
        NORTH_EAST.opposite = SOUTH_WEST;
        SOUTH_WEST.opposite = NORTH_EAST;
        SOUTH_EAST.opposite = NORTH_WEST;
        NORTH_WEST.opposite = SOUTH_EAST;
    }

    public Direction getOpposite() {
        return opposite;
    }

    static {
        NORTH.subdirection = List.of(NORTH);
        SOUTH.subdirection = List.of(SOUTH);
        WEST.subdirection = List.of(WEST);
        EAST.subdirection = List.of(EAST);
        NORTH_EAST.subdirection = List.of(NORTH, EAST);
        SOUTH_WEST.subdirection = List.of(SOUTH, WEST);
        SOUTH_EAST.subdirection = List.of(SOUTH, EAST);
        NORTH_WEST.subdirection = List.of(NORTH, WEST);
    }

    public List<Direction> getSubdirection() {
        return subdirection;
    }

    public static Direction fromSubdirections(List<Direction> directions) {
        if (directions.size() == 1) {
            return directions.get(0);
        }
        if (directions.size() != 2) {
            throw new IllegalArgumentException("Invalid direction combination");
        }

        Direction first = directions.get(0);
        Direction second = directions.get(1);

        if ((first == NORTH && second == EAST) || (first == EAST && second == NORTH)) return NORTH_EAST;
        if ((first == SOUTH && second == EAST) || (first == EAST && second == SOUTH)) return SOUTH_EAST;
        if ((first == SOUTH && second == WEST) || (first == WEST && second == SOUTH)) return SOUTH_WEST;
        if ((first == NORTH && second == WEST) || (first == WEST && second == NORTH)) return NORTH_WEST;

        throw new IllegalArgumentException("Invalid direction combination: " + first + " and " + second);
    }
}