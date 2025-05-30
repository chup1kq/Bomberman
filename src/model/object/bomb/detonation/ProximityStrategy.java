package model.object.bomb.detonation;

import model.field.Cell;
import model.field.GameField;
import model.object.bomb.Bomb;
import model.unit.enemy.Enemy;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ProximityStrategy implements DetonationStrategy {

    @Override
    public boolean shouldExplode(@NotNull Bomb bomb, double deltaTime) {
        Cell bombCell = bomb.getCell();
        GameField field = bomb.getOwner().getField();

        if (bombCell == null) return false;


        if (hasEnemyInCell(bombCell, field)) {
            return true;
        }

        for (Cell neighbor : bombCell.getNeighbors().values()) {
            if (neighbor != null && hasEnemyInCell(neighbor, field)) {
                return true;
            }
        }

        return false;
    }

    private boolean hasEnemyInCell(@NotNull Cell cell, GameField field) {
        Optional<Enemy> enemy = field.findColliding(cell, Enemy.class);
        return enemy.isPresent() && !enemy.get().dead();
    }
}