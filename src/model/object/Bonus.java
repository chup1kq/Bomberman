package model.object;

import model.enums.BonusType;
import model.field.Cell;
import model.geometry.Size;
import model.view.sprites.SpriteLoader;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Bonus extends GameObject {

    private static final Size DEFAULT_SIZE = new Size(40, 40);

    private final BonusType _type;

    public Bonus(@NotNull BonusType type) {
        super(null, DEFAULT_SIZE);

        _type = type;
    }

    public Bonus(Cell cell, @NotNull BonusType type) {
        super(cell, DEFAULT_SIZE);

        _type = type;
    }

    public BonusType getType() {
        return _type;
    }

    @Override
    public void render(Graphics g) {
        BufferedImage image;

        switch (_type) {
            case SPEED_BONUS:
                image = SpriteLoader.speedBonusTile();
                break;
            case RADIUS_BONUS:
                image = SpriteLoader.radiusBonusTile();
                break;
            case AMMUNITION_BONUS:
                image = SpriteLoader.ammunitionBonusTile();
                break;
            default:
                return;
        }

        double offsetX = (Cell.getDefaultSize().getWidth() - size().getWidth()) / 2.0;
        double offsetY = (Cell.getDefaultSize().getHeight() - size().getHeight()) / 2.0;

        g.drawImage(
                image,
                (int) (position().getX() + offsetX),
                (int) (position().getY() + offsetY),
                (int) size().getWidth(),
                (int) size().getHeight(),
                null
        );
    }

}
