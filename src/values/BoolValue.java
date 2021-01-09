package values;

public final class BoolValue implements Value<Boolean> {

    private final boolean _value;

    public BoolValue(final boolean value) {
        _value = value;
    }

    @Override
    public final Boolean getValue() {
        return _value;
    }

    public final BoolValue and(final BoolValue other) {
        return new BoolValue(_value && other._value);
    }

    public final BoolValue or(final BoolValue other) {
        return new BoolValue(_value || other._value);
    }

    public final BoolValue not() {
        return new BoolValue(!_value);
    }

    @Override
    public String toString() {
        return String.valueOf(_value);
    }
}
