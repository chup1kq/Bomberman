package bomberman;

import model.enums.BonusType;
import model.enums.Direction;
import model.enums.Orientation;
import model.field.Cell;
import model.geometry.Position;
import model.logic.Updatable;
import model.object.Bonus;
import model.object.GameObject;
import model.object.Portal;
import model.object.bomb.Explosion;
import model.object.bomb.explosion.CrossStrategy;
import model.object.wall.BreakableWall;
import model.object.wall.UnbreakableWall;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExplosionTest {

    private List<Cell> _cells;
    private CrossStrategy _strategy = new CrossStrategy();

    @BeforeEach
    void setUp() {
        _cells = create3x3Field();
    }

    @Test
    @DisplayName("Создание взрыва в середине поля")
    void explosionInCenter() {
        Cell cell = _cells.get(4);
        _strategy.createExplosion(cell, 1, 1);

        assertNull(_cells.get(0).getObject());
        assertInstanceOf(Explosion.class, _cells.get(1).getObject());
        assertNull(_cells.get(2).getObject());
        assertInstanceOf(Explosion.class, _cells.get(3).getObject());
        assertInstanceOf(Explosion.class, _cells.get(4).getObject());
        assertInstanceOf(Explosion.class, _cells.get(5).getObject());
        assertNull(_cells.get(6).getObject());
        assertInstanceOf(Explosion.class,_cells.get(7).getObject() );
        assertNull(_cells.get(8).getObject());

        assertEquals(Orientation.VERTICAL, ((Explosion)_cells.get(1).getObject()).getOrientation());
        assertEquals(Orientation.HORIZONTAL, ((Explosion)_cells.get(3).getObject()).getOrientation());
        assertEquals(Orientation.OMNIDIRECTIONAL, ((Explosion)_cells.get(4).getObject()).getOrientation());
        assertEquals(Orientation.HORIZONTAL, ((Explosion)_cells.get(5).getObject()).getOrientation());
        assertEquals(Orientation.VERTICAL, ((Explosion)_cells.get(7).getObject()).getOrientation());
    }

    @Test
    @DisplayName("Создание взрыва в левом верхнем углу")
    void explosionTopLeftCorner() {
        Cell cell = _cells.get(0);
        _strategy.createExplosion(cell, 1, 1);

        assertInstanceOf(Explosion.class, _cells.get(0).getObject());
        assertInstanceOf(Explosion.class, _cells.get(1).getObject());
        assertInstanceOf(Explosion.class, _cells.get(3).getObject());
        assertNull(_cells.get(2).getObject());
        assertNull(_cells.get(4).getObject());
        assertNull(_cells.get(5).getObject());
        assertNull(_cells.get(6).getObject());
        assertNull(_cells.get(7).getObject());
        assertNull(_cells.get(8).getObject());

        assertEquals(Orientation.OMNIDIRECTIONAL, ((Explosion)_cells.get(0).getObject()).getOrientation());
        assertEquals(Orientation.HORIZONTAL, ((Explosion)_cells.get(1).getObject()).getOrientation());
        assertEquals(Orientation.VERTICAL, ((Explosion)_cells.get(3).getObject()).getOrientation());
    }

    @Test
    @DisplayName("Создание взрыва в правом нижнем углу")
    void explosionBottomRightCorner() {
        Cell cell = _cells.get(8);
        _strategy.createExplosion(cell, 1, 1);

        assertInstanceOf(Explosion.class, _cells.get(8).getObject());
        assertInstanceOf(Explosion.class, _cells.get(5).getObject());
        assertInstanceOf(Explosion.class, _cells.get(7).getObject());
        assertNull(_cells.get(0).getObject());
        assertNull(_cells.get(1).getObject());
        assertNull(_cells.get(2).getObject());
        assertNull(_cells.get(3).getObject());
        assertNull(_cells.get(4).getObject());
        assertNull(_cells.get(6).getObject());

        assertEquals(Orientation.OMNIDIRECTIONAL, ((Explosion)_cells.get(8).getObject()).getOrientation());
        assertEquals(Orientation.HORIZONTAL, ((Explosion)_cells.get(7).getObject()).getOrientation());
        assertEquals(Orientation.VERTICAL, ((Explosion)_cells.get(5).getObject()).getOrientation());
    }

    @Test
    @DisplayName("Создание взрыва в левом верхнем углу")
    void explosionTopLeftCornerWithRadius2() {
        Cell cell = _cells.get(0);
        _strategy.createExplosion(cell, 2, 1);

        assertInstanceOf(Explosion.class, _cells.get(0).getObject());
        assertInstanceOf(Explosion.class, _cells.get(1).getObject());
        assertInstanceOf(Explosion.class, _cells.get(2).getObject());
        assertInstanceOf(Explosion.class, _cells.get(3).getObject());
        assertNull(_cells.get(4).getObject());
        assertNull(_cells.get(5).getObject());
        assertInstanceOf(Explosion.class, _cells.get(6).getObject());
        assertNull(_cells.get(7).getObject());
        assertNull(_cells.get(8).getObject());

        assertEquals(Orientation.OMNIDIRECTIONAL, ((Explosion)_cells.get(0).getObject()).getOrientation());
        assertEquals(Orientation.HORIZONTAL, ((Explosion)_cells.get(1).getObject()).getOrientation());
        assertEquals(Orientation.HORIZONTAL, ((Explosion)_cells.get(2).getObject()).getOrientation());
        assertEquals(Orientation.VERTICAL, ((Explosion)_cells.get(3).getObject()).getOrientation());
        assertEquals(Orientation.VERTICAL, ((Explosion)_cells.get(6).getObject()).getOrientation());
    }

    @Test
    @DisplayName("Создание взрыва в середине поля окруженного не разрушаемыми стенами")
    void explosionInCenterSurroundedByUnbreakableWalls() {
        Cell cell = _cells.get(4);
        new UnbreakableWall(_cells.get(1));
        new UnbreakableWall(_cells.get(3));
        new UnbreakableWall(_cells.get(5));
        new UnbreakableWall(_cells.get(7));
        _strategy.createExplosion(cell, 1, 1);

        assertNull(_cells.get(0).getObject());
        assertInstanceOf(UnbreakableWall.class, _cells.get(1).getObject());
        assertNull(_cells.get(2).getObject());
        assertInstanceOf(UnbreakableWall.class, _cells.get(3).getObject());
        assertInstanceOf(Explosion.class, _cells.get(4).getObject());
        assertInstanceOf(UnbreakableWall.class, _cells.get(5).getObject());
        assertNull(_cells.get(6).getObject());
        assertInstanceOf(UnbreakableWall.class,_cells.get(7).getObject() );
        assertNull(_cells.get(8).getObject());

        assertEquals(Orientation.OMNIDIRECTIONAL, ((Explosion)_cells.get(4).getObject()).getOrientation());
    }

    @Test
    @DisplayName("Создание взрыва в середине поля окруженного разрушаемыми стенами")
    void explosionInCenterSurroundedByBreakableWalls() {
        Cell cell = _cells.get(4);
        new BreakableWall(_cells.get(1));
        new BreakableWall(_cells.get(3));
        new BreakableWall(_cells.get(5));
        new BreakableWall(_cells.get(7));
        _strategy.createExplosion(cell, 1, 1);

        assertNull(_cells.get(0).getObject());
        assertNull(_cells.get(1).getObject());
        assertNull(_cells.get(2).getObject());
        assertNull(_cells.get(3).getObject());
        assertInstanceOf(Explosion.class, _cells.get(4).getObject());
        assertNull(_cells.get(5).getObject());
        assertNull(_cells.get(6).getObject());
        assertNull(_cells.get(7).getObject());
        assertNull(_cells.get(8).getObject());

        assertEquals(Orientation.OMNIDIRECTIONAL, ((Explosion)_cells.get(4).getObject()).getOrientation());
    }

    @Test
    @DisplayName("Создание взрыва в середине поля окруженного разрушаемыми стенами с кол-вом HP больше чем урон")
    void explosionInCenterSurroundedByBreakableWallsWithManyHP() {
        Cell cell = _cells.get(4);
        new BreakableWall(_cells.get(1), 2);
        new BreakableWall(_cells.get(3), 2);
        new BreakableWall(_cells.get(5), 2);
        new BreakableWall(_cells.get(7), 2);
        _strategy.createExplosion(cell, 1, 1);

        assertNull(_cells.get(0).getObject());
        assertInstanceOf(BreakableWall.class, _cells.get(1).getObject());
        assertNull(_cells.get(2).getObject());
        assertInstanceOf(BreakableWall.class, _cells.get(3).getObject());
        assertInstanceOf(Explosion.class, _cells.get(4).getObject());
        assertInstanceOf(BreakableWall.class, _cells.get(5).getObject());
        assertNull(_cells.get(6).getObject());
        assertInstanceOf(BreakableWall.class,_cells.get(7).getObject() );
        assertNull(_cells.get(8).getObject());

        assertEquals(Orientation.OMNIDIRECTIONAL, ((Explosion)_cells.get(4).getObject()).getOrientation());
    }

    @Test
    @DisplayName("Взрыв стены рядом с порталом и стеной, которая содержит объект")
    void collapseBreakableWallWithObject() {
        Cell cell = _cells.get(0);
        GameObject object1 = new Bonus(BonusType.SPEED_BONUS);
        GameObject object2 = new Portal(_cells.get(3));
        BreakableWall wall = new BreakableWall(_cells.get(1));
        wall.setObject(object1);
        _strategy.createExplosion(cell, 1, 1);

        assertInstanceOf(Explosion.class, _cells.get(0).getObject());
        assertInstanceOf(Bonus.class, _cells.get(1).getObject());
        assertInstanceOf(Portal.class, _cells.get(3).getObject());
        assertNull(_cells.get(2).getObject());
        assertNull(_cells.get(4).getObject());
        assertNull(_cells.get(5).getObject());
        assertNull(_cells.get(6).getObject());
        assertNull(_cells.get(7).getObject());
        assertNull(_cells.get(8).getObject());

        assertEquals(Orientation.OMNIDIRECTIONAL, ((Explosion)_cells.get(0).getObject()).getOrientation());
    }

    @Test
    @DisplayName("Обновление взрыва")
    void updateExplosion() {
        Cell cell = _cells.get(4);
        _strategy.createExplosion(cell, 1, 1);

        assertNull(_cells.get(0).getObject());
        assertInstanceOf(Explosion.class, _cells.get(1).getObject());
        assertNull(_cells.get(2).getObject());
        assertInstanceOf(Explosion.class, _cells.get(3).getObject());
        assertInstanceOf(Explosion.class, _cells.get(4).getObject());
        assertInstanceOf(Explosion.class, _cells.get(5).getObject());
        assertNull(_cells.get(6).getObject());
        assertInstanceOf(Explosion.class,_cells.get(7).getObject() );
        assertNull(_cells.get(8).getObject());

        assertEquals(Orientation.VERTICAL, ((Explosion)_cells.get(1).getObject()).getOrientation());
        assertEquals(Orientation.HORIZONTAL, ((Explosion)_cells.get(3).getObject()).getOrientation());
        assertEquals(Orientation.OMNIDIRECTIONAL, ((Explosion)_cells.get(4).getObject()).getOrientation());
        assertEquals(Orientation.HORIZONTAL, ((Explosion)_cells.get(5).getObject()).getOrientation());
        assertEquals(Orientation.VERTICAL, ((Explosion)_cells.get(7).getObject()).getOrientation());

        _cells.stream()
                .map(Cell::getObject)
                .filter(Objects::nonNull)
                .filter(obj -> obj instanceof Updatable)
                .map(obj -> (Updatable) obj)
                .forEach(updatable -> updatable.update(1000));

        _cells.forEach(cell1 -> assertNull(cell1.getObject()));
    }

    @Test
    @DisplayName("Обновление взрыва, но он не пропадает")
    void updateExplosionNotFinishedEarly() {
        Cell cell = _cells.get(4);
        _strategy.createExplosion(cell, 1, 1);

        assertNull(_cells.get(0).getObject());
        assertInstanceOf(Explosion.class, _cells.get(1).getObject());
        assertNull(_cells.get(2).getObject());
        assertInstanceOf(Explosion.class, _cells.get(3).getObject());
        assertInstanceOf(Explosion.class, _cells.get(4).getObject());
        assertInstanceOf(Explosion.class, _cells.get(5).getObject());
        assertNull(_cells.get(6).getObject());
        assertInstanceOf(Explosion.class,_cells.get(7).getObject() );
        assertNull(_cells.get(8).getObject());

        assertEquals(Orientation.VERTICAL, ((Explosion)_cells.get(1).getObject()).getOrientation());
        assertEquals(Orientation.HORIZONTAL, ((Explosion)_cells.get(3).getObject()).getOrientation());
        assertEquals(Orientation.OMNIDIRECTIONAL, ((Explosion)_cells.get(4).getObject()).getOrientation());
        assertEquals(Orientation.HORIZONTAL, ((Explosion)_cells.get(5).getObject()).getOrientation());
        assertEquals(Orientation.VERTICAL, ((Explosion)_cells.get(7).getObject()).getOrientation());

        _cells.stream()
                .map(Cell::getObject)
                .filter(Objects::nonNull)
                .filter(obj -> obj instanceof Updatable)
                .map(obj -> (Updatable) obj)
                .forEach(updatable -> updatable.update(1000 - 0.1));

        assertNotNull(_cells.get(1).getObject());
        assertNotNull(_cells.get(3).getObject());
        assertNotNull(_cells.get(4).getObject());
        assertNotNull(_cells.get(5).getObject());
        assertNotNull(_cells.get(7).getObject());
    }

    private static List<Cell> create3x3Field() {
        int size = 3;
        Cell[][] grid = new Cell[size][size];
        List<Cell> allCells = new ArrayList<>();

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                Cell cell = new Cell(new Position(x, y));
                grid[y][x] = cell;
                allCells.add(cell);
            }
        }

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                Cell cell = grid[y][x];

                if (y > 0) cell.setNeighbor(grid[y - 1][x], Direction.NORTH);
                if (y < size - 1) cell.setNeighbor(grid[y + 1][x], Direction.SOUTH);
                if (x > 0) cell.setNeighbor(grid[y][x - 1], Direction.WEST);
                if (x < size - 1) cell.setNeighbor(grid[y][x + 1], Direction.EAST);

                if (y > 0 && x > 0) cell.setNeighbor(grid[y - 1][x - 1], Direction.NORTH_WEST);
                if (y > 0 && x < size - 1) cell.setNeighbor(grid[y - 1][x + 1], Direction.NORTH_EAST);
                if (y < size - 1 && x > 0) cell.setNeighbor(grid[y + 1][x - 1], Direction.SOUTH_WEST);
                if (y < size - 1 && x < size - 1) cell.setNeighbor(grid[y + 1][x + 1], Direction.SOUTH_EAST);
            }
        }

        return allCells;
    }
}
