package codeGeneration.programGeneration;

import java.io.IOException;
import java.io.PrintWriter;

public final class CompilerWriter {

    private final PrintWriter _writer;

    public CompilerWriter(final String fileName) throws IOException {
        _writer = new PrintWriter(fileName);
    }

    public void comment(final String comment) {
        _writer.println("; " + comment);
    }

    public void line(final String line) {
        _writer.println(line);
    }

    public void line(final String line, final Object... args) {
        line(String.format(line, args));
    }

    public void code(final String byteCode) {
        line("\t" + byteCode);
    }

    public void code(final String byteCode, final Object... args) {
        code(String.format(byteCode, args));
    }

    public void label(final String label) {
        line(label + ":");
    }

    public void nextLine() {
        line("");
    }

    public void close() {
        _writer.flush();
        _writer.close();
    }
}
