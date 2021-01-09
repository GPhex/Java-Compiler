package codeGeneration.environment;

final class DuplicateIdentifierException extends RuntimeException {

    DuplicateIdentifierException(final String identifier, final int depth) {
        super(String.format("Cannot create two identifiers with the same name %s in the same scope %d", identifier, depth));
    }
}
