package compilation.fileGeneration;

import codeGeneration.programGeneration.*;
import compilation.Frame;
import compilation.JasminCompiler;
import compilation.LinkedFrame;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class CachedProgramGenerator implements ProgramGenerator<JasminCompiler> {

    private static final String FRAME_FORMAT= "frame_%d";

    private static CachedProgramGenerator INSTANCE = null;

    private final Map<String, CompiledClass<JasminCompiler>> _generatable;
    private final String _extension;
    private int _frameIndex;

    public static CachedProgramGenerator getInstance() {
        return INSTANCE;
    }

    public CachedProgramGenerator(final String mainFileName, final String extension) {
        _generatable = new HashMap<>();
        _extension = extension;
        addClass(mainFileName, BaseTypes.OBJECT);
        _frameIndex = 0;
        INSTANCE = this;
    }

    @Override
    public final CompiledClass<JasminCompiler> addClass(String className, CompilerType superType) {
        return addClass(new CompilerType() {
            @Override
            public final String asLiteral() {
                return className;
            }

            @Override
            public final String asDescriptor() {
                return "L" + className + ";";
            }
        }, superType);
    }

    @Override
    public final CompiledClass<JasminCompiler> addClass(CompilerType type, CompilerType superType) {
        final CompiledClass<JasminCompiler> cls = new JasminClass(type, superType);
        _generatable.put(type.asLiteral(), cls);
        return cls;
    }

    @Override
    public final CompiledClass<JasminCompiler> getClass(CompilerType type) {
        return getClass(type.asLiteral());
    }

    @Override
    public final CompiledClass<JasminCompiler> getClass(String className) {
        return _generatable.get(className);
    }

    @Override
    public final void generateFiles() throws IOException {
        for(Map.Entry<String, CompiledClass<JasminCompiler>> entry : _generatable.entrySet()) {
            final String fileName = String.format("%s.%s", entry.getKey(), _extension);
            System.out.printf("Generating %s ...\n", fileName);
            CompilerWriter writer = new CompilerWriter(fileName);
            entry.getValue().dump(writer);
            writer.close();
        }
        System.out.println("Program generated.");
    }

    public final Frame newFrame(final Frame parent) {
        final String name = String.format(FRAME_FORMAT, _frameIndex++);
        final Frame frame = new LinkedFrame(name, parent);
        _generatable.put(name, frame);
        return frame;
    }
}
