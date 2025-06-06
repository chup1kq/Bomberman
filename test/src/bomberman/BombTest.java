package bomberman;

import model.field.Cell;
import model.geometry.Position;
import model.object.GameObject;
import model.object.bomb.Bomb;
import model.object.bomb.BombSettings;
import model.object.bomb.Explosion;
import model.object.bomb.detonation.DetonationStrategy;
import model.object.bomb.detonation.TimerStrategy;
import model.object.bomb.explosion.CrossStrategy;
import model.object.bomb.explosion.ExplosionStrategy;
import model.timer.Timer;
import model.unit.Unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class BombTest {
    private Bomb bomb;
    private Cell cell;
    private Unit unit;
    private BombSettings bombSettings = new BombSettings(TimerStrategy.class, CrossStrategy.class);
    private final int TEST_RADIUS = 2;
    private final Position TEST_POSITION = new Position(100, 100);



    @BeforeEach
    void setUp() {
        unit = new Unit(null, new Position(20, 20), 0, 0) {
            @Override
            public void update(double deltaTime) {}

            @Override
            public void render(Graphics g) {}

            @Override
            public void takeDamage(int damage) {
                setHealthPoint(getHealthPoint() - damage);
            }
        };
        cell = new Cell(TEST_POSITION);
        bomb = new Bomb(cell, TEST_RADIUS, unit, bombSettings) {
            @Override
            public void update(double deltaTime) {
                if (getDetonationStrategy().shouldExplode(this, deltaTime)) {
                    explode();
                }
            }
        };
    }

    @Test
    @DisplayName("Установка и получение прозрачности")
    void testTransparency() {
        bomb.setTransparent(false);
        assertFalse(bomb.isTransparent());

        bomb.setTransparent(true);
        assertTrue(bomb.isTransparent());
    }

    @Test
    @DisplayName("Установка и получение урона")
    void testDamage() {
        bomb.setDamage(3);
        assertEquals(3, bomb.getDamage());

        bomb.setDamage(0);
        assertEquals(0, bomb.getDamage());
    }

    @Test
    @DisplayName("Обновление таймера бомбы")
    void testUpdate() {
        assertNotNull(cell.getObject());
        assertInstanceOf(Bomb.class, cell.getObject());

        bomb.update(3000);

        assertNotNull(cell.getObject());
    }

    @Test
    @DisplayName("Ручной взрыв бомбы")
    void testExplode() {
        assertNotNull(cell.getObject());
        bomb.explode();
        assertNotNull(cell.getObject());
    }

    @Test
    @DisplayName("Бомба не прозрачна после установки непрозрачности")
    void testNonTransparentBomb() {
        bomb.setTransparent(false);
        assertFalse(bomb.isTransparent());
    }

    @Test
    @DisplayName("Бомба остается в клетке после создания")
    void testBombInCellAfterCreation() {
        assertEquals(bomb, cell.getObject());
    }

    @Test
    @DisplayName("Взрыв создает Explosion в той же клетке")
    void testExplosionCreation() {
        bomb.explode();
        GameObject cellObject = cell.getObject();
        assertNotNull(cellObject);
        assertInstanceOf(Explosion.class, cellObject);
    }

    @Test
    @DisplayName("Обновление бомбы с недостаточным временем не вызывает взрыв")
    void testUpdateWithoutExplosion() {
        bomb.update(1000);
        assertNotNull(cell.getObject());
        assertInstanceOf(Bomb.class, cell.getObject());
    }
}