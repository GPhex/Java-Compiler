package values;

public final class IntValue implements Value<Integer> {

    private final int _value;

    public IntValue(final int value) {
        _value = value;
    }

    @Override
    public final Integer getValue() {
        return _value;
    }

    public IntValue add(final IntValue other) {
        return new IntValue(_value + other._value);
    }

    public IntValue sub(final IntValue other) {
        return new IntValue(_value - other._value);
    }

    public IntValue mul(final IntValue other) {
        return new IntValue(_value * other._value);
    }

    public IntValue div(final IntValue other) {
        return new IntValue(_value / other._value);
    }

    public IntValue mod(final IntValue other) {
        return new IntValue(_value % other._value);
    }

    public IntValue neg() {
        return new IntValue(-_value);
    }

    public BoolValue isEqual(final IntValue other) {
        return new BoolValue(_value == other._value);
    }

    public BoolValue isGreaterThan(final IntValue other) {
        return new BoolValue(_value > other._value);
    }

    public BoolValue isGreaterEqual(final IntValue other) {
        return new BoolValue(_value >= other._value);
    }

    public BoolValue isLesserThan(final IntValue other) {
        return new BoolValue(_value < other._value);
    }

    public BoolValue isLesserEqual(final IntValue other) {
        return new BoolValue(_value <= other._value);
    }

    @Override
    public String toString() {
        return String.valueOf(_value);
    }
}
