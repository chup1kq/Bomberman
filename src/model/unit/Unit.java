package model.unit;

import model.enums.Direction;
import model.event.UnitEvent;
import model.event.UnitListener;
import model.field.Cell;
import model.field.GameField;
import model.logic.Collidable;
import model.geometry.Position;
import model.geometry.Size;
import model.logic.Damageable;
import model.logic.Renderable;
import model.logic.Updatable;
import model.object.Bonus;
import model.object.GameObject;
import model.object.Portal;
import model.object.bomb.Bomb;
import model.object.bomb.Explosion;
import model.object.wall.Wall;
import model.unit.enemy.Enemy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Unit implements Updatable, Collidable, Damageable, Renderable {

    private static final Size DEFAULT_SIZE = new Size(30, 30);

    private double _speed;

    private Position _position;

    private final GameField _field;

    private final Size _size = DEFAULT_SIZE;

    private int _healthPoint;

    protected Unit(GameField field, Position position, int healthPoint, double speed) {
        _field = field;
        _position = position;
        _healthPoint = healthPoint;
        _speed = speed;
    }

    //------------------------------- Speed ----------------------------

    public double getSpeed() {
        return _speed;
    }

    public void setSpeed(double speed) {
        _speed = speed;
    }

    //------------------------------- Position ----------------------------

    @Override
    public Position position() {
        return _position;
    }

    public void setPosition(Position position) {
        _position = position;
    }

    //------------------------------- HP ----------------------------

    public int getHealthPoint() {
        return _healthPoint;
    }

    public void setHealthPoint(int healthPoint) {
        _healthPoint = healthPoint;
    }

    //------------------------------- Size ----------------------------

    @Override
    public Size size() {
        return _size;
    }

    //------------------------------- Field ----------------------------

    public GameField getField() {
        return _field;
    }

    public void move(Direction direction, double deltaTime) {
        Position targetPosition = calculateNewPosition(direction, deltaTime);

        if (canMove(targetPosition)) {
            _position = targetPosition;
            return;
        }

        for (Direction sub : direction.getSubdirection()) {
            Position subPosition = calculateNewPosition(sub, deltaTime);
            if (canMove(subPosition)) {
                _position = subPosition;
                return;
            }
        }
    }


    public void collide(Collidable object) {
        if (object instanceof Explosion explosion) {
            takeDamage(explosion.getDamage());
        }
    }

    protected void findCollideWithExplosion() {
        _field.findColliding(this, Explosion.class)
                .ifPresent(this::collide);
    }

    protected void findCollideWithPortal() {
        getField().findColliding(this, Portal.class)
                .ifPresent(this::collide);
    }

    protected void findCollideWithBonus() {
        getField().findColliding(this, Bonus.class)
                .ifPresent(this::collide);
    }

    protected void findCollideWithEnemy() {
        getField().findColliding(this, Enemy.class)
                .ifPresent(this::collide);
    }

    protected void findCollideWithBomberman() {
        getField().findColliding(this, Bomberman.class)
                .ifPresent(this::collide);
    }

    private Position calculateNewPosition(Direction direction, double deltaTime) {
        double newX = _position.getX();
        double newY = _position.getY();
        double distance = _speed * deltaTime;

        for (Direction subDir : direction.getSubdirection()) {
            switch (subDir) {
                case NORTH -> newY -= distance;
                case SOUTH -> newY += distance;
                case WEST -> newX -= distance;
                case EAST -> newX += distance;
            }
        }

        return new Position(newX, newY);
    }

    public boolean canMove(Direction dir) {
        Position newPosition = calculateNewPosition(dir, 1.0);
        return canMove(newPosition);
    }

    private boolean canMove(Position position) {
        Cell currentCell = _field.getCellAt(position);
        Map<Direction, Cell> neighbors = currentCell.getNeighbors();

        if (checkCollisionWithCell(currentCell, position)) {
            return false;
        }

        for (Cell neighborCell : neighbors.values()) {
            if (checkCollisionWithCell(neighborCell, position)) {
                return false;
            }
        }

        return true;
    }

    private boolean checkCollisionWithCell(Cell cell, Position position) {
        GameObject object = cell.getObject();
        if (object == null) return false;

        if (object.intersects(position, _size)) {
            return switch (object) {
                case Wall _ -> true;
                case Portal portal -> !portal.isOpened();
                case Bomb bomb -> !bomb.isTransparent();
                default -> false;
            };
        }

        return false;
    }

    @Override
    public abstract void takeDamage(int damage);

    private final List<UnitListener> _listeners = new ArrayList<>();

    public void addListener(UnitListener listener) {
        _listeners.add(listener);
    }

    public void removeListener(UnitListener listener) {
        _listeners.remove(listener);
    }

    protected void fireDied(UnitEvent event) {
        _listeners.forEach(listener -> listener.died(event));
    }

    protected void fireFoundExit(UnitEvent event) {
        _listeners.forEach(listener -> listener.foundExit(event));
    }
}
