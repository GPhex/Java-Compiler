package util;

import java.io.PrintWriter;

public final class JasminFileHelper {

    public static void classPreamble(final PrintWriter writer, final String className, final Type superType) {
        writer.println(String.format(".class public %s", className));
        writer.println(String.format(".super %s", superType.getName()));
    }

    public static void classPreamble(final PrintWriter writer, final Type classType, final Type superType) {
        classPreamble(writer, classType.getName(), superType);
    }

    public static void defaultConstructor(final PrintWriter writer, final Type superType) {
        writer.println(".method public <init>()V");
        writeIndent(writer, "aload_0");
        writeIndent(writer, String.format("invokespecial %s/<init>()V", superType.getName()));
        writeIndent(writer, "return");
        writer.println(".end method");
    }

    public static void writeIndent(final PrintWriter writer, final String instruction) {
        writer.println(String.format("\t%s", instruction));
    }
}
