package model.event;

import java.util.EventListener;

public interface TimerListener extends EventListener {

    void timeIsOver(TimerEvent event);
}
