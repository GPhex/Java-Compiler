package compilation.fileGeneration;

import codeGeneration.programGeneration.*;
import compilation.JasminCompiler;
import util.JasvaInstructionSet;

import java.util.LinkedHashMap;
import java.util.Map;

public class JasminClass implements CompiledClass<JasminCompiler> {

    private static final String SLOT_NAME = "v%d";

    private final CompilerType _type;
    private final CompilerType _superType;
    protected final Map<String, Slot> _fields;
    private final Map<String, MethodBuilder<JasminCompiler>> _methods;
    private int _nextFieldIndex;

    public JasminClass(final CompilerType thisType, final CompilerType superType) {
        _fields = new LinkedHashMap<>();
        _methods = new LinkedHashMap<>();
        _nextFieldIndex = 0;
        _superType = superType;
        _type = thisType;
        addDefaultConstructor();
    }

    public JasminClass(final CompilerType type) {
        this(type, BaseTypes.OBJECT);
    }

    @Override
    public final CompilerType getType() {
        return _type;
    }

    @Override
    public final String addSlot(CompilerType slotType, Modifiers... modifiers) {
        final String slotName = String.format(SLOT_NAME, _nextFieldIndex++);
        addSlot(slotName, slotType, modifiers);
        return slotName;
    }

    @Override
    public final void addSlot(final String name, final CompilerType slotType, final Modifiers... modifiers) {
        _fields.put(name, constructSlot(name, slotType, modifiers));
    }

    @Override
    public final Slot getSlot(String slotName) {
        return _fields.get(slotName);
    }

    @Override
    public final JasminCompiler addConstructor(String constructorDescriptor, Modifiers... mods) {
        final MethodBuilder<JasminCompiler> constructor = constructor(constructorDescriptor, mods);
        if(constructor == null)
            return null;
        return constructor.getCompiler();
    }

    @Override
    public final MethodBuilder<JasminCompiler> addMethod(String methodName, CompilerType returnType, Modifiers... modifiers) {
        if(_methods.containsKey(methodName))
            return null;
        final MethodBuilder<JasminCompiler> method = new JasminMethod(this, methodName, false, returnType, modifiers);
        _methods.put(methodName, method);
        return method;
    }

    @Override
    public final CompiledMethod<JasminCompiler> getConstructor(String constructorDescriptor) {
        return _methods.get(String.format("<%s>", constructorDescriptor));
    }

    @Override
    public final CompiledMethod<JasminCompiler> getMethod(String methodName) {
        return _methods.get(methodName);
    }

    protected void dumpFields(CompilerWriter fileDumper) {
        _fields.forEach((n, s) -> fileDumper.line(".field %s %s %s", Modifiers.arrayToString(s.getModifiers()), n, s.getType().asDescriptor()));
    }

    @Override
    public void dump(CompilerWriter fileDumper) {
        fileDumper.line(String.format(".class public %s ", _type.asLiteral()));
        fileDumper.line(".super " + _superType.asLiteral());
        dumpFields(fileDumper);
        final MethodBuilder<JasminCompiler> defaultConstructor = _methods.remove("<init>");
        defaultConstructor.dump(fileDumper);
        _methods.forEach((name, method) -> method.dump(fileDumper));
    }

    private void addDefaultConstructor() {
        final MethodBuilder<JasminCompiler> constructor = constructor("init", Modifiers.PUBLIC);
        final JasminCompiler compiler = constructor.getCompiler();
        compiler.pushInstruction(JasvaInstructionSet.LDA.instance(0));
        compiler.pushInstruction(JasvaInstructionSet.IVK_SUPER.instance());
        constructor.markSpecial();
    }

    private MethodBuilder<JasminCompiler> constructor(final String constructorDescriptor, Modifiers... mods) {
        final String constructorName = String.format("<%s>", constructorDescriptor);
        if(_methods.containsKey(constructorName))
            return null;
        final MethodBuilder<JasminCompiler> method = new JasminMethod(this, constructorName, true, BaseTypes.VOID, mods);
        _methods.put(constructorName, method);
        return method;
    }

    private Slot constructSlot(final String slotName,
                                 final CompilerType slotType,
                                 final Modifiers... mods) {
        final String path = String.format("%s/%s", _type.asLiteral(), slotName);
        return new Slot() {
            @Override
            public final CompilerType getType() {
                return slotType;
            }

            @Override
            public final String getPath() {
                return path;
            }

            @Override
            public final Modifiers[] getModifiers() {
                return mods;
            }

            @Override
            public final boolean isStatic() {
                return false;
            }
        };
    }
}
