package model.unit;

import model.event.TimerEvent;
import model.event.TimerListener;
import model.event.UnitEvent;
import model.field.Cell;
import model.logic.Collidable;
import model.object.Bonus;
import model.object.GameObject;
import model.object.Portal;
import model.object.bomb.Bomb;
import model.enums.BonusType;
import model.field.GameField;
import model.geometry.Position;
import model.object.bomb.Explosion;
import model.strategy.UnitStrategy;
import model.timer.Timer;
import model.unit.enemy.Enemy;
import model.view.sprites.SpriteLoader;

import java.awt.*;
import java.sql.Time;

public class Bomberman extends Unit {

    private static final int DEFAULT_HP = 3;

    private final static double START_SPEED = 0.1;

    private int _supplyOfBombs = 1;

    private int _bombRadius = 1;

    private boolean _isInvulnerable = false;

    private Timer _invulnerableTimer;

    public Bomberman(GameField field, UnitStrategy strategy, Position position) {
        super(field, position, DEFAULT_HP, START_SPEED);

        setStrategy(strategy);
        getStrategy().setUnit(this);
    }

    //------------------------------- Bombs ----------------------------

    public int getSupplyOfBombs() {
        return _supplyOfBombs;
    }

    public void setSupplyOfBombs(int bombCount) {
        _supplyOfBombs = bombCount;
    }

    //------------------------------- Radius ----------------------------

    public int getBombRadius() {
        return _bombRadius;
    }

    public void setBombRadius(int bombRadius) {
        _bombRadius = bombRadius;
    }

    //-----------------------------------------------------------

    public void useBonus(BonusType bonus) {
        switch (bonus) {
            case BonusType.AMMUNITION_BONUS -> _supplyOfBombs++;
            case BonusType.RADIUS_BONUS -> _bombRadius++;
            case BonusType.SPEED_BONUS -> setSpeed(getSpeed() + 0.02);
        }
    }

    public void plantBomb() {
        Cell cell = getField().getCellAt(position());
        if (cell.isEmpty() && getField().countBombs(this) < _supplyOfBombs) {
            new Bomb(cell, _bombRadius, this);
        }
    }

    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);
        if (_invulnerableTimer != null) _invulnerableTimer.update(deltaTime);
        findCollideWithEnemy();
        findCollideWithBonus();
        findCollideWithPortal();
    }

    private void findCollideWithPortal() {
        getField().findColliding(this, Portal.class)
                .ifPresent(this::collide);
    }

    private void findCollideWithBonus() {
        getField().findColliding(this, Bonus.class)
                .ifPresent(this::collide);
    }

    private void findCollideWithEnemy() {
        getField().findColliding(this, Enemy.class)
                .ifPresent(this::collide);
    }

    @Override
    public void collide(Collidable object) {
        super.collide(object);

        if (object instanceof Enemy enemy) {
            takeDamage(enemy.getDamage());
        }else if (object instanceof Bonus bonus) {
            useBonus(bonus.getType());
            ((GameObject) object).getCell().deleteObject();
        } else if (object instanceof Portal portal) {
            if (portal.isOpened()) {
                UnitEvent event = new UnitEvent(this);
                event.setUnit(this);
                fireFoundExit(event);
            }
        }
    }

//    private void respawn() {
//        setPosition(new Position(40, 40));
//    }

    @Override
    public void takeDamage(int damage) {
        if (!_isInvulnerable) {
            setHealthPoint(getHealthPoint() - 1);
            //respawn();

            _isInvulnerable = true;
            _invulnerableTimer = new Timer(2000);
            TimerObserver observer = new TimerObserver();
            _invulnerableTimer.addListener(observer);
            _invulnerableTimer.start();

            UnitEvent event = new UnitEvent(this);
            event.setUnit(this);
            fireDied(event);
        }
    }

    @Override
    public void render(Graphics g) {
        double offsetX = (Cell.getDefaultSize().getWidth() - size().getWidth()) / 2;
        double offsetY = (Cell.getDefaultSize().getHeight() - size().getHeight()) / 2;

        g.drawImage(
                SpriteLoader.bombermanTile(),
                (int)(position().getX() + offsetX),
                (int)(position().getY() + offsetY),
                (int)size().getWidth(),
                (int)size().getHeight(),
                null
        );
    }

    private class TimerObserver implements TimerListener {

        @Override
        public void timeIsOver(TimerEvent event) {
            _isInvulnerable = false;
            _invulnerableTimer = null;
        }
    }
}
