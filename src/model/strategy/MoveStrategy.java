package model.strategy;

import model.enums.Direction;
import model.geometry.Position;

public interface MoveStrategy {

    void move(double deltaTime);
}
