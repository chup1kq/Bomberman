package model.field;

import model.logic.Collidable;
import model.geometry.Size;
import model.logic.Renderable;
import model.object.GameObject;
import model.enums.Direction;
import model.geometry.Position;
import model.view.sprites.SpriteLoader;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.EnumMap;
import java.util.Map;

public class Cell implements Collidable, Renderable {

    private static final Size DEFAULT_SIZE = new Size(40, 40);

    public static Size getDefaultSize() {
        return DEFAULT_SIZE;
    }

    private Position _position;

    private final Size _size = DEFAULT_SIZE;

    private GameObject _object;

    private final Map<Direction, Cell> _neighborCells = new EnumMap<>(Direction.class);

    public Cell(Position position) {
        _position = position;
    }

    //--------------------------------- Position ------------------------------------

    @Override
    public Position position() {
        return _position;
    }

    public void setPosition(Position position) {
        _position = position;
    }

    //---------------------------------- Neighbors -----------------------------------

    public Map<Direction, Cell> getNeighbors() {
        return _neighborCells;
    }

    public Cell getNeighbor(Direction direction) {
        return _neighborCells.get(direction);
    }

    public void setNeighbor(@NotNull Cell cell, @NotNull Direction direction) {
        if(_neighborCells.containsKey(direction) && _neighborCells.containsValue(cell)) return;
        if(_neighborCells.containsKey(direction)) throw new IllegalArgumentException();
        _neighborCells.put(direction, cell);
        if(cell.getNeighbor(direction.getOpposite()) == null) {
            cell.setNeighbor(this, direction.getOpposite());
        }
    }

    //-------------------------------- Object -------------------------------------

    public GameObject getObject() {
        return _object;
    }

    public void setObject(GameObject object) {
        if (object.getCell() != null) {
            throw new RuntimeException("Один объект не может одновременно находится в двух клетках");
        }
        if (_object != null) {
            throw new RuntimeException("Клетка занята");
        }

        _object = object;
        _object.setCell(this);
    }

    public void deleteObject() {
        if (_object == null) return;

        _object.setCell(null);
        _object = null;
    }

    public boolean isEmpty() {
        return _object == null;
    }

    //-------------------------------- Size -------------------------------------

    @Override
    public Size size() {
        return _size;
    }

    //---------------------------------------------------------------------

    @Override
    public void render(Graphics g) {
        g.drawImage(
                SpriteLoader.grassTile(),
                (int) _position.getX(),
                (int) _position.getY(),
                (int) size().getWidth(),
                (int) size().getHeight(),
                null
        );
    }
}
