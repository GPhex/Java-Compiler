package compilation.fileGeneration;

import codeGeneration.programGeneration.*;
import compilation.Frame;
import compilation.JasminCompiler;
import compilation.NullFrame;
import compilation.StackTrackingCompiler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class JasminMethod implements MethodBuilder<JasminCompiler> {

    private final CompiledClass<JasminCompiler> cls;
    private final boolean _constructor;
    private final String _name;
    private final CompilerType _returnType;
    private final Modifiers[] _modifiers;
    private boolean _isStatic, _isSpecial;
    private final Map<String, CompilerType> _params;
    private int _nextLVASpot;
    private JasminCompiler _compiler;
    private Frame _topFrame;

    public JasminMethod(CompiledClass<JasminCompiler> cls, String methodName, boolean isConstructor, CompilerType returnType, Modifiers... modifiers) {
        this.cls = cls;
        _constructor = isConstructor;
        _name = methodName;
        _returnType = returnType;
        _modifiers = modifiers;
        _params = new LinkedHashMap<>();
        _nextLVASpot = 1;
        _isStatic = false;
        _isSpecial = false;
        _compiler = null;
        for(Modifiers mod : modifiers) {
            if(mod == Modifiers.STATIC) {
                _nextLVASpot = 0;
                _isStatic = true;
                break;
            }
        }
        _topFrame = new NullFrame();
    }

    @Override
    public final void addParam(String paramName, CompilerType paramType) {
        if(!_params.containsKey(paramName)) {
            _params.put(paramName, paramType);
            _nextLVASpot++;
        }
    }

    @Override
    public final boolean isConstructor() {
        return _constructor;
    }

    @Override
    public final void markSpecial() {
        _isSpecial = true;
    }

    @Override
    public final boolean isStatic() {
        return _isStatic;
    }

    @Override
    public final boolean isSpecial() {
        return _isSpecial;
    }

    @Override
    public final Frame getCurrentFrame() {
        return _topFrame;
    }

    @Override
    public final Frame addFrame() {
        _topFrame = CachedProgramGenerator.getInstance().newFrame(_topFrame);
        return _topFrame;
    }

    @Override
    public final Frame popFrame() {
        Frame curTop = _topFrame;
        _topFrame = _topFrame.getParent();
        return curTop;
    }

    @Override
    public final JasminCompiler getCompiler() {
        if(_isSpecial)
            return null;
        if(_compiler == null)
            _compiler = new StackTrackingCompiler(this, _nextLVASpot++);
        return _compiler;
    }

    @Override
    public final void dump(CompilerWriter fileDumper) {
        fileDumper.line(".method %s %s", Modifiers.arrayToString(_modifiers), signature());
        if(!isConstructor()) {
            fileDumper.code(".limit locals %d", _nextLVASpot);
            fileDumper.code(".limit stack %d", _compiler.getEvaluationStackMaxSize());
        }
        _compiler.dump(fileDumper);
        fileDumper.line(".end method");
    }

    @Override
    public String toString() {
        return String.format("%s/%s", cls.getType().asLiteral(), signature());
    }

    private String signature() {
        return String.format("%s(%s)%s", _name,
                _params.values()
                        .stream()
                        .map(CompilerType::asDescriptor)
                        .collect(Collectors.joining(", ")),
                _returnType.asDescriptor());
    }
}
