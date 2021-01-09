package compilation;

public final class TypeException extends RuntimeException {

    public TypeException(final String operation, final String message) {
        super(String.format("Type error on %s: %s", operation, message));
    }
}
