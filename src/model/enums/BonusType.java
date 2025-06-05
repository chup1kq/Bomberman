package model.enums;

import model.view.sprites.SpriteLoader;

import java.awt.image.BufferedImage;
import java.util.function.Supplier;

public enum BonusType {
    SPEED_BONUS(SpriteLoader::speedBonusTile),
    RADIUS_BONUS(SpriteLoader::radiusBonusTile),
    AMMUNITION_BONUS(SpriteLoader::ammunitionBonusTile),
    NON_CENTRAL_RADIUS_BONUS(SpriteLoader::nonCenterBombBonusTile),
    PROXIMITY_BOMB_BONUS(SpriteLoader::proximityBombBonusTile);

    private final Supplier<BufferedImage> _spriteSupplier;

    BonusType(Supplier<BufferedImage> spriteSupplier) {
        _spriteSupplier = spriteSupplier;
    }

    public BufferedImage getSprite() {
        return _spriteSupplier.get();
    }
}
