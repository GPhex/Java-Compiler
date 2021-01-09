package codeGeneration.programGeneration;

public enum BaseTypes implements CompilerType {

    INTEGER("I",true),
    STRING("java/lang/String",false),
    OBJECT("java/lang/Object",false),
    VOID("V",true);

    private final String _literal, _descriptor;
    private final boolean _isPrimitive;

    BaseTypes(final String literal,
                final boolean isPrimitive) {
        _literal = literal;
        _isPrimitive = isPrimitive;
        _descriptor = _isPrimitive ? _literal : String.format("L%s;", _literal);
    }

    @Override
    public final String asLiteral() {
        return _literal;
    }

    @Override
    public final boolean isPrimitive() {
        return _isPrimitive;
    }

    @Override
    public final String asDescriptor() {
        return _descriptor;
    }
}