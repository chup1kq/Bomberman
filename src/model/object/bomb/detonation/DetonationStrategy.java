package model.object.bomb.detonation;

import model.object.bomb.Bomb;

public interface DetonationStrategy {

    boolean shouldExplode(Bomb bomb, double deltaTime);
}
