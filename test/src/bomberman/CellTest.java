package bomberman;

import model.field.Cell;
import model.geometry.Position;
import model.geometry.Size;
import model.object.GameObject;
import model.enums.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {

    private Cell cell;
    private Position position;
    private final Size defaultSize = new Size(40, 40);
    private GameObject testObject;

    @BeforeEach
    void setUp() {
        position = new Position(100, 100);
        cell = new Cell(position);
        testObject = new GameObject(null, new Size(10, 10)) {
            @Override
            public void render(Graphics g) {}
        };
    }

    // ------------------------- Position Tests -------------------------
    @Test
    @DisplayName("Позиция клетки корректно устанавливается и возвращается")
    void testPosition() {
        assertEquals(position, cell.position());

        Position newPosition = new Position(200, 200);
        cell.setPosition(newPosition);
        assertEquals(newPosition, cell.position());
    }

    // ------------------------- Size Tests -------------------------
    @Test
    @DisplayName("Размер клетки по умолчанию равен 40x40")
    void testDefaultSize() {
        assertEquals(defaultSize, cell.size());
        assertEquals(defaultSize, Cell.getDefaultSize());
    }

    // ------------------------- Neighbor Tests -------------------------
    @Test
    @DisplayName("Установка и получение соседней клетки")
    void testSetAndGetNeighbor() {
        Cell neighbor = new Cell(new Position(140, 100));

        cell.setNeighbor(neighbor, Direction.EAST);
        assertEquals(neighbor, cell.getNeighbor(Direction.EAST));

        // Проверка двусторонней связи
        assertEquals(cell, neighbor.getNeighbor(Direction.WEST));
    }

    @Test
    @DisplayName("Попытка установить клетку повторно в том же направлении вызывает исключение")
    void testSetNeighborTwiceThrowsException() {
        Cell neighbor1 = new Cell(new Position(140, 100));
        cell.setNeighbor(neighbor1, Direction.EAST);
        assertThrows(IllegalArgumentException.class, () ->
                cell.setNeighbor(new Cell(new Position(140, 100)), Direction.EAST)
        );
    }

    @Test
    @DisplayName("Получение всех соседей возвращает корректный Map")
    void testGetNeighbors() {
        Cell eastNeighbor = new Cell(new Position(140, 100));
        Cell southNeighbor = new Cell(new Position(100, 140));

        cell.setNeighbor(eastNeighbor, Direction.EAST);
        cell.setNeighbor(southNeighbor, Direction.SOUTH);

        Map<Direction, Cell> neighbors = cell.getNeighbors();
        assertEquals(2, neighbors.size());
        assertEquals(eastNeighbor, neighbors.get(Direction.EAST));
        assertEquals(southNeighbor, neighbors.get(Direction.SOUTH));
    }

    // ------------------------- Object Placement Tests -------------------------
    @Test
    @DisplayName("Размещение объекта в клетке")
    void testPlaceObject() {
        cell.setObject(testObject);
        assertEquals(testObject, cell.getObject());
        assertEquals(cell, testObject.getCell());
    }

    @Test
    @DisplayName("Удаление объекта из клетки")
    void testDeleteObject() {
        cell.setObject(testObject);
        cell.deleteObject();
        assertNull(cell.getObject());
        assertNull(testObject.getCell());
    }

    @Test
    @DisplayName("Попытка удалить объект из пустой клетки не вызывает ошибок")
    void testDeleteObjectFromEmptyCell() {
        assertDoesNotThrow(() -> cell.deleteObject());
    }

    @Test
    @DisplayName("Попытка разместить объект, который уже находится в другой клетке, вызывает исключение")
    void testSetObjectAlreadyInAnotherCellThrowsException() {
        Cell cell2 = new Cell(new Position(200, 200));
        cell.setObject(testObject);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                cell2.setObject(testObject)
        );
        assertEquals("Один объект не может одновременно находится в двух клетках", exception.getMessage());
    }

    @Test
    @DisplayName("Попытка разместить объект в занятой клетке вызывает исключение")
    void testSetObjectInOccupiedCellThrowsException() {
        cell.setObject(testObject);
        GameObject anotherObject = new GameObject(null, new Size(10, 10)) {
            @Override
            public void render(Graphics g) {}
        };

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                cell.setObject(anotherObject)
        );
        assertEquals("Клетка занята", exception.getMessage());
    }

    @Test
    @DisplayName("Успешное размещение объекта в пустой клетке")
    void testSetObjectInEmptyCellSuccess() {
        assertDoesNotThrow(() -> cell.setObject(testObject));
        assertEquals(testObject, cell.getObject());
        assertEquals(cell, testObject.getCell());
    }

    @Test
    @DisplayName("Попытка разместить null в клетке вызывает NPE")
    void testSetNullObjectThrowsNPE() {
        assertThrows(NullPointerException.class, () -> cell.setObject(null));
    }

    // ------------------------- Collision Tests -------------------------
    @Test
    @DisplayName("Клетка корректно сообщает о коллизии с другой позицией")
    void testIntersectsWithPosition() {
        assertTrue(cell.intersects(new Position(100, 100)));  // inside
        assertTrue(cell.intersects(new Position(120, 100)));  // edge
        assertFalse(cell.intersects(new Position(121, 100))); // outside
    }
}