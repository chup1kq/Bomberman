package model.input;

import model.enums.Direction;
import model.unit.Bomberman;

import java.util.ArrayList;
import java.util.List;

public class BombermanController {

    private Bomberman _bomberman;

    private HandleInput _handleInput = new HandleInput();

    public BombermanController(Bomberman bomberman) {
        _bomberman = bomberman;
    }

    public void processInput(long deltaTime) {
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
            _bomberman.move(Direction.fromSubdirections(directionList), deltaTime);
        }

        if (_handleInput.isSpace()) {
            _bomberman.plantBomb();
        }
    }
}
