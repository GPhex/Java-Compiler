package codeGeneration.environment;

import java.util.HashMap;
import java.util.Map;

public final class LinkedEnvironment<Value> implements Environment<Value> {

    private final Map<String, Value> _mappings;
    private final Environment<Value> _parent;
    private final int _depth;

    public LinkedEnvironment(final Environment<Value> parent) {
        _parent = parent;
        _mappings = new HashMap<>();
        _depth = parent == null ? 0 : parent.getDepth() + 1;
    }

    public LinkedEnvironment() {
        this(null);
    }

    @Override
    public final Environment<Value> beginScope() {
        return new LinkedEnvironment<>(this);
    }

    @Override
    public final Environment<Value> endScope() {
        return _parent;
    }

    @Override
    public final int getDepth() {
        return _depth;
    }

    @Override
    public final void associate(String key, Value value) throws DuplicateKeyException {
        if(_mappings.containsKey(key))
            throw new DuplicateKeyException(key, _depth);
        _mappings.put(key, value);
    }

    @Override
    public Value find(String key) throws UnknownKeyException {
        Value foundValue = _mappings.get(key);
        if(foundValue == null && _parent != null)
            foundValue = _parent.find(key);
        if(foundValue == null)
            throw new UnknownKeyException(key, _depth);
        return foundValue;
    }
}
