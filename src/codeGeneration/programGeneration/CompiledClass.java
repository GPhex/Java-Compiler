package codeGeneration.programGeneration;

import codeGeneration.Compiler;

public interface CompiledClass<C extends Compiler<?, ?, ?>> extends Dumpable {

    CompilerType getType();

    void addSlot(final String slotName,
                 final CompilerType slotType,
                 final Modifiers... modifiers);

    String addSlot(final CompilerType slotType,
                   final Modifiers... modifiers);

    Slot getSlot(final String slotName);

    C addConstructor(final String constructorDescriptor,
                     final Modifiers... mods);

    MethodBuilder<C> addMethod(final String methodName,
                               final CompilerType returnType,
                               final Modifiers... modifiers);

    CompiledMethod<C> getConstructor(final String constructorDescriptor);

    CompiledMethod<C> getMethod(final String methodName);

}
