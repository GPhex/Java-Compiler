package util;

public enum ConstantTypes implements Type {

    INTEGER("I", true),
    VOID("V", true),
    OBJECT("java/lang/Object", false);

    private final String _jvmRepresentation;
    private final boolean _primitive;

    ConstantTypes(final String jvmRepresentation, final boolean isPrimitive) {
        _jvmRepresentation = jvmRepresentation;
        _primitive = isPrimitive;
    }

    @Override
    public final String getName() {
        return _jvmRepresentation;
    }

    @Override
    public final String getLiteral() {
        return _primitive ? _jvmRepresentation : String.format("L%s;", _jvmRepresentation);
    }

}
