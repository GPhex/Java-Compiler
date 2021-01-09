package codeGeneration.programGeneration;

public interface Slot {

    CompilerType getType();

    String getPath();

    Modifiers[] getModifiers();

    boolean isStatic();
}
