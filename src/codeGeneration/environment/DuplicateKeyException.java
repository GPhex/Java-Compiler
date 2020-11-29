package codeGeneration.environment;

public final class DuplicateKeyException extends RuntimeException {

    public DuplicateKeyException(final String key, final int scopeDepth) {
        super(String.format("Attempted association of existing key %s at %d scope depth.", key, scopeDepth));
    }
}
