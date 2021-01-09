package util;

public final class Pair<X, Y> {

    private final X _xVal;
    private final Y _yVal;

    public Pair(final X xVal, final Y yVal) {
        _xVal = xVal;
        _yVal = yVal;
    }

    public final X getX() {
        return _xVal;
    }

    public final Y getY() {
        return _yVal;
    }
}
