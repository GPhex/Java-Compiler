package ast;

import codeGeneration.Instruction;
import codeGeneration.IntermediateCodeGenerator;
import codeGeneration.environment.Environment;
import util.Pair;

public abstract class ASTUnaryNode implements ASTNode {

    protected final ASTNode _child;
    private final Instruction _jvmInstruction;

    public ASTUnaryNode(final ASTNode child, final Instruction jvmInstruction) {
        _child = child;
        _jvmInstruction = jvmInstruction;
    }

    public ASTUnaryNode(final ASTNode child) {
        this(child, null);
    }

    @Override
    public void compile(Environment<Pair<Integer, String>> variableLinker, IntermediateCodeGenerator generator) {
        _child.compile(variableLinker, generator);
        if(_jvmInstruction != null) generator.pushInstruction(_jvmInstruction);
    }
}
