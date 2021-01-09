package compilation;

import codeGeneration.programGeneration.BaseTypes;
import codeGeneration.programGeneration.CompilerType;
import codeGeneration.programGeneration.CompilerWriter;
import codeGeneration.programGeneration.Modifiers;
import compilation.fileGeneration.JasminClass;

public final class LinkedFrame extends JasminClass implements Frame {

    private final Frame _parent;

    public LinkedFrame(final String className,
                       final Frame parentFrame) {
        super(new CompilerType() {
            @Override
            public final String asLiteral() {
                return className;
            }

            @Override
            public final String asDescriptor() {
                return "L" + className + ";";
            }
        });
        _parent = parentFrame;
        addSlot(SL_SLOT, parentFrame.getType(), Modifiers.PUBLIC);
    }

    @Override
    protected void dumpFields(CompilerWriter fileDumper) {
        _fields.forEach((name, slot) -> {
            final CompilerType type = slot.getType().isPrimitive() ? slot.getType() : BaseTypes.OBJECT;
            fileDumper.line(".field %s %s %s", Modifiers.arrayToString(slot.getModifiers()), name, type.asDescriptor());
        });
    }

    @Override
    public final Frame getParent() {
        return _parent;
    }
}
