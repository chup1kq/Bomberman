package model.object.bomb.detonation;

import model.object.bomb.Bomb;
import org.jetbrains.annotations.NotNull;

public interface DetonationStrategy {

    boolean shouldExplode(@NotNull Bomb bomb, double deltaTime);
}
