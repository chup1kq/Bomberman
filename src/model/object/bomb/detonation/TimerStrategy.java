package model.object.bomb.detonation;

import model.event.TimerEvent;
import model.event.TimerListener;
import model.object.bomb.Bomb;
import model.timer.Timer;

public class TimerStrategy implements DetonationStrategy {

    private final Timer _timer;

    private boolean _shouldExplode = false;

    public TimerStrategy(Timer timer) {
        _timer = timer;

        TimerObserver observer = new TimerObserver();
        _timer.addListener(observer);

        _timer.start();
    }

    @Override
    public boolean shouldExplode(Bomb bomb, double deltaTime) {
        _timer.update(deltaTime);
        return _shouldExplode;
    }

    private class TimerObserver implements TimerListener {

        @Override
        public void timeIsOver(TimerEvent event) {
            _shouldExplode = true;
        }
    }
}
