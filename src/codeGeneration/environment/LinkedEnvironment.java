package codeGeneration.environment;

import java.util.HashMap;
import java.util.Map;

public final class LinkedEnvironment<Storage> implements Environment<Storage> {

    private final Environment<Storage> _parent;
    private final Map<String, Storage> _associations;
    private final int _depth;

    public LinkedEnvironment(final Environment<Storage> parent) {
        _parent = parent;
        _associations = new HashMap<>();
        _depth = _parent == null ? 0 : _parent.depth() + 1;
    }

    @Override
    public final Environment<Storage> beginScope() {
        return new LinkedEnvironment<>(this);
    }

    @Override
    public final Environment<Storage> endScope() {
        return _parent;
    }

    @Override
    public final int depth() {
        return _depth;
    }

    @Override
    public final void associate(String id, Storage value) throws DuplicateIdentifierException {
        if(_associations.containsKey(id))
            throw new DuplicateIdentifierException(id, _depth);
        _associations.put(id, value);
    }

    @Override
    public Storage find(String id) throws UnboundIdentifierException {
        Storage value = _associations.get(id);
        if(value == null && _parent != null)
            value = _parent.find(id);
        if(value == null)
            throw new UnboundIdentifierException(id, _depth);
        return value;
    }
}
