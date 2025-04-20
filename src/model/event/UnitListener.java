package model.event;

import java.util.EventListener;

public interface UnitListener extends EventListener {

    void died(UnitEvent event);

    void foundExit(UnitEvent event);
}
