package ast.binary;

import ast.ASTBinaryNode;
import ast.ASTNode;
import codeGeneration.JVMInstructionSet;
import codeGeneration.environment.Environment;

public final class ASTAddNode extends ASTBinaryNode {

    public ASTAddNode(final ASTNode leftNode, final ASTNode rightNode) {
        super(leftNode, rightNode, JVMInstructionSet.ADD);
    }

    @Override
    public final int evaluate(Environment<Integer> variableDeclarations) {
        return _lhs.evaluate(variableDeclarations) + _rhs.evaluate(variableDeclarations);
    }
}
