package ast.binary;

import ast.ASTBinaryNode;
import ast.ASTNode;
import codeGeneration.JVMInstructionSet;
import codeGeneration.environment.Environment;

public final class ASTSubNode extends ASTBinaryNode {

    public ASTSubNode(final ASTNode leftNode, final ASTNode rightNode) {
        super(leftNode, rightNode, JVMInstructionSet.SUB);
    }

    @Override
    public final int evaluate(Environment<Integer> variableDeclarations) {
        return _lhs.evaluate(variableDeclarations) - _rhs.evaluate(variableDeclarations);
    }
}
