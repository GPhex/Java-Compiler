package codeGeneration.environment;

final class UnboundIdentifierException extends RuntimeException {

    public UnboundIdentifierException(final String identifier, final int depth) {
        super(String.format("Identifier %s is not bounded in scope of depth %d", identifier, depth));
    }
}
