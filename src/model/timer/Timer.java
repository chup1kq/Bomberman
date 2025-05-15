package model.timer;

import model.logic.Updatable;
import model.event.TimerEvent;
import model.event.TimerListener;

import java.util.ArrayList;
import java.util.List;

public class Timer implements Updatable {

    // milliseconds
    private double _time;

    private final double _maxTime;

    private boolean _isRunning;

    public Timer(double maxTime) {
        _maxTime = maxTime;
        _time = 0;
        _isRunning = false;
    }

    public void start() {
        _isRunning = true;
    }

    public void stop() {
        _isRunning = false;
    }

    public void reset() {
        _time = 0;
    }

    public double getTime() {
        return _time;
    }

    public boolean isRunning() {
        return _isRunning;
    }

    private final List<TimerListener> _listeners = new ArrayList<>();

    public void addListener(TimerListener listener) {
        _listeners.add(listener);
    }

    public void removeListener(TimerListener listener) {
        _listeners.remove(listener);
    }

    public void fireTimeIsOver(TimerEvent event) {
        _listeners.forEach(listener -> listener.timeIsOver(event));
    }

    @Override
    public void update(double deltaTime) {
        if (_isRunning) {
            _time += deltaTime;
            if (_time >= _maxTime) {
                _isRunning = false;

                TimerEvent event = new TimerEvent(this);
                event.setTimer(this);
                fireTimeIsOver(event);
            }
        }
    }

    @Override
    public String toString() {
        int seconds = (int) (_time / 1000);
        int min = seconds / 60;
        int sec = seconds % 60;

        return String.format("%02d:%02d", min, sec);
    }
}
