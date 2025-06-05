package bomberman;

import model.field.Cell;
import model.field.GameField;
import model.object.bomb.Bomb;
import model.object.bomb.detonation.ProximityStrategy;
import model.object.bomb.detonation.TimerStrategy;
import model.object.bomb.explosion.CrossStrategy;
import model.unit.Bomberman;
import model.unit.enemy.Dahl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProximityStrategyTest {
    private GameField gameField;
    private ProximityStrategy proximityStrategy;

    @BeforeEach
    void setUp() {
        gameField = new GameField();
        proximityStrategy = new ProximityStrategy();
    }

    @Test
    @DisplayName("Враг появляется рядом с бомбой")
    void spawnEnemyNearBomb() {
        Bomberman bomberman = gameField.getBomberman();
        ArrayList<Cell> cells = new ArrayList<>(gameField.getCells());
        Bomb bomb = new Bomb(cells.get(18), 1, bomberman, new TimerStrategy(), new CrossStrategy());
        gameField.spawnEnemy(new Dahl(gameField, cells.get(1).position()));

        assertTrue(proximityStrategy.shouldExplode(bomb, 0));
    }

    @Test
    @DisplayName("Враг появляется не рядом с бомбой")
    void spawnEnemyNotNearBomb() {
        Bomberman bomberman = gameField.getBomberman();
        ArrayList<Cell> cells = new ArrayList<>(gameField.getCells());
        Bomb bomb = new Bomb(cells.get(18), 1, bomberman, new TimerStrategy(), new CrossStrategy());
        gameField.spawnEnemy(new Dahl(gameField, cells.get(4).position()));

        assertFalse(proximityStrategy.shouldExplode(bomb, 0));
    }
}
