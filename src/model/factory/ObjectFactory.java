package model.factory;

import model.enums.BonusType;
import model.field.Cell;
import model.object.Portal;
import model.object.bomb.Bomb;
import model.object.bomb.Explosion;
import model.object.wall.BreakableWall;
import model.object.wall.UnbreakableWall;

public interface ObjectFactory {

    UnbreakableWall createUnbreakableWall(Cell cell);

    BreakableWall createBreakableWall(Cell cell);

    BreakableWall createBonusInWall(Cell cell, BonusType type);

    BreakableWall createPortalInWall(Cell cell);


}
