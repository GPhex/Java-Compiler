package codeGeneration;

import java.util.function.Predicate;

public interface Instruction<Alias, IS extends Enum<IS> & Instruction<Alias, IS>> {

    FormattedInstruction<Alias, IS> instance(final Object... args);

    Alias getCompilerFormattingAlias();

    int getEvaluationStackImpact();

}
