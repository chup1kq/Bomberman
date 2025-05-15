package bomberman;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import model.logic.Collidable;
import model.geometry.Position;
import model.geometry.Size;

class CollidableTest {

    static class TestCollidable implements Collidable {
        private final Position position;
        private final Size size;

        TestCollidable(Position position, Size size) {
            this.position = position;
            this.size = size;
        }

        @Override
        public Position position() {
            return position;
        }

        @Override
        public Size size() {
            return size != null ? size : new Size(0, 0);
        }
    }

    // ---------------------------
    // Тесты для intersects(Collidable)
    // ---------------------------
    @Test
    @DisplayName("Объекты пересекаются")
    void testObjectsWhenOverlap() {
        Collidable a = new TestCollidable(new Position(5, 5), new Size(2, 2));
        Collidable b = new TestCollidable(new Position(6, 6), new Size(2, 2));
        assertTrue(a.intersects(b));
        assertTrue(b.intersects(a));
    }

    @Test
    @DisplayName("Объекты не пересекаются")
    void testObjectsWhenNoOverlap() {
        Collidable a = new TestCollidable(new Position(0, 0), new Size(2, 2));
        Collidable b = new TestCollidable(new Position(3, 3), new Size(2, 2));
        assertFalse(a.intersects(b));
        assertFalse(b.intersects(a));
    }

    @Test
    @DisplayName("Объекты соприкасаются")
    void testObjectsWhenTouching() {
        Collidable a = new TestCollidable(new Position(0, 0), new Size(2, 2));
        Collidable b = new TestCollidable(new Position(2, 2), new Size(2, 2));
        assertTrue(a.intersects(b));
        assertTrue(b.intersects(a));
    }

    @Test
    @DisplayName("Размер объекта null (должен использовать Size(0, 0))")
    void testObjectsWhenOneSizeIsNull() {
        Collidable a = new TestCollidable(new Position(0, 0), new Size(2, 2));
        Collidable b = new TestCollidable(new Position(1, 1), null);
        assertTrue(a.intersects(b));
        assertTrue(b.intersects(a));
    }

    @Test
    @DisplayName("Один из объектов null (должно быть исключение)")
    void testObjectsWhenOtherIsNull() {
        Collidable a = new TestCollidable(new Position(0, 0), new Size(1, 1));
        assertThrows(IllegalArgumentException.class, () -> a.intersects((Position) null));
    }

    @Test
    @DisplayName("Объекты разных размеров")
    void testObjectsWithDifferentSizes() {
        Collidable large = new TestCollidable(new Position(5, 5), new Size(10, 10));
        Collidable small = new TestCollidable(new Position(8, 8), new Size(2, 2));
        assertTrue(large.intersects(small));
        assertTrue(small.intersects(large));
    }

    @Test
    @DisplayName("Один объект внутри другого")
    void testObjectsWhenOneInsideAnother() {
        Collidable outer = new TestCollidable(new Position(5, 5), new Size(10, 10));
        Collidable inner = new TestCollidable(new Position(6, 6), new Size(2, 2));
        assertTrue(outer.intersects(inner));
        assertTrue(inner.intersects(outer));
    }

    // ---------------------------
    // Тесты для intersects(Position)
    // ---------------------------
    @Test
    @DisplayName("Точка внутри объекта")
    void testPointsWhenPointInside() {
        Collidable obj = new TestCollidable(new Position(5, 5), new Size(2, 2));
        assertTrue(obj.intersects(new Position(5, 5)));
        assertTrue(obj.intersects(new Position(5.5, 5.5)));
    }

    @Test
    @DisplayName("Точка вне объекта")
    void testPointsWhenPointOutside() {
        Collidable obj = new TestCollidable(new Position(5, 5), new Size(2, 2));
        assertFalse(obj.intersects(new Position(7, 7)));
        assertFalse(obj.intersects(new Position(3, 3)));
    }

    @Test
    @DisplayName("Точка на границе объекта")
    void testPointsWhenPointOnEdge() {
        Collidable obj = new TestCollidable(new Position(5, 5), new Size(2, 2));
        assertTrue(obj.intersects(new Position(6, 4)));
        assertTrue(obj.intersects(new Position(4, 6)));
    }

    @Test
    @DisplayName("Позиция null (должно быть исключение)")
    void testPointsWhenPositionIsNull() {
        Collidable obj = new TestCollidable(new Position(0, 0), new Size(1, 1));
        assertThrows(IllegalArgumentException.class, () -> obj.intersects((Position) null));
    }

    @Test
    @DisplayName("Объект с нулевым размером")
    void testPointsWithZeroSizeObject() {
        Collidable obj = new TestCollidable(new Position(5, 5), new Size(0, 0));
        assertTrue(obj.intersects(new Position(5, 5)));
        assertFalse(obj.intersects(new Position(5.1, 5.1)));
    }

    @Test
    @DisplayName("Размер объекта null (должен вести себя как Size(0, 0))")
    void testPointsWithNullSize() {
        Collidable obj = new TestCollidable(new Position(5, 5), null);
        assertTrue(obj.intersects(new Position(5, 5)));
        assertFalse(obj.intersects(new Position(6, 6)));
    }
}