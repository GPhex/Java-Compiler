package codeGeneration.programGeneration;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Modifiers {

    PUBLIC,
    STATIC;

    @Override
    public String toString() {
        return name().toLowerCase();
    }

    public static String arrayToString(final Modifiers... mods) {
        return Arrays.stream(mods)
                .map(Modifiers::toString)
                .collect(Collectors.joining(" "));
    }
}
