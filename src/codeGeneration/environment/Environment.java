package codeGeneration.environment;

public interface Environment<Storage> {

    Environment<Storage> beginScope();

    Environment<Storage> endScope();

    int depth();

    void associate(final String id,
                   final Storage value) throws DuplicateIdentifierException;

    Storage find(final String id) throws UnboundIdentifierException;
}
