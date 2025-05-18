package model.factory;

import model.enums.BonusType;
import model.field.Cell;
import model.object.Bonus;
import model.object.GameObject;
import model.object.Portal;
import model.object.bomb.Bomb;
import model.object.wall.BreakableWall;
import model.object.wall.UnbreakableWall;

public class GameObjectFactory extends AbstractFactory  {

    private BreakableWall createBreakableWallWithObject(Cell cell, GameObject object) {
        BreakableWall wall = createBreakableWall(cell);
        wall.setObject(object);

        return wall;
    }

    @Override
    public UnbreakableWall createUnbreakableWall(Cell cell) {
        return new UnbreakableWall(cell);
    }

    @Override
    public BreakableWall createBreakableWall(Cell cell) {
        return new BreakableWall(cell);
    }

    @Override
    public BreakableWall createBonusInWall(Cell cell, BonusType type) {
        return createBreakableWallWithObject(cell, new Bonus(type));
    }

    @Override
    public BreakableWall createPortalInWall(Cell cell) {
        return createBreakableWallWithObject(cell, new Portal());
    }
}