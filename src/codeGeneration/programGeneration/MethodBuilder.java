package codeGeneration.programGeneration;

import codeGeneration.Compiler;

public interface MethodBuilder<C extends Compiler<?, ?, ?>> extends CompiledMethod<C>, Dumpable {

    void addParam(final String paramName,
                  final CompilerType paramType);

    void markSpecial();

}
