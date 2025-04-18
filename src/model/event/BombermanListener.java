package model.event;

import java.util.EventListener;

public interface BombermanListener extends EventListener {

    void died(BombermanEvent event);

    void foundExit(BombermanEvent event);
}
