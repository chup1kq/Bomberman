package model.unit;

import model.enums.Direction;
import model.event.TimerEvent;
import model.event.TimerListener;
import model.event.UnitEvent;
import model.field.Cell;
import model.input.HandleInput;
import model.logic.Collidable;
import model.object.Bonus;
import model.object.GameObject;
import model.object.Portal;
import model.object.bomb.Bomb;
import model.enums.BonusType;
import model.field.GameField;
import model.geometry.Position;
import model.object.bomb.BombSettings;
import model.object.bomb.detonation.DetonationStrategy;
import model.object.bomb.detonation.ProximityStrategy;
import model.object.bomb.detonation.TimerStrategy;
import model.object.bomb.explosion.CrossStrategy;
import model.object.bomb.explosion.ExplosionStrategy;
import model.object.bomb.explosion.WaveStrategy;
import model.timer.Timer;
import model.unit.enemy.Enemy;
import model.view.sprites.SpriteLoader;

import java.awt.*;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class Bomberman extends Unit {

    private static final int DEFAULT_HP = 3;

    private final static double START_SPEED = 0.1;

    private int _supplyOfBombs = 1;

    private int _bombRadius = 1;

    private final HandleInput _input = HandleInput.getInstance();

    private boolean _isInvulnerable = false;

    private Timer _invulnerableTimer;

    public Bomberman(GameField field, Position position) {
        super(field, position, DEFAULT_HP, START_SPEED);
    }

    private BombSettings _bombSettings = new BombSettings();

    public KeyListener getInput() {
        return _input;
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
            case AMMUNITION_BONUS -> _supplyOfBombs++;
            case RADIUS_BONUS -> _bombRadius++;
            case SPEED_BONUS -> setSpeed(getSpeed() + 0.02);
            default -> applyBombSettingsBonus(bonus);
        }
    }

    private void applyBombSettingsBonus(BonusType bonus) {
        BombSettings settings = bonus.getBombSettings();
        if (settings != null) {
            _bombSettings = settings;
        }
    }

    private void plantBomb() {
        Cell cell = getField().getCellAt(position());
        if (cell.isEmpty() && getField().countBombs(this) < _supplyOfBombs) {
            new Bomb(cell, _bombRadius, this, _bombSettings);
        }
    }


    private void handleMovement(double deltaTime) {
        int dx = 0, dy = 0;

        if (_input.isUp()) dy++;
        if (_input.isDown()) dy--;
        if (_input.isLeft()) dx--;
        if (_input.isRight()) dx++;

        if (dx != 0 || dy != 0) {
            List<Direction> directions = new ArrayList<>();
            if (dx > 0) directions.add(Direction.EAST);
            if (dx < 0) directions.add(Direction.WEST);
            if (dy > 0) directions.add(Direction.NORTH);
            if (dy < 0) directions.add(Direction.SOUTH);

            Direction direction = Direction.fromSubdirections(directions);
            move(direction, deltaTime);
        }
    }

    private void handleBombPlanting() {
        if (_input.isSpace()) {
            plantBomb();
        }
    }


    @Override
    public void update(double deltaTime) {
        handleMovement(deltaTime);
        handleBombPlanting();
        if (_invulnerableTimer != null) _invulnerableTimer.update(deltaTime);
        findCollisions();
    }

    private void findCollisions() {
        findCollideWithExplosion();
        findCollideWithEnemy();
        findCollideWithBonus();
        findCollideWithPortal();
    }

    @Override
    public void collide(Collidable object) {
        super.collide(object);

        if (object instanceof Enemy enemy) {
            takeDamage(enemy.getDamage());
        } else if (object instanceof Bonus bonus) {
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

    @Override
    public void takeDamage(int damage) {
        if (!_isInvulnerable) {
            setHealthPoint(getHealthPoint() - 1);

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
                (int) (position().getX() + offsetX),
                (int) (position().getY() + offsetY),
                (int) size().getWidth(),
                (int) size().getHeight(),
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
