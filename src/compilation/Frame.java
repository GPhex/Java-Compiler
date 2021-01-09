package compilation;

import codeGeneration.programGeneration.CompiledClass;

public interface Frame extends CompiledClass<JasminCompiler> {

    String SL_SLOT = "sl";

    Frame getParent();

}
