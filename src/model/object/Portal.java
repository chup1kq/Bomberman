package model.object;

import model.field.Cell;
import model.geometry.Size;
import model.view.sprites.SpriteLoader;

import java.awt.*;

public class Portal extends GameObject {

    private static final Size DEFAULT_SIZE = new Size(35, 35);

    private boolean _isOpened = false;

    public Portal(){
        super(null, DEFAULT_SIZE);
    }

    public Portal(Cell cell) {
        super(cell, DEFAULT_SIZE);
    }

    public boolean isOpened(){
        return _isOpened;
    }

    public void open(){
        _isOpened = true;
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(
                SpriteLoader.portalTile(),
                (int) position().getX(),
                (int) position().getY(),
                (int) size().getWidth(),
                (int) size().getHeight(),
                null
        );
    }
}
