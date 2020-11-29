package ast.unary;

import ast.ASTNode;
import ast.ASTUnaryNode;
import codeGeneration.environment.Environment;

public final class ASTPosNode extends ASTUnaryNode {

    public ASTPosNode(final ASTNode child) {
        super(child);
    }

    @Override
    public final int evaluate(Environment<Integer> variableDeclarations) {
        return _child.evaluate(variableDeclarations);
    }
}
