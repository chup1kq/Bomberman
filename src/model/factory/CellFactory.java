package model.factory;

import model.field.Cell;
import model.geometry.Position;

public class CellFactory {

    public Cell createCell(Position position) {
        return new Cell(position);
    }
}
