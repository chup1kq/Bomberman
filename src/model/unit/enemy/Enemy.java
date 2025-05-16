package model.unit.enemy;

import model.event.UnitEvent;
import model.field.GameField;
import model.geometry.Position;
import model.logic.Collidable;
import model.unit.Bomberman;
import model.unit.Unit;

public abstract class Enemy extends Unit {

    private int _damage = 1;

    private boolean _isDead = false;

    public void setDamage(int damage) {
        _damage = damage;
    }

    public int getDamage() {
        return _damage;
    }

    public Enemy(GameField field, Position position, int health, double speed) {
        super(field, position, health, speed);
        field.spawnEnemy(this);
    }

    public void die() {
        _isDead = true;
    }

    public boolean dead() {
        return _isDead;
    }

    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);
        findCollideWithBomberman();
    }

    @Override
    public void collide(Collidable object) {
        super.collide(object);

        if (object instanceof Bomberman bomberman) {
            bomberman.takeDamage(_damage);
        }
    }

    private void findCollideWithBomberman() {
        getField().findColliding(this, Bomberman.class)
                .ifPresent(this::collide);
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
