package model;

import model.view.FieldPanel;
import model.view.GamePanel;
import org.jetbrains.annotations.NotNull;

import javax.swing.Timer;

public class GameCycle {
    private static final int TARGET_FRAME_RATE = 60;
    private static final int TARGET_FRAME_TIME = 1000 / TARGET_FRAME_RATE;

    private boolean _running;
    private final Game _game;
    private final GamePanel _gamePanel;
    private final FieldPanel _fieldPanel;
    private long _lastUpdateTime;

    public GameCycle(@NotNull Game game, @NotNull GamePanel panel) {
        _running = false;
        _game = game;
        _gamePanel = panel;
        _fieldPanel = panel.getFieldPanel();
        _lastUpdateTime = System.nanoTime();
    }

    public void start() {
        _running = true;
        _lastUpdateTime = System.nanoTime();
        Timer _timer = new Timer(TARGET_FRAME_TIME, e -> {
            if (_running) {
                update();
                _fieldPanel.repaint();
            }
        });
        _timer.start();
    }

    public void update() {
        long currentTime = System.nanoTime();
        double deltaTime = (currentTime - _lastUpdateTime) / 1_000_000.0;
        _lastUpdateTime = currentTime;

        deltaTime = Math.min(deltaTime, 100);

        _game.update(deltaTime);
        _gamePanel.updateStatistic();
    }

    public void stop() {
        _running = false;
    }
}
