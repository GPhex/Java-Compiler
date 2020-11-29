package ast.unary;

import ast.ASTNode;
import ast.ASTUnaryNode;
import codeGeneration.JVMInstructionSet;
import codeGeneration.environment.Environment;

public final class ASTNegNode extends ASTUnaryNode {

    public ASTNegNode(final ASTNode child) {
        super(child, JVMInstructionSet.NEG);
    }

    @Override
    public final int evaluate(Environment<Integer> variableDeclarations) {
        return -_child.evaluate(variableDeclarations);
    }
}
