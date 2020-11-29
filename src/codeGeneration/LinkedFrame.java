package codeGeneration;

import util.ConstantTypes;
import util.JasminFileHelper;
import util.Type;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

final class LinkedFrame implements Frame {

    private static final String SLOT_NAME = "v%d";

    private final Type _frameType;

    private final Frame _parent;
    private final Map<String, Slot> _fields;
    private int _nextSlotIndex;

    LinkedFrame(final String frameName,
                final Frame parent) {
        _parent = parent;
        _fields = new HashMap<>();
        _nextSlotIndex = 0;
        _frameType = new Type() {
            @Override
            public final String getName() {
                return frameName;
            }

            @Override
            public final String getLiteral() {
                return String.format("L%s;", frameName);
            }
        };
        final Type parentType = _parent == null ? ConstantTypes.OBJECT : _parent.getType();
        _fields.put(Frame.PARENT_SLOT, constructSlot(Frame.PARENT_SLOT, parentType));
    }

    @Override
    public final Type getType() {
        return _frameType;
    }

    @Override
    public final Frame getParent() {
        return _parent;
    }

    @Override
    public final String addSlot(Type type) {
        final String slotName = String.format(SLOT_NAME, _nextSlotIndex++);
        _fields.put(slotName, constructSlot(slotName, type));
        return slotName;
    }

    @Override
    public final Slot getSlot(String slotName) {
        return _fields.get(slotName);
    }

    @Override
    public final String[] dumpClass() throws IOException {
        final String fileName = _frameType.getName() + ".j";
        final PrintWriter fileWriter = new PrintWriter(fileName);
        JasminFileHelper.classPreamble(fileWriter, _frameType, ConstantTypes.OBJECT);
        for(Map.Entry<String, Slot> entry : _fields.entrySet())
            fileWriter.println(String.format(".field public %s %s", entry.getKey(), entry.getValue().getType().getLiteral()));
        JasminFileHelper.defaultConstructor(fileWriter, ConstantTypes.OBJECT);
        fileWriter.flush();
        fileWriter.close();
        return new String[]{fileName};
    }

    private Slot constructSlot(final String name, final Type type) {
        final String path = String.format("%s/%s", _frameType.getName(), name);
        return new Slot() {
            @Override
            public final String getPath() {
                return path;
            }

            @Override
            public final Type getType() {
                return type;
            }

            @Override
            public String toString() {
                return String.format("%s %s", path, type.getLiteral());
            }
        };
    }
}
