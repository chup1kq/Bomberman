package model.event;

import model.timer.Timer;

import java.util.EventObject;

public class TimerEvent extends EventObject {

    private Timer _timer;

    public Timer getTimer() {
        return _timer;
    }

    public void setTimer(Timer timer) {
        _timer = timer;
    }

    public TimerEvent(Object source) {
        super(source);
    }
}
