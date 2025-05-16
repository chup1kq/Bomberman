package model;

import model.enums.BonusType;
import model.factory.AbstractFactory;
import model.field.Cell;
import model.geometry.Position;
import model.level.Level;
import model.field.GameField;
import model.unit.enemy.Ballom;
import model.unit.enemy.Dahl;

public class Maze {

    private GameField _field;

    private AbstractFactory _factory;

    public Maze(AbstractFactory factory) {
        _factory = factory;
    }

    public void setField(GameField field) {
        _field = field;
    }

    public GameField getField() {
        return _field;
    }

    public void setFactory(AbstractFactory factory) {
        _factory = factory;
    }

    public AbstractFactory getFactory() {
        return _factory;
    }

    public String[][] loadLevel(int levelId) {
        return Level.getInstance().getLevel(levelId);
    }

    public void buildField(int level) {
        String[][] levelData = loadLevel(level);

        for (int i = 0; i < levelData.length; i++) {
            for (int j = 0; j < levelData[i].length; j++) {
                String cellData = levelData[i][j];
                Position position = new Position(
                        j * Cell.getDefaultSize().getWidth(),
                        i * Cell.getDefaultSize().getHeight()
                );
                Cell cell = _field.getCellAt(position);

                switch (cellData) {
                    case "#" -> _factory.createUnbreakableWall(cell);
                    case "*" -> _factory.createBreakableWall(cell);
                    case "B" -> _field.getBomberman().setPosition(position);
                    case "b" -> _field.spawnEnemy(new Ballom(_field, position));
                    case "d" -> _field.spawnEnemy(new Dahl(_field, position));
                    case "p" -> _factory.createPortalInWall(cell);
                    case "s" -> _factory.createBonusInWall(cell, BonusType.SPEED_BONUS);
                    case "a" -> _factory.createBonusInWall(cell, BonusType.AMMUNITION_BONUS);
                    case "r" -> _factory.createBonusInWall(cell, BonusType.RADIUS_BONUS);
                }
            }
        }
    }
}
