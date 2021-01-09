package compilation;

import codeGeneration.FormattedInstruction;
import codeGeneration.programGeneration.CompilerWriter;
import codeGeneration.programGeneration.Dumpable;
import compilation.fileGeneration.JasminMethod;
import util.JasvaInstructionSet;

import java.util.LinkedList;
import java.util.List;

public final class StackTrackingCompiler implements JasminCompiler, Dumpable {

    private static final String LABEL_FORMAT = "L%d";

    private final JasminMethod relatedMethod;

    private final List<ByteCode> _codeStack;
    private final int _staticLinker;
    private int _maxStackSize;
    private int _curStackSize;
    private int _nextLabel;

    public StackTrackingCompiler(final JasminMethod method,
                                 final int staticLinkerLocation) {
        relatedMethod = method;
        _staticLinker = staticLinkerLocation;
        _codeStack = new LinkedList<>();
        _maxStackSize = _curStackSize = 0;
        _nextLabel = 1;
    }

    @Override
    public final void pushInstruction(FormattedInstruction<String, JasvaInstructionSet> instruction) {
        final String inst = instruction.getTransformedType();
        final int stackImpact = instruction.getUpdatedStackImpact();
        pushRaw(inst, stackImpact);
    }

    @Override
    public String pushLabel() {
        final String label = genLabel();
        pushLabel(label);
        return label;
    }

    @Override
    public final String genLabel() {
        return String.format(LABEL_FORMAT, _nextLabel++);
    }

    @Override
    public void pushLabel(String label) {
        _codeStack.add(new ByteCode(label, false));
    }

    @Override
    public final int getEvaluationStackMaxSize() {
        return _maxStackSize;
    }

    @Override
    public final void startStaticLinker() {
        pushInstruction(JasvaInstructionSet.NUL.instance());
        pushInstruction(JasvaInstructionSet.STA.instance(_staticLinker));
        _maxStackSize = 1;
    }

    @Override
    public final Frame beginScope() {
        final Frame newScope = relatedMethod.addFrame();
        pushInstruction(JasvaInstructionSet.NEW.instance(newScope));
        pushInstruction(JasvaInstructionSet.DUP.instance());
        pushInstruction(JasvaInstructionSet.IVK.instance(newScope.getConstructor("init")));
        pushInstruction(JasvaInstructionSet.DUP.instance());
        pushInstruction(JasvaInstructionSet.LDA.instance(_staticLinker));
        pushInstruction(JasvaInstructionSet.PUT.instance(newScope.getSlot(Frame.SL_SLOT)));
        pushInstruction(JasvaInstructionSet.STA.instance(_staticLinker));
        return newScope;
    }

    @Override
    public final Frame loadFrame() {
        final Frame curFrame = relatedMethod.getCurrentFrame();
        pushInstruction(JasvaInstructionSet.LDA.instance(_staticLinker));
        return curFrame;
    }

    @Override
    public final Frame endScope() {
        final Frame prevScope = relatedMethod.popFrame();
        pushInstruction(JasvaInstructionSet.LDA.instance(_staticLinker));
        pushInstruction(JasvaInstructionSet.GET.instance(prevScope.getSlot(Frame.SL_SLOT)));
        pushInstruction(JasvaInstructionSet.STA.instance(_staticLinker));
        return prevScope;
    }

    @Override
    public final void printValue(boolean hasValue, boolean changeLine) {
        pushRaw("getstatic java/lang/System/out Ljava/io/PrintStream;", 1);
        if(hasValue)
            pushInstruction(JasvaInstructionSet.SWP.instance());
        pushRaw(String.format("invokevirtual java/io/PrintStream/print%s(%s)V", changeLine ? "ln" : "", hasValue ? "I" : ""),-2);
    }

    @Override
    public final void dump(CompilerWriter fileDumper) {
        pushInstruction(JasvaInstructionSet.RET.instance());
        _codeStack.forEach(bc -> {
            if (bc.isCode)
                fileDumper.code(bc.value);
            else
                fileDumper.label(bc.value);
        });
    }

    private void pushRaw(final String inst, final int stackImpact) {
        _curStackSize += stackImpact;
        _maxStackSize = Math.max(_maxStackSize, _curStackSize);
        final ByteCode code = new ByteCode(inst, true);
        _codeStack.add(code);
    }

    private static class ByteCode {

        final boolean isCode;
        final String value;

        ByteCode(final String str, final boolean isCode) {
            value = str;
            this.isCode = isCode;
        }

    }
}
