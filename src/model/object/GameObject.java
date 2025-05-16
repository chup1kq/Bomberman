package model.object;

import model.field.Cell;
import model.logic.Collidable;
import model.geometry.Position;
import model.geometry.Size;
import model.logic.Renderable;
import org.jetbrains.annotations.NotNull;

public abstract class GameObject implements Collidable, Renderable {

    private Cell _cell;

    private final Size _size;

    protected GameObject(Cell cell, @NotNull Size size) {
        _size = size;

        if (cell != null) {
            cell.setObject(this);
            _cell = cell;
        }
    }

    public void setCell(Cell cell) {
        _cell = cell;
    }

    public Cell getCell() {
        return _cell;
    }

    @Override
    public Position position() {
        return _cell == null ? null : _cell.position();
    }

    @Override
    public Size size() {
        return _size;
    }
}
