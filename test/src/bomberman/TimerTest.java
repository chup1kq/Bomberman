package bomberman;

import model.event.TimerEvent;
import model.event.TimerListener;
import model.timer.Timer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TimerTest {
    static private Timer timer;
    private final double MAX_TIME = 5.0;
    private boolean isTimeUp = false;


    private class TimerObserver implements TimerListener {
        @Override
        public void timeIsOver(TimerEvent e) {
            isTimeUp = true;
            assertSame(timer, e.getTimer());
        }
    }

    @BeforeEach
    void setUp() {
        isTimeUp = false;
        timer = new Timer(MAX_TIME);
    }

    @Test
    @DisplayName("Таймер правильно инициализируется")
    void testInitialization() {
        assertEquals(0.0, timer.getTime(), 0.001);
        assertFalse(timer.isRunning());
    }

    @Test
    @DisplayName("Старт таймера")
    void testStart() {
        timer.start();
        assertTrue(timer.isRunning());
    }

    @Test
    @DisplayName("Остановка таймера")
    void testStop() {
        timer.start();
        timer.stop();
        assertFalse(timer.isRunning());
    }

    @Test
    @DisplayName("Сброс таймера")
    void testReset() {
        timer.start();
        timer.update(2.0);
        timer.reset();
        assertEquals(0.0, timer.getTime(), 0.001);
    }

    @Test
    @DisplayName("Обновление времени работающего таймера")
    void testUpdateRunningTimer() {
        timer.start();
        timer.update(1.5);
        assertEquals(1.5, timer.getTime(), 0.001);
    }

    @Test
    @DisplayName("Обновление времени остановленного таймера")
    void testUpdateStoppedTimer() {
        timer.update(1.5);
        assertEquals(0.0, timer.getTime(), 0.001);
    }

    @Test
    @DisplayName("Таймер завершается при достижении максимального времени")
    void testTimerFinishes() {
        TimerListener listener = new TimerObserver();
        timer.addListener(listener);

        timer.start();
        timer.update(MAX_TIME);

        assertFalse(timer.isRunning());
        assertTrue(isTimeUp);
    }

    @Test
    @DisplayName("Таймер не завершается раньше времени")
    void testTimerNotFinishedEarly() {
        TimerListener listener = new TimerObserver();
        timer.addListener(listener);

        timer.start();
        timer.update(MAX_TIME - 0.1);

        assertTrue(timer.isRunning());
        assertFalse(isTimeUp);
    }
}
