package codeGeneration.environment;

public interface Environment<Value> {

    Environment<Value> beginScope();

    Environment<Value> endScope();

    int getDepth();

    void associate(final String key,
                   final Value value) throws DuplicateKeyException;

    Value find(final String key) throws UnknownKeyException;
}
