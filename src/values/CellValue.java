package values;

public final class CellValue implements Value<Value<?>> {

    private Value<?> _value;

    public CellValue(final Value<?> value) {
        _value = value;
    }

    @Override
    public final Value<?> getValue() {
        return _value;
    }

    public void set(final Value<?> value) {
        _value = value;
    }

    @Override
    public String toString() {
        return "Cell of " + _value.toString();
    }
}
