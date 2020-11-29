package codeGeneration.environment;

public final class UnknownKeyException extends RuntimeException {

    public UnknownKeyException(final String key, final int scopeDepth) {
        super(String.format("Attempted retrieval of unassigned key %s at %d scope depth", key, scopeDepth));
    }
}
