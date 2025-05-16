package model.event;

public interface GameListener {

    void onVictory(GameEvent event);

    void onDefeat(GameEvent event);
}
