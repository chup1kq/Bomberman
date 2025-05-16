package model.object.wall;

import model.field.Cell;
import model.view.sprites.SpriteLoader;

import java.awt.*;

public class UnbreakableWall extends Wall {

    public UnbreakableWall(Cell cell) {
        super(cell);
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(
                SpriteLoader.unbreakableWallTile(),
                (int) position().getX(),
                (int) position().getY(),
                (int) size().getWidth(),
                (int) size().getHeight(),
                null
        );
    }
}
