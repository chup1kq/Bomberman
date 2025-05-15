package bomberman;

import model.enums.BonusType;
import model.event.UnitEvent;
import model.event.UnitListener;
import model.field.Cell;
import model.field.GameField;
import model.geometry.Position;
import model.object.Bonus;
import model.object.Portal;
import model.object.bomb.Bomb;
import model.strategy.BombermanStrategy.BombermanStrategy;
import model.unit.Bomberman;
import model.unit.enemy.Dahl;
import model.unit.enemy.Enemy;
import model.strategy.UnitStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BombermanTest {
    private Bomberman bomberman;
    private GameField field;
    private UnitStrategy strategy;
    private Position testPosition;

    @BeforeEach
    void setUp() {
        field = new GameField();
        strategy = new BombermanStrategy();
        testPosition = field.getCells().get(0).position();
        bomberman = new Bomberman(field, strategy, testPosition);
        field.setBomberman(bomberman);
    }

    @Test
    @DisplayName("Инициализация с правильными параметрами")
    void initializationTest() {
        assertEquals(3, bomberman.getHealthPoint());
        assertEquals(0.1, bomberman.getSpeed());
        assertEquals(1, bomberman.getSupplyOfBombs());
        assertEquals(1, bomberman.getBombRadius());
        assertSame(testPosition, bomberman.position());
    }

    @Test
    @DisplayName("Установка количества бомб")
    void setBombSupplyTest() {
        bomberman.setSupplyOfBombs(5);
        assertEquals(5, bomberman.getSupplyOfBombs());
    }

    @Test
    @DisplayName("Установка радиуса взрыва")
    void setBombRadiusTest() {
        bomberman.setBombRadius(3);
        assertEquals(3, bomberman.getBombRadius());
    }

    @Test
    @DisplayName("Использование бонусов")
    void useBonusTest() {
        // Бонус амуниции
        bomberman.useBonus(BonusType.AMMUNITION_BONUS);
        assertEquals(2, bomberman.getSupplyOfBombs());

        // Бонус радиуса
        bomberman.useBonus(BonusType.RADIUS_BONUS);
        assertEquals(2, bomberman.getBombRadius());

        // Бонус скорости
        bomberman.useBonus(BonusType.SPEED_BONUS);
        assertEquals(0.12, bomberman.getSpeed(), 1e-6);
    }

    @Test
    @DisplayName("Установка бомбы при наличии запаса")
    void plantBombWhenHasSupplyTest() {
        assertEquals(0, field.countBombs(bomberman));
        bomberman.plantBomb();
        assertEquals(1, field.countBombs(bomberman));
    }

    @Test
    @DisplayName("Не устанавливает бомбу при превышении лимита")
    void plantBombWhenExceedsSupplyTest() {
        Cell cell = field.getCells().get(5);
        new Bomb(cell, 1, bomberman);
        assertEquals(1, field.countBombs(bomberman));
        bomberman.plantBomb();
        assertEquals(1, field.countBombs(bomberman));
    }

    @Test
    @DisplayName("На поле есть бомба, установленная врагом")
    void enemyBombOnFieldTest() {
        Cell cell = field.getCells().get(5);
        Enemy enemy = new Dahl(field, cell.position());
        new Bomb(cell, 1, enemy);

        assertEquals(1, field.countBombs(enemy));
        assertEquals(0, field.countBombs(bomberman));
        bomberman.plantBomb();
        assertEquals(1, field.countBombs(bomberman));
    }

    @Test
    @DisplayName("Столкновение с врагом наносит урон")
    void collideWithEnemyTest() {
        Enemy enemy = new Dahl(field, bomberman.position());
        field.spawnEnemy(enemy);
        bomberman.update(0);

        assertEquals(2, bomberman.getHealthPoint());
    }

    @Test
    @DisplayName("Столкновение с бонусом применяет эффект")
    void collideWithBonusTest() {
        Bonus bonus = new Bonus(field.getCellAt(bomberman.position()), BonusType.SPEED_BONUS);
        bomberman.update(0);

        assertEquals(3, bomberman.getHealthPoint());
        assertEquals(0.12, bomberman.getSpeed(), 1e-6);
    }

    @Test
    @DisplayName("Враг наносит урон")
    void findCollideWithEnemyTest() {
        Enemy enemy = new Dahl(field, bomberman.position());
        enemy.update(0);

        assertEquals(2, bomberman.getHealthPoint());
    }

    @Test
    @DisplayName("Получение урона уменьшает здоровье")
    void takeDamageTest() {
        bomberman.takeDamage(1);
        assertEquals(2, bomberman.getHealthPoint());
    }

//    @Test
//    @DisplayName("Перерождение Bomberman'а при потере здоровья")
//    void respawnBomberman() {
//        bomberman.setPosition(field.getCells().get(1).position());
//        bomberman.takeDamage(1);
//        assertEquals(field.getCells().get(18).position(), bomberman.position());
//    }


    class BombermanObserver implements UnitListener {

        int countSignals = 0;

        @Override
        public void died(UnitEvent event) {
            countSignals++;
            assertSame(bomberman, event.getUnit());
        }

        @Override
        public void foundExit(UnitEvent event) {
            countSignals++;
            assertSame(bomberman, event.getUnit());
        }
    }


    @Test
    @DisplayName("Сигнал при потере здоровья")
    void deathEvent() {
        BombermanObserver observer = new BombermanObserver();
        bomberman.addListener(observer);
        bomberman.takeDamage(1);
        assertEquals(1, observer.countSignals);
    }

    @Test
    @DisplayName("Bomberman нашел портал")
    void findPortalEvent() {
        BombermanObserver observer = new BombermanObserver();
        Portal portal = new Portal(field.getCellAt(bomberman.position()));
        portal.open();
        bomberman.addListener(observer);
        bomberman.update(0);
        assertEquals(1, observer.countSignals);
    }
}
