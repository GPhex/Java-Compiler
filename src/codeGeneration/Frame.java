package codeGeneration;

import util.FileGenerator;
import util.Type;

public interface Frame extends FileGenerator {

    String PARENT_SLOT = "sl";

    Type getType();

    Frame getParent();

    String addSlot(final Type type);

    Slot getSlot(final String slotName);

    interface Slot {

        String getPath();

        Type getType();

    }
}
