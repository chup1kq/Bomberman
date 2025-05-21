package model.object.bomb;

import model.field.Cell;
import model.geometry.Size;
import model.logic.Damageable;
import model.logic.Updatable;
import model.object.GameObject;
import model.object.bomb.detonation.DetonationStrategy;
import model.object.bomb.explosion.ExplosionStrategy;
import model.unit.Unit;
import model.view.sprites.SpriteLoader;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Bomb extends GameObject implements Updatable, Damageable {

    private static final Size DEFAULT_SIZE = new Size(36, 36);

    private final int _radius;

    private boolean _isTransparent = true;

    private int _damage = 1;

    private final Unit _owner;

    private final DetonationStrategy _detonationStrategy;

    private final ExplosionStrategy _explosionStrategy;

    public Bomb(@NotNull Cell cell, int radius, @NotNull Unit owner, DetonationStrategy detonationStrategy, ExplosionStrategy explosionStrategy) {
        super(cell, DEFAULT_SIZE);

        _radius = radius;
        _owner = owner;
        _detonationStrategy = detonationStrategy;
        _explosionStrategy = explosionStrategy;
    }

    //--------------- Радиус --------------------------

    public int getRadius() {
        return _radius;
    }

    //--------------- Владелец --------------------------

    public Unit getOwner() {
        return _owner;
    }

    //--------------- Урон --------------------------

    public void setDamage(int damage) {
        _damage = damage;
    }

    public int getDamage() {
        return _damage;
    }

    //--------------- Можно пройти через? --------------------------

    public void setTransparent(boolean isTransparent) {
        _isTransparent = isTransparent;
    }

    public boolean isTransparent() {
        return _isTransparent;
    }


    public void explode() {
        Cell cell = getCell();
        cell.deleteObject();
        _explosionStrategy.createExplosion(cell, _radius, _damage);
    }

    @Override
    public void update(double deltaTime) {
        if (_detonationStrategy.shouldExplode(this, deltaTime)) {
            explode();
        }

        if (_isTransparent && _owner.getField().findColliding(this, _owner.getClass()).isEmpty()) {
            _isTransparent = false;
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(
                SpriteLoader.bombTile(),
                (int) position().getX(),
                (int) position().getY(),
                (int) size().getWidth(),
                (int) size().getHeight(),
                null
        );
    }

    @Override
    public void takeDamage(int damage) {
        explode();
    }
}
