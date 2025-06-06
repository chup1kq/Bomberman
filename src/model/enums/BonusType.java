package model.enums;

import model.view.sprites.SpriteLoader;
import model.object.bomb.BombSettings;
import model.object.bomb.detonation.ProximityStrategy;
import model.object.bomb.detonation.TimerStrategy;
import model.object.bomb.explosion.CrossStrategy;
import model.object.bomb.explosion.WaveStrategy;

import java.awt.image.BufferedImage;
import java.util.function.Supplier;

public enum BonusType {
    SPEED_BONUS(SpriteLoader::speedBonusTile, null),
    RADIUS_BONUS(SpriteLoader::radiusBonusTile, null),
    AMMUNITION_BONUS(SpriteLoader::ammunitionBonusTile, null),
    NON_CENTRAL_RADIUS_BONUS(
            SpriteLoader::nonCenterBombBonusTile,
            new BombSettings(TimerStrategy.class, WaveStrategy.class)
    ),
    PROXIMITY_BOMB_BONUS(
            SpriteLoader::proximityBombBonusTile,
            new BombSettings(ProximityStrategy.class, CrossStrategy.class)
    );

    private final Supplier<BufferedImage> spriteSupplier;
    private final BombSettings bombSettings;

    BonusType(Supplier<BufferedImage> spriteSupplier, BombSettings bombSettings) {
        this.spriteSupplier = spriteSupplier;
        this.bombSettings = bombSettings;
    }

    public BufferedImage getSprite() {
        return spriteSupplier.get();
    }

    public BombSettings getBombSettings() {
        return bombSettings;
    }
}