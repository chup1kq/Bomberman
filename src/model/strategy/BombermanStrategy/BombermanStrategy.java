package model.strategy.BombermanStrategy;

import model.enums.Direction;
import model.strategy.BombStrategy;
import model.strategy.MoveStrategy;
import model.strategy.UnitStrategy;
import model.unit.Bomberman;

import java.util.ArrayList;
import java.util.List;

public class BombermanStrategy extends UnitStrategy implements MoveStrategy, BombStrategy {

    private HandleInput _handleInput = HandleInput.getInstance();

    @Override
    public void update(double deltaTime) {
        move(deltaTime);
        plantBomb();
    }

    @Override
    public void move(double deltaTime) {
        int dx = 0, dy = 0;

        if (_handleInput.isUp()) dy++;
        if (_handleInput.isDown()) dy--;
        if (_handleInput.isLeft()) dx--;
        if (_handleInput.isRight()) dx++;

        if (dx != 0 || dy != 0) {
            List<Direction> directionList = new ArrayList<>();
            if (dx > 0) directionList.add(Direction.EAST);
            if (dx < 0) directionList.add(Direction.WEST);
            if (dy > 0) directionList.add(Direction.NORTH);
            if (dy < 0) directionList.add(Direction.SOUTH);
            getUnit().move(Direction.fromSubdirections(directionList), deltaTime);
        }
    }

    @Override
    public void plantBomb() {
        if (_handleInput.isSpace()) {
            ((Bomberman)getUnit()).plantBomb();
        }
    }
}
