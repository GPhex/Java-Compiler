package codeGeneration;

import util.FileGenerator;

public interface IntermediateCodeGenerator extends FileGenerator {

    void pushInstruction(final Instruction instruction, final Object... args);

    void printTopStack();

    Frame createFrame();

    Frame loadFrame();

    void popFrame();

}
