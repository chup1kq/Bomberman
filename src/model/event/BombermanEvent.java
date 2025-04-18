package model.event;

import model.unit.Bomberman;

import java.util.EventObject;

public class BombermanEvent extends EventObject {

    private Bomberman _bomberman;

    public Bomberman getBomberman() {
        return _bomberman;
    }

    public void setBomberman(Bomberman bomberman) {
        _bomberman = bomberman;
    }

    public BombermanEvent(Object source) {
        super(source);
    }
}
