package model.geometry;

public class Size {

    private double _width;

    private double _height;

    public Size(double width, double height) {
        _width = width;
        _height = height;
    }

    //--------------------------- Width -------------------------

    public double getWidth() {
        return _width;
    }

    public void setWidth(double width) {
        _width = width;
    }

    //--------------------------- Height -------------------------

    public double getHeight() {
        return _height;
    }

    public void setHeight(double height) {
        _height = height;
    }

    //------------------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;

        if (!(o instanceof Size size))
            return false;

        return Double.compare(_height, size._height) == 0 &&
                Double.compare(_width, size._width) == 0;
    }

    @Override
    public int hashCode() {
        return 31 * Double.hashCode(_width) + Double.hashCode(_height);
    }
}
