package model.unit.enemy;

import model.field.Cell;
import model.field.GameField;
import model.geometry.Position;
import model.logic.Collidable;
import model.strategy.EnemyStrategy.EnemyStrategy;
import model.view.sprites.SpriteLoader;

import java.awt.*;

// Такой же, как и Ballom, но быстрее
public class Dahl extends Enemy {

    private static final int DEFAULT_HEALTH = 1;

    private static final double DEFAULT_SPEED = 0.1;

    public Dahl(GameField field, Position position) {
        super(field, position, DEFAULT_HEALTH, DEFAULT_SPEED);

        setStrategy(new EnemyStrategy(this));
    }

    @Override
    public void render(Graphics g) {
        double offsetX = (Cell.getDefaultSize().getWidth() - size().getWidth()) / 2;
        double offsetY = (Cell.getDefaultSize().getHeight() - size().getHeight()) / 2;

        g.drawImage(
                SpriteLoader.dahlTile(),
                (int)(position().getX() + offsetX),
                (int)(position().getY() + offsetY),
                (int) size().getWidth(),
                (int) size().getHeight(),
                null
        );
    }
}
