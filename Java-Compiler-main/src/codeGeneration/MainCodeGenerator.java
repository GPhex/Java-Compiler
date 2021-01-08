package codeGeneration;

import util.ConstantTypes;
import util.JasminFileHelper;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

public final class MainCodeGenerator implements IntermediateCodeGenerator {

    private static final String FRAME_NAME = "frame_%d";

    private final String _mainClassName;
    private final List<String> _codeStack;
    private int _maxStackSize;
    private int _curStackSize;

    private final List<Frame> _frameColl;
    private final int _lvaIndexFrames;
    private Frame _topFrame;

    public MainCodeGenerator(final String mainName) {
        _mainClassName = mainName;
        _codeStack = new LinkedList<>();
        _frameColl = new LinkedList<>();
        _lvaIndexFrames = 1;
        _topFrame = null;

        _codeStack.add("getstatic java/lang/System/out Ljava/io/PrintStream;");
        _maxStackSize = _curStackSize = 1;
    }

    @Override
    public final void pushInstruction(Instruction instruction, Object... args) {
        final String instString = instruction.getFormattedInstruction(args);
        _curStackSize += instruction.getStackImpact();
        _maxStackSize = Math.max(_maxStackSize, _curStackSize);
        _codeStack.add(instString);
    }

    @Override
    public final void printTopStack() {
        _codeStack.add("invokestatic java/lang/String/valueOf(I)Ljava/lang/String;");
        _codeStack.add("invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V");
    }

    @Override
    public final Frame createFrame() {
        if(_frameColl.isEmpty()) initFrameStack();
        final Frame frame = new LinkedFrame(String.format(FRAME_NAME, _frameColl.size()), _topFrame);
        pushFrameInstructions(frame);
        _topFrame = frame;
        _frameColl.add(frame);
        return frame;
    }

    @Override
    public final Frame loadFrame() {
        pushInstruction(JVMInstructionSet.LVA_LOAD, _lvaIndexFrames);
        return _topFrame;
    }

    @Override
    public final void popFrame() {
        popFrameInstructions(_topFrame);
        _topFrame = _topFrame.getParent();
    }

    @Override
    public final String[] dumpClass() throws IOException {
        pushInstruction(JVMInstructionSet.RET);
        final String fileName = _mainClassName + ".j";
        final PrintWriter fileWriter = new PrintWriter(fileName);
        JasminFileHelper.classPreamble(fileWriter, _mainClassName, ConstantTypes.OBJECT);
        JasminFileHelper.defaultConstructor(fileWriter, ConstantTypes.OBJECT);
        fileWriter.println(".method public static main([Ljava/lang/String;)V");
        JasminFileHelper.writeIndent(fileWriter, String.format(".limit locals %d", _frameColl.size() == 0 ? 1 : 2));
        JasminFileHelper.writeIndent(fileWriter, String.format(".limit stack %d", _maxStackSize));
        _codeStack.forEach(instruction -> JasminFileHelper.writeIndent(fileWriter, instruction));
        fileWriter.println(".end method");
        fileWriter.flush();
        fileWriter.close();
        final List<String> fileNames = new LinkedList<>();
        fileNames.add(fileName);
        for(Frame f : _frameColl)
            fileNames.add(f.dumpClass()[0]);
        return fileNames.toArray(new String[0]);
    }

    private void initFrameStack() {
        _codeStack.add(1, JVMInstructionSet.NULL_CONST.getFormattedInstruction());
        _codeStack.add(2, JVMInstructionSet.LVA_STORE.getFormattedInstruction(_lvaIndexFrames));
        _maxStackSize = Math.max(_maxStackSize, 1);
    }

    private void pushFrameInstructions(final Frame frame) {
        pushInstruction(JVMInstructionSet.OBJECT_NEW, frame.getType().getName());
        pushInstruction(JVMInstructionSet.DUP);
        pushInstruction(JVMInstructionSet.DEFAULT_CONSTRUCTOR, frame.getType().getName());
        pushInstruction(JVMInstructionSet.DUP);
        pushInstruction(JVMInstructionSet.LVA_LOAD, _lvaIndexFrames);
        pushInstruction(JVMInstructionSet.FIELD_PUT, frame.getSlot(Frame.PARENT_SLOT));
        pushInstruction(JVMInstructionSet.LVA_STORE, _lvaIndexFrames);
    }

    private void popFrameInstructions(final Frame frame) {
        pushInstruction(JVMInstructionSet.LVA_LOAD, _lvaIndexFrames);
        pushInstruction(JVMInstructionSet.FIELD_GET, frame.getSlot(Frame.PARENT_SLOT));
        pushInstruction(JVMInstructionSet.LVA_STORE, _lvaIndexFrames);
    }
}
