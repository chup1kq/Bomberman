package model.object.wall;

import model.field.Cell;
import model.logic.Damageable;
import model.object.GameObject;
import model.view.sprites.SpriteLoader;

import java.awt.*;

public class BreakableWall extends Wall implements Damageable {

    private static final int DEFAULT_HP = 1;

    private GameObject _object;

    private int _healthPoint;

    public BreakableWall(Cell cell) {
        super(cell);

        _healthPoint = DEFAULT_HP;
    }

    public BreakableWall(Cell cell, int healthPoint) {
        super(cell);

        _healthPoint = healthPoint;
    }

    public int getHealthPoint() {
        return _healthPoint;
    }

    public void setHealthPoint(int healthPoint) {
        _healthPoint = healthPoint;
    }

    public void setObject(GameObject object) {
        _object = object;
    }

    public GameObject getObject() {
        return _object;
    }

    public void collapse() {
        GameObject object = _object;
        Cell cell = getCell();

        cell.deleteObject();
        if (object != null) cell.setObject(object);
    }

    @Override
    public void takeDamage(int damage) {
        _healthPoint -= damage;

        if (_healthPoint <= 0) collapse();
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(
                SpriteLoader.breakableWallTile(),
                (int) position().getX(),
                (int) position().getY(),
                (int) size().getWidth(),
                (int) size().getHeight(),
                null
        );
    }
}
