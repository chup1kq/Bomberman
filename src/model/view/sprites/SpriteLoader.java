package model.view.sprites;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public abstract class SpriteLoader {

    public static BufferedImage spriteSheet;

    private static final int TILE_SIZE = 16;

    private static final String SPRITES_PATH = "/model/view/sprites/classic.png";

    static {
        try {
            BufferedImage sprite = ImageIO.read(SpriteLoader.class.getResourceAsStream(SPRITES_PATH));
            spriteSheet = makeTransparent(sprite, new Color(255, 0, 255));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static BufferedImage makeTransparent(BufferedImage image, Color colorToRemove) {
        BufferedImage result = new BufferedImage(
                image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = image.getRGB(x, y);
                if ((pixel & 0x00FFFFFF) == (colorToRemove.getRGB() & 0x00FFFFFF)) {
                    result.setRGB(x, y, 0x00000000);
                } else {
                    result.setRGB(x, y, pixel);
                }
            }
        }

        return result;
    }


    public static BufferedImage grassTile() {
        return spriteSheet.getSubimage(6 * TILE_SIZE, 0 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    public static BufferedImage bombermanTile() {
        return spriteSheet.getSubimage(2 * TILE_SIZE, 0 * TILE_SIZE, 12, TILE_SIZE);
    }

    public static BufferedImage unbreakableWallTile() {
        return spriteSheet.getSubimage(5 * TILE_SIZE, 0 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    public static BufferedImage breakableWallTile() {
        return spriteSheet.getSubimage(7 * TILE_SIZE, 0 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    public static BufferedImage portalTile() {
        return spriteSheet.getSubimage(4 * TILE_SIZE, 0 * TILE_SIZE, 14, 14);
    }

    public static BufferedImage bombTile() {
        return spriteSheet.getSubimage(0 * TILE_SIZE, 3 * TILE_SIZE, 15, 15);
    }

    public static BufferedImage omnidirectionalExplosionTile() {
        return spriteSheet.getSubimage(0 * TILE_SIZE, 4 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    public static BufferedImage verticalExplosionTile() {
        return spriteSheet.getSubimage(1 * TILE_SIZE, 5 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    public static BufferedImage horizontalExplosionTile() {
        return spriteSheet.getSubimage(1 * TILE_SIZE, 7 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    public static BufferedImage speedBonusTile() {
        return spriteSheet.getSubimage(3 * TILE_SIZE, 10 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    public static BufferedImage ammunitionBonusTile() {
        return spriteSheet.getSubimage(0 * TILE_SIZE, 10 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    public static BufferedImage radiusBonusTile() {
        return spriteSheet.getSubimage(1 * TILE_SIZE, 10 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    public static BufferedImage nonCenterBombBonusTile() {
        return spriteSheet.getSubimage(2 * TILE_SIZE, 10 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    public static BufferedImage proximityBombBonusTile() {
        return spriteSheet.getSubimage(6 * TILE_SIZE, 10 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    public static BufferedImage dahlTile() {
        return spriteSheet.getSubimage(11 * TILE_SIZE, 0 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    public static BufferedImage ballomTile() {
        return spriteSheet.getSubimage(9 * TILE_SIZE, 0 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }
}
