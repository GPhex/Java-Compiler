package codeGeneration.programGeneration;

import codeGeneration.Compiler;

import java.io.IOException;

public interface ProgramGenerator<C extends Compiler<?, ?, ?>> {

    CompiledClass<C> addClass(final String className,
                              final CompilerType superType);

    CompiledClass<C> getClass(final String className);

    CompiledClass<C> addClass(final CompilerType type,
                              final CompilerType superType);

    CompiledClass<C> getClass(final CompilerType type);

    void generateFiles() throws IOException;

}
