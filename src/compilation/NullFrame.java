package compilation;

import codeGeneration.programGeneration.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public final class NullFrame implements Frame {

    public NullFrame() {

    }

    @Override
    public final CompilerType getType() {
        return BaseTypes.OBJECT;
    }

    @Override
    public void addSlot(String slotName, CompilerType slotType, Modifiers... modifiers) {
        throw new NotImplementedException();
    }

    @Override
    public final String addSlot(CompilerType slotType, Modifiers... modifiers) {
        throw new NotImplementedException();
    }

    @Override
    public final Slot getSlot(String slotName) {
        throw new NotImplementedException();
    }

    @Override
    public final JasminCompiler addConstructor(String constructorDescriptor, Modifiers... mods) {
        throw new NotImplementedException();
    }

    @Override
    public final MethodBuilder<JasminCompiler> addMethod(String methodName, CompilerType returnType, Modifiers... modifiers) {
        throw new NotImplementedException();
    }

    @Override
    public final CompiledMethod<JasminCompiler> getConstructor(String constructorDescriptor) {
        throw new NotImplementedException();
    }

    @Override
    public final CompiledMethod<JasminCompiler> getMethod(String methodName) {
        throw new NotImplementedException();
    }

    @Override
    public final void dump(CompilerWriter fileDumper) {
        throw new NotImplementedException();
    }

    @Override
    public final Frame getParent() {
        throw new NotImplementedException();
    }
}
