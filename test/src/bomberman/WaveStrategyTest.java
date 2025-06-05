package bomberman;

import model.enums.Direction;
import model.enums.Orientation;
import model.field.Cell;
import model.geometry.Position;
import model.object.bomb.Explosion;
import model.object.bomb.explosion.CrossStrategy;
import model.object.bomb.explosion.WaveStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WaveStrategyTest {
    private List<Cell> _cells;
    private WaveStrategy _strategy = new WaveStrategy();

    @BeforeEach
    void setUp() {
        _cells = ExplosionTest.create3x3Field();
    }

    @Test
    @DisplayName("Создание взрыва в середине поля")
    void explosionInCenter() {
        Cell cell = _cells.get(4);
        _strategy.createExplosion(cell, 0, 1);

        assertNull(_cells.get(0).getObject());
        assertInstanceOf(Explosion.class, _cells.get(1).getObject());
        assertNull(_cells.get(2).getObject());
        assertInstanceOf(Explosion.class, _cells.get(3).getObject());
        assertNull(_cells.get(4).getObject());
        assertInstanceOf(Explosion.class, _cells.get(5).getObject());
        assertNull(_cells.get(6).getObject());
        assertInstanceOf(Explosion.class,_cells.get(7).getObject() );
        assertNull(_cells.get(8).getObject());

        assertEquals(Orientation.VERTICAL, ((Explosion)_cells.get(1).getObject()).getOrientation());
        assertEquals(Orientation.HORIZONTAL, ((Explosion)_cells.get(3).getObject()).getOrientation());
        assertEquals(Orientation.HORIZONTAL, ((Explosion)_cells.get(5).getObject()).getOrientation());
        assertEquals(Orientation.VERTICAL, ((Explosion)_cells.get(7).getObject()).getOrientation());
    }
}
