package bomberman;

import model.field.Cell;
import model.geometry.Position;
import model.geometry.Size;
import model.object.GameObject;
import model.object.wall.BreakableWall;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class BreakableWallTest {
    private Cell cell;
    private BreakableWall breakableWall;
    private GameObject testObject;

    @BeforeEach
    void setUp() {
        cell = new Cell(new Position(0, 0));
        breakableWall = new BreakableWall(cell);
        testObject = new GameObject(null, new Size(10, 10)) {
            @Override
            public void render(Graphics g) {

            }
        };
    }

    @Test
    @DisplayName("Создание стены с дефолтным здоровьем")
    void testDefaultHealth() {
        assertEquals(1, breakableWall.getHealthPoint());
    }

    @Test
    @DisplayName("Создание стены с кастомным здоровьем")
    void testCustomHealth() {
        cell.deleteObject();
        BreakableWall customWall = new BreakableWall(cell, 3);
        assertEquals(3, customWall.getHealthPoint());
    }

    @Test
    @DisplayName("Установка нового значения здоровья")
    void testSetHealthPoint() {
        breakableWall.setHealthPoint(5);
        assertEquals(5, breakableWall.getHealthPoint());
    }

    @Test
    @DisplayName("Хранение объекта в стене")
    void testObjectStorage() {
        breakableWall.setObject(testObject);
        assertEquals(testObject, breakableWall.getObject());
    }

    @Test
    @DisplayName("Получение урона уменьшает здоровье")
    void testTakeDamage() {
        breakableWall.takeDamage(1);
        assertEquals(0, breakableWall.getHealthPoint());
    }

    @Test
    @DisplayName("Разрушение стены при достижении 0 HP")
    void testCollapseAtZeroHealth() {
        breakableWall.setObject(testObject);

        breakableWall.takeDamage(1);

        assertNotNull(cell.getObject());
        assertEquals(testObject, cell.getObject());
    }

    @Test
    @DisplayName("Стена не разрушается при HP > 0")
    void testNoCollapseAboveZeroHealth() {
        cell.deleteObject();
        BreakableWall strongWall = new BreakableWall(cell, 2);
        strongWall.setObject(testObject);

        strongWall.takeDamage(1);

        assertEquals(strongWall, cell.getObject());
        assertEquals(testObject, strongWall.getObject());
    }

    @Test
    @DisplayName("Разрушение пустой стены")
    void testCollapseEmptyWall() {
        BreakableWall emptyWall = new BreakableWall(new Cell(new Position(1, 1)));

        assertDoesNotThrow(() -> {
            emptyWall.takeDamage(1);
        });
    }

    @Test
    @DisplayName("Негативное здоровье после урона")
    void testNegativeHealth() {
        breakableWall.takeDamage(2);
        assertTrue(breakableWall.getHealthPoint() < 0);
    }
}