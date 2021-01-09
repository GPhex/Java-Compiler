package types;

import codeGeneration.programGeneration.BaseTypes;
import codeGeneration.programGeneration.CompilerType;

public final class IntType implements Type {

    static final CompilerType REFERENCE = new CompilerType() {
        @Override
        public final String asLiteral() {
            return "ref_int";
        }

        @Override
        public String asDescriptor() {
            return "Lref_int;";
        }
    };

    public IntType() {

    }

    @Override
    public final CompilerType getCompilerType() {
        return BaseTypes.INTEGER;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof IntType;
    }

    @Override
    public String toString() {
        return "integer";
    }
}
