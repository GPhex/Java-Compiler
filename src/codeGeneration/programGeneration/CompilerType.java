package codeGeneration.programGeneration;

public interface CompilerType {

    String asLiteral();

    default boolean isPrimitive() { return false; }

    String asDescriptor();

    static CompilerType array(CompilerType arrayType) {
        return new CompilerType() {
            @Override
            public final String asLiteral() {
                return "[" + arrayType.asLiteral();
            }

            @Override
            public final String asDescriptor() {
                return "[" + arrayType.asDescriptor();
            }
        };
    }
}
