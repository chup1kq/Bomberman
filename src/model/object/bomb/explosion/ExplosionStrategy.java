package model.object.bomb.explosion;

import model.field.Cell;

public interface ExplosionStrategy {

    void createExplosion(Cell center, int radius, int damage);
}
