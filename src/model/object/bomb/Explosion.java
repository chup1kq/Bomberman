package model.object.bomb;

import model.enums.Orientation;
import model.event.TimerEvent;
import model.event.TimerListener;
import model.field.Cell;
import model.geometry.Size;
import model.logic.Damageable;
import model.logic.Updatable;
import model.object.GameObject;
import model.enums.Direction;
import model.timer.Timer;
import model.view.sprites.SpriteLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.Map;

public class Explosion extends GameObject implements Updatable {

    private static final Map<Orientation, Size> DEFAULT_SIZES = new EnumMap<>(Orientation.class) {{
        put(Orientation.HORIZONTAL, new Size(40, 30));
        put(Orientation.VERTICAL, new Size(30, 40));
        put(Orientation.OMNIDIRECTIONAL, new Size(40, 40));
    }};

    private static final Map<Direction, Orientation> DIRECTIONS = new EnumMap<>(Direction.class){{
        put(Direction.NORTH, Orientation.VERTICAL);
        put(Direction.SOUTH, Orientation.VERTICAL);
        put(Direction.WEST, Orientation.HORIZONTAL);
        put(Direction.EAST, Orientation.HORIZONTAL);
    }};

    private Orientation _orientation;

    private int _explosionRange;

    private int _damage;

    private final Timer _timer = new Timer(1000);

    //----------------------------------------------------------------

    public void setDamage(int damage) {
        _damage = damage;
    }

    public int getDamage() {
        return _damage;
    }

    //----------------------------------------------------------------

    public int getExplosionRange() {
        return _explosionRange;
    }

    void setExplosionRange(int explosionRange) {
        _explosionRange = explosionRange;
    }

    //----------------------------------------------------------------

    public Orientation getOrientation() {
        return _orientation;
    }

    void setOrientation(Orientation orientation) {
        _orientation = orientation;
    }

    //----------------------------------------------------------------

    public static void createExplosion(Cell cell, int radius, int damage) {
        new Explosion(cell, radius, Orientation.OMNIDIRECTIONAL, damage);

        for (Direction dir : DIRECTIONS.keySet()) {
            Cell current = cell;
            for (int i = 1; i <= radius; i++) {
                current = current.getNeighbor(dir);
                if (current == null) break;

                GameObject obj = current.getObject();
                if (obj != null) {
                    if (obj instanceof Damageable) {
                        ((Damageable)obj).takeDamage(damage);
                    }
                    break;
                }

                Orientation orientation = DIRECTIONS.get(dir);
                new Explosion(current, radius - i, orientation, damage);
            }
        }
    }


    private Explosion(Cell cell, int radius, Orientation orientation, int damage) {
        super(cell, DEFAULT_SIZES.get(Orientation.OMNIDIRECTIONAL));

        _orientation = orientation;
        _damage = damage;
        _timer.start();
        TimerObserver observer = new TimerObserver();
        _timer.addListener(observer);
    }


    @Override
    public void update(double deltaTime) {
        _timer.update(deltaTime);
    }

    @Override
    public void render(Graphics g) {
        BufferedImage image;

        switch (_orientation) {
            case OMNIDIRECTIONAL:
                image = SpriteLoader.omnidirectionalExplosionTile();
                break;
            case HORIZONTAL:
                image = SpriteLoader.horizontalExplosionTile();
                break;
            case VERTICAL:
                image = SpriteLoader.verticalExplosionTile();
                break;
            default:
                return;
        }

        double offsetX = (Cell.getDefaultSize().getWidth() - size().getWidth()) / 2;
        double offsetY = (Cell.getDefaultSize().getWidth() - size().getHeight()) / 2;

        g.drawImage(
                image,
                (int) (position().getX() + offsetX),
                (int) (position().getY() + offsetY),
                (int) size().getWidth(),
                (int) size().getHeight(),
                null
        );
    }


    private class TimerObserver implements TimerListener {

        @Override
        public void timeIsOver(TimerEvent event) {
            if (_timer == event.getTimer()) {
                getCell().deleteObject();
            }
        }
    }
}
