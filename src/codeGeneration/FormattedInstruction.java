package codeGeneration;

public interface FormattedInstruction<Alias, Reference extends Enum<Reference> & Instruction<Alias, Reference>> {

    Reference getReference();

    default Alias getTransformedType() { return getReference().getCompilerFormattingAlias(); }

    default int getUpdatedStackImpact() { return getReference().getEvaluationStackImpact(); }

}
