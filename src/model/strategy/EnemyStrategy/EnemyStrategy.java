package model.strategy.EnemyStrategy;

import model.enums.Direction;
import model.strategy.MoveStrategy;
import model.strategy.UnitStrategy;
import model.unit.enemy.Enemy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EnemyStrategy extends UnitStrategy implements MoveStrategy {

    private final Enemy _enemy;
    private Direction _currentDirection;
    private double _timeSinceLastChange = 0;
    private static final double CHANGE_DIRECTION_INTERVAL = 3000.0;
    private double _timeSinceLastStep = 0;
    private static final double STEP_DELAY = 60;

    public EnemyStrategy(Enemy enemy) {
        _enemy = enemy;
        _currentDirection = getRandomAvailableDirection();
    }

    @Override
    public void update(double deltaTime) {
        _timeSinceLastChange += deltaTime;
        _timeSinceLastStep += deltaTime;

        if (!_enemy.canMove(_currentDirection) || _timeSinceLastChange >= CHANGE_DIRECTION_INTERVAL) {
            _currentDirection = getRandomAvailableDirection();
            _timeSinceLastChange = 0;
        }

        if (_timeSinceLastStep >= STEP_DELAY) {
            _enemy.move(_currentDirection, deltaTime);
            _timeSinceLastStep = 0;
        }
    }

    @Override
    public void move(double deltaTime) {
        _enemy.move(_currentDirection, deltaTime);
    }

    private Direction getRandomAvailableDirection() {
        List<Direction> directions = new ArrayList<>();
        Collections.addAll(directions, Direction.values());
        Collections.shuffle(directions);

        for (Direction dir : directions) {
            if (_enemy.canMove(dir)) {
                return dir;
            }
        }

        return _currentDirection;
    }
}
