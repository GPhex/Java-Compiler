package ast.special;

import IValue.*;
import ast.ASTNode;
import codeGeneration.IntermediateCodeGenerator;
import codeGeneration.JVMInstructionSet;
import codeGeneration.environment.Environment;
import util.Pair;

public final class ASTNumNode implements ASTNode {

    private final int _number;

    public ASTNumNode(final int number) {
        _number = number;
    }

    @Override
    public IValue evaluate(Environment<IValue> variableDeclarations) {

                return new VInt(_number);
            }

    @Override
    public void compile(Environment<Pair<Integer, String>> variableLinker, IntermediateCodeGenerator builder) {
        builder.pushInstruction(JVMInstructionSet.PUSH_INT, _number);
    }
}
