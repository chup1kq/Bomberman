package model.event;

import model.Game;

import java.util.EventObject;

public class GameEvent extends EventObject {

    private Game _game;

    public Game getGame() {
        return _game;
    }

    public void setGame(Game game) {
        _game = game;
    }

    public GameEvent(Object source) {
        super(source);
    }
}
