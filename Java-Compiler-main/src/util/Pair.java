package util;

public final class Pair<XVal, YVal> {

    private final XVal _xVal;
    private final YVal _yVal;

    public Pair(final XVal xval,
                final YVal yval) {
        _xVal = xval;
        _yVal = yval;
    }

    public final XVal getX()  {
        return _xVal;
    }

    public final YVal getY()  {
        return _yVal;
    }

}
