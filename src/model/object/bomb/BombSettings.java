package model.object.bomb;

import model.object.bomb.detonation.DetonationStrategy;
import model.object.bomb.detonation.TimerStrategy;
import model.object.bomb.explosion.CrossStrategy;
import model.object.bomb.explosion.ExplosionStrategy;

public class BombSettings {
    private final Class<? extends DetonationStrategy> _detonationType;
    private final Class<? extends ExplosionStrategy> _explosionType;

    public BombSettings() {
        this(TimerStrategy.class, CrossStrategy.class);
    }

    public BombSettings(Class<? extends DetonationStrategy> detonationType,
                        Class<? extends ExplosionStrategy> explosionType) {
        _detonationType = detonationType;
        _explosionType = explosionType;
    }

    public Class<? extends DetonationStrategy> getDetonationType() {
        return _detonationType;
    }

    public Class<? extends ExplosionStrategy> getExplosionType() {
        return _explosionType;
    }
}
