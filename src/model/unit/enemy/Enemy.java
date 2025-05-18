package model.unit.enemy;

import model.enums.Direction;
import model.event.UnitEvent;
import model.field.GameField;
import model.geometry.Position;
import model.logic.Collidable;
import model.unit.Bomberman;
import model.unit.Unit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Enemy extends Unit {

    private int _damage = 1;

    private boolean _isDead = false;

    private Direction _currentDirection;
    private double _timeSinceLastChange = 0;
    private static final double CHANGE_DIRECTION_INTERVAL = 3000.0;
    private double _timeSinceLastStep = 0;
    private static final double STEP_DELAY = 60;

    public void setDamage(int damage) {
        _damage = damage;
    }

    public int getDamage() {
        return _damage;
    }

    public Enemy(GameField field, Position position, int health, double speed) {
        super(field, position, health, speed);
        field.spawnEnemy(this);
        _currentDirection = getRandomAvailableDirection();
    }

    public void die() {
        _isDead = true;
    }

    public boolean dead() {
        return getHealthPoint() <= 0;
    }

    @Override
    public void update(double deltaTime) {
        updateStrategy(deltaTime);
        findCollideWithExplosion();
        findCollideWithBomberman();
    }

    @Override
    public void collide(Collidable object) {
        super.collide(object);

        if (object instanceof Bomberman bomberman) {
            bomberman.takeDamage(_damage);
        }
    }


    private void updateStrategy(double deltaTime) {
        _timeSinceLastChange += deltaTime;
        _timeSinceLastStep += deltaTime;

        if (!canMove(_currentDirection) || _timeSinceLastChange >= CHANGE_DIRECTION_INTERVAL) {
            _currentDirection = getRandomAvailableDirection();
            _timeSinceLastChange = 0;
        }

        if (_timeSinceLastStep >= STEP_DELAY) {
            move(_currentDirection, deltaTime);
            _timeSinceLastStep = 0;
        }
    }

    private Direction getRandomAvailableDirection() {
        List<Direction> directions = new ArrayList<>();
        Collections.addAll(directions, Direction.values());
        Collections.shuffle(directions);

        for (Direction dir : directions) {
            if (canMove(dir)) {
                return dir;
            }
        }

        return _currentDirection;
    }

    @Override
    public void takeDamage(int damage) {
        setHealthPoint(getHealthPoint() - damage);

        if (getHealthPoint() <= 0) {
            die();
            UnitEvent event = new UnitEvent(this);
            event.setUnit(this);
            fireDied(event);
        }
    }
}
