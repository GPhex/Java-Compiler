package codeGeneration;

import codeGeneration.programGeneration.Dumpable;

public interface Compiler<Alias, Scope, IS extends Enum<IS> & Instruction<Alias, IS>> extends Dumpable {

    void pushInstruction(final FormattedInstruction<Alias, IS> instruction);

    String pushLabel();

    String genLabel();

    void pushLabel(final String label);

    int getEvaluationStackMaxSize();

    Scope beginScope();

    Scope endScope();
}
