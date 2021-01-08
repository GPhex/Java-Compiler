package codeGeneration.environment;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public final class NullEnvironment<Value> implements Environment<Value> {

    public NullEnvironment() {

    }

    @Override
    public final Environment<Value> beginScope() {
        return new LinkedEnvironment<>(null);
    }

    @Override
    public Environment<Value> endScope() {
        throw new NotImplementedException();
    }

    @Override
    public int getDepth() {
        throw new NotImplementedException();
    }

    @Override
    public void associate(String key, Value value) throws DuplicateKeyException {
        throw new NotImplementedException();
    }

    @Override
    public Value find(String key) throws UnknownKeyException {
        throw new NotImplementedException();
    }
}
