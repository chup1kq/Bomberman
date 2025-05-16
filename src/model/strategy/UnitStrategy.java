package model.strategy;

import model.logic.Updatable;
import model.unit.Unit;

public abstract class UnitStrategy implements Updatable {

    private Unit _unit;

    public Unit getUnit() {
        return _unit;
    }

    public void setUnit(Unit unit) {
        _unit = unit;
    }

    @Override
    public abstract void update(double deltaTime);
}
