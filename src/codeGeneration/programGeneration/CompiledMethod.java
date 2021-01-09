package codeGeneration.programGeneration;

import codeGeneration.Compiler;
import compilation.Frame;

public interface CompiledMethod<C extends Compiler<?, ?, ?>> {

    C getCompiler();

    boolean isConstructor();

    boolean isStatic();

    boolean isSpecial();

    Frame getCurrentFrame();

    Frame addFrame();

    Frame popFrame();

}
