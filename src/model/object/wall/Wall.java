package model.object.wall;

import model.field.Cell;
import model.geometry.Size;
import model.object.GameObject;

public abstract class Wall extends GameObject {

    private static final Size DEFAULT_SIZE = new Size(40, 40);

    public Wall(Cell cell) {
        super(cell, DEFAULT_SIZE);
    }
}
