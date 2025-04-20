package model.event;

import model.unit.Unit;

import java.util.EventObject;

public class UnitEvent extends EventObject {

    private Unit _unit;

    public Unit getUnit() {
        return _unit;
    }

    public void setUnit(Unit unit) {
        _unit = unit;
    }

    public UnitEvent(Object source) {
        super(source);
    }
}
