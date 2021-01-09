package types;

import codeGeneration.programGeneration.BaseTypes;
import codeGeneration.programGeneration.CompiledClass;
import codeGeneration.programGeneration.CompilerType;
import codeGeneration.programGeneration.Modifiers;
import compilation.JasminCompiler;
import compilation.fileGeneration.CachedProgramGenerator;

public final class RefType implements Type {

    static final CompilerType REFERENCE = new CompilerType() {
        @Override
        public final String asLiteral() {
            return "ref_ref";
        }

        @Override
        public String asDescriptor() {
            return "Lref_ref;";
        }
    };

    private final Type _refType;

    public RefType(final Type refType) {
        _refType = refType;
    }

    public Type getRefType() {
        return _refType;
    }

    @Override
    public final CompilerType getCompilerType() {
        return _refType instanceof IntType ? IntType.REFERENCE : _refType instanceof BoolType ? BoolType.REFERENCE : REFERENCE;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof RefType)) return false;
        return ((RefType) o).getRefType().equals(_refType);
    }

    @Override
    public String toString() {
        return "reference to " + _refType.toString();
    }
}
