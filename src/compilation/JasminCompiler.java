package compilation;

import codeGeneration.Compiler;
import types.Type;
import util.JasvaInstructionSet;

public interface JasminCompiler extends Compiler<String, Frame, JasvaInstructionSet> {

    void startStaticLinker();

    Frame loadFrame();

    void printValue(final boolean hasValue,
                    final boolean changeLine);

}
