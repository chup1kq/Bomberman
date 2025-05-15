package model.field;

import model.enums.Direction;
import model.geometry.Position;
import model.logic.Collidable;
import model.logic.Renderable;
import model.logic.Updatable;
import model.object.GameObject;
import model.object.Portal;
import model.object.bomb.Bomb;
import model.object.wall.BreakableWall;
import model.strategy.BombermanStrategy.BombermanStrategy;
import model.unit.Bomberman;
import model.unit.Unit;
import model.unit.enemy.Enemy;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.*;
import java.util.List;

public class GameField implements Updatable, Renderable {

    //------------------------------- Размеры в клетках ----------------------------
    private final static int HEIGHT = 13;
    private final static int WIDTH = 17;

    private Bomberman _bomberman = new Bomberman(this, new BombermanStrategy(), null);

    private final List<Enemy> _enemies = new ArrayList<>();

    private final List<Cell> _cells = new ArrayList<>() {{
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                add(new Cell(new Position(
                        j * Cell.getDefaultSize().getWidth(),
                        i * Cell.getDefaultSize().getHeight()
                )));
            }
        }
    }};

    public GameField() {
        _bomberman.getStrategy().setUnit(_bomberman);
        mergeCells();
    }

    private void mergeCells() {
        for (Cell cell : _cells) {
            Position position = cell.position();

            for (Direction direction : Direction.values()) {
                Position neighborPosition = switch (direction) {
                    case NORTH -> new Position(position.getX(), position.getY() - Cell.getDefaultSize().getHeight());
                    case SOUTH -> new Position(position.getX(), position.getY() + Cell.getDefaultSize().getHeight());
                    case WEST -> new Position(position.getX() - Cell.getDefaultSize().getWidth(), position.getY());
                    case EAST -> new Position(position.getX() + Cell.getDefaultSize().getWidth(), position.getY());
                    case NORTH_EAST ->
                            new Position(position.getX() + Cell.getDefaultSize().getWidth(), position.getY() - Cell.getDefaultSize().getHeight());
                    case NORTH_WEST ->
                            new Position(position.getX() - Cell.getDefaultSize().getWidth(), position.getY() - Cell.getDefaultSize().getHeight());
                    case SOUTH_EAST ->
                            new Position(position.getX() + Cell.getDefaultSize().getWidth(), position.getY() + Cell.getDefaultSize().getHeight());
                    case SOUTH_WEST ->
                            new Position(position.getX() - Cell.getDefaultSize().getWidth(), position.getY() + Cell.getDefaultSize().getHeight());
                };

                Cell neighbor = getCellAt(neighborPosition);
                if (neighbor != null) {
                    cell.setNeighbor(neighbor, direction);
                }
            }
        }
    }

    public void setBomberman(Bomberman bomberman) {
        _bomberman = bomberman;
    }

    public Bomberman getBomberman() {
        return _bomberman;
    }

    public List<Enemy> getEnemies() {
        return _enemies;
    }

    public List<Cell> getCells() {
        return _cells;
    }

    public Portal getPortal() {
        for (Cell cell : _cells) {
            GameObject object = cell.getObject();

            if (object instanceof Portal portal) return portal;
            else if (object instanceof BreakableWall wall) {
                if (wall.getObject() instanceof Portal portal) return portal;
            }
        }

        return null;
    }

    public Cell getCellAt(Position position) {
        for (Cell cell : _cells) {
            if (cell.position().equals(position) ||
                    cell.intersects(position)) {
                return cell;
            }
        }

        return null;
    }

    public void spawnEnemy(Enemy enemy) {
        _enemies.add(enemy);
    }

    public void respawnBomberman() {
        _bomberman.setPosition(_cells.get(18).position());
    }

    public int countBombs(Unit owner) {
        int count = 0;

        for (Cell cell : _cells) {
            if (cell.getObject() instanceof Bomb bomb && bomb.getOwner() == owner) count++;
        }

        return count;
    }

    public void clear() {
        _bomberman.setPosition(null);
        _enemies.clear();
        _cells.forEach(Cell::deleteObject);
    }


    public <T extends Collidable> Optional<T> findColliding(@NotNull Collidable object, @NotNull Class<T> type) {
        return getPotentialColliders(type)
                .stream()
                .filter(candidate -> candidate != object)
                .filter(object::intersects)
                .findFirst();
    }


    private <T extends Collidable> Collection<T> getPotentialColliders(Class<T> type) {
        if (Enemy.class.isAssignableFrom(type)) {
            return (Collection<T>) _enemies;
        } else if (Bomberman.class.isAssignableFrom(type)) {
            return Collections.singletonList((T) _bomberman);
        } else if (Cell.class.isAssignableFrom(type)) {
            return (Collection<T>) _cells;
        } else if (GameObject.class.isAssignableFrom(type)) {
            return getAllGameObjects(type);
        }
        return Collections.emptyList();
    }


    private <T extends Collidable> Collection<T> getAllGameObjects(Class<T> type) {
        List<T> result = new ArrayList<>();
        for (Cell cell : _cells) {
            GameObject obj = cell.getObject();
            if (type.isInstance(obj)) {
                result.add(type.cast(obj));
            }
        }
        return result;
    }

    @Override
    public void update(double deltaTime) {
        _bomberman.update(deltaTime);
        _enemies.forEach(e -> e.update(deltaTime));
        _cells.forEach(c -> {
            Object object = c.getObject();
            if (object instanceof Updatable obj) {
                obj.update(deltaTime);
            }
        });

        _enemies.removeIf(Enemy::dead);
    }

    @Override
    public void render(Graphics g) {
        _cells.forEach(cell -> {
            cell.render(g);

            GameObject object = cell.getObject();
            if (object != null) object.render(g);
        });

        if (_bomberman.position() != null) _bomberman.render(g);
        _enemies.forEach(enemy -> enemy.render(g));
    }
}