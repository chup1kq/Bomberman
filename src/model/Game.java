package model;

import model.enums.GameStatus;
import model.event.*;
import model.factory.GameObjectFactory;
import model.field.GameField;
import model.level.Level;
import model.logic.Renderable;
import model.logic.Updatable;
import model.object.Portal;
import model.strategy.BombermanStrategy.BombermanStrategy;
import model.timer.Timer;
import model.unit.Bomberman;
import model.unit.enemy.Enemy;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Game implements Updatable, Renderable {

    private static final double GAME_TIME = 180_000.00;

    private static final int MAX_LEVEL = Level.getInstance().getMaxLevel();

    private GameStatus _status;

    private int _level;

    private Timer _timer;

    private final GameField _field = new GameField();

    private final Maze _maze = new Maze(new GameObjectFactory());

    public Game() {
        _status = GameStatus.STOPPED;
        _level = 1;
        _timer = new Timer(GAME_TIME);

        TimerObserver timerObserver = new TimerObserver();
        _timer.addListener(timerObserver);
    }

    //--------------------------------------------

    public GameField getField() {
        return _field;
    }

    //--------------------------------------------

    public void setTimer(Timer timer) {
        _timer = timer;
    }

    public Timer getTimer() {
        return _timer;
    }

    //--------------------------------------------

    public GameStatus getStatus() {
        return _status;
    }

    public void setStatus(GameStatus status) {
        _status = status;
    }

    public void start() {
        _status = GameStatus.RUNNING;
        _field.setNewBomberman();
        _maze.setField(_field);
        buildField(_level);
        _timer.start();

        UnitObserver unitObserver = new UnitObserver();
        _field.getBomberman().addListener(unitObserver);

        // Это я убрал, тк слушать не надо
//        _field.getEnemies().forEach(e -> {
//            UnitObserver observer = new UnitObserver();
//            e.addListener(observer);
//        });
    }

    public void finish() {
        _status = GameStatus.STOPPED;
        GameEvent event = new GameEvent(this);
        event.setGame(Game.this);
        fireDefeat(event);
    }

    public void continueGame() {
        _status = GameStatus.RUNNING;
    }

    public void end() {
        _status = GameStatus.STOPPED;
    }

    public void restart() {
        exit();
        start();
    }

    public void stop() {
        _status = GameStatus.STOPPED;
    }

    public void moveToNextLevel() {
        _level++;

        if (_level <= MAX_LEVEL) {
            _field.clear();
            buildField(_level);
        } else {
            end();
            GameEvent event = new GameEvent(this);
            event.setGame(this);
            fireVictory(event);
        }
    }

    private void buildField(int level) {
        _maze.buildField(level);
    }

    @Override
    public void update(double deltaTime) {
        if (_status == GameStatus.RUNNING) {
            _timer.update(deltaTime);
            _field.update(deltaTime);
            portalCheck();
        }
    }

    public void exit() {
        _status = GameStatus.STOPPED;
        _field.clear();
        _timer.reset();
        _level = 1;
        _timer.reset();
        _field.setNewBomberman();
    }

    private void portalCheck() {
        if (!_field.getEnemies().isEmpty()) return;

        Portal portal = _field.getPortal();
        if (portal != null) portal.open();
    }

    @Override
    public void render(Graphics g) {
        _field.render(g);
    }

    private class TimerObserver implements TimerListener {

        @Override
        public void timeIsOver(TimerEvent event) {
            if (event.getTimer() == _timer) end();

            GameEvent event1 = new GameEvent(this);
            event1.setGame(Game.this);
            fireDefeat(event1);
        }
    }

    private class UnitObserver implements UnitListener {

        @Override
        public void died(UnitEvent event) {
            if (event.getUnit() instanceof Bomberman) {
                if (_field.getBomberman().getHealthPoint() <= 0) {
                    finish();
                    return;
                }

                _field.respawnBomberman();
                return;
            }

            if (event.getUnit() instanceof Enemy) {
                if (_field.getEnemies().isEmpty()) {
                    Portal portal = _field.getPortal();

                    if (portal != null) portal.open();
                }
            }
        }

        @Override
        public void foundExit(UnitEvent event) {
            if (event.getUnit() instanceof Enemy)
                return;

            if (event.getUnit() instanceof Bomberman) {
                moveToNextLevel();
            }
        }
    }

    private final List<GameListener> _listeners = new ArrayList<>();

    public void addListener(GameListener listener) {
        _listeners.add(listener);
    }

    public void removeListener(GameListener listener) {
        _listeners.remove(listener);
    }

    public void fireVictory(GameEvent event) {
        _listeners.forEach(listener -> listener.onVictory(event));
    }

    public void fireDefeat(GameEvent event) {
        _listeners.forEach(listener -> listener.onDefeat(event));
    }
}
