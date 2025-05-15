package model.geometry;

public class Position {

    private double _x;

    private double _y;

    public Position(double x, double y) {
        _x = x;
        _y = y;
    }

    public double getX() {
        return _x;
    }

    public double getY() {
        return _y;
    }

    public void setX(double x) {
        _x = x;
    }

    public void setY(double y) {
        _y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;

        if (!(o instanceof Position position))
            return false;

        return Double.compare(_x, position._x) == 0 &&
                Double.compare(_y, position._y) == 0;
    }

    @Override
    public int hashCode() {
        return 31 * Double.hashCode(_x) + Double.hashCode(_y);
    }

    @Override
    public String toString() {
        return "(" + _x + "," + _y  + ")";
    }
}
