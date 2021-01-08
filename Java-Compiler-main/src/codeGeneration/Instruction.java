package codeGeneration;

public interface Instruction {

    String getFormattedInstruction(final Object... args);

    int getStackImpact();

}
