package ast;

import codeGeneration.Instruction;
import codeGeneration.IntermediateCodeGenerator;
import codeGeneration.environment.Environment;
import util.Pair;

public abstract class ASTBinaryNode implements ASTNode {

    protected final ASTNode _lhs, _rhs;
    private final Instruction _jvmInstruction;

    public ASTBinaryNode(final ASTNode lhs, final ASTNode rhs, final Instruction jvmInstruction) {
        _lhs = lhs;
        _rhs = rhs;
        _jvmInstruction = jvmInstruction;
    }

    @Override
    public final void compile(Environment<Pair<Integer, String>> variableLinker, IntermediateCodeGenerator generator) {
        _lhs.compile(variableLinker, generator);
        _rhs.compile(variableLinker, generator);
        generator.pushInstruction(_jvmInstruction);
    }
}
