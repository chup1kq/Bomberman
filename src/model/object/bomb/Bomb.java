package model.object.bomb;

import model.event.TimerEvent;
import model.event.TimerListener;
import model.field.Cell;
import model.geometry.Size;
import model.logic.Damageable;
import model.logic.Updatable;
import model.object.GameObject;
import model.timer.Timer;
import model.unit.Bomberman;
import model.unit.Unit;
import model.view.sprites.SpriteLoader;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Bomb extends GameObject implements Updatable, Damageable {

    private static final Size DEFAULT_SIZE = new Size(36, 36);

    private final int _radius;

    private final Timer _timer = new Timer(2000);

    private boolean _isTransparent = true;

    private int _damage = 1;

    private final Unit _owner;

    public Bomb(@NotNull Cell cell, int radius, @NotNull Unit owner) {
        super(cell, DEFAULT_SIZE);

        _radius = radius;
        _owner = owner;
        _timer.start();
        _timer.addListener(new TimerObserver());
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

    //--------------- Таймер --------------------------

    public Timer getTimer() {
        return _timer;
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
        Explosion.createExplosion(cell, _radius, _damage);
    }

    @Override
    public void update(double deltaTime) {
        _timer.update(deltaTime);

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

    private class TimerObserver implements TimerListener {

        @Override
        public void timeIsOver(TimerEvent event) {
            if (_timer == event.getTimer()) {
                explode();
            }
        }
    }
}
