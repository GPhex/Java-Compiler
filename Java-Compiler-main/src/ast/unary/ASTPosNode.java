package ast.unary;

import IValue.IValue;
import ast.ASTNode;
import ast.ASTUnaryNode;
import codeGeneration.environment.Environment;

public final class ASTPosNode extends ASTUnaryNode {

    public ASTPosNode(final ASTNode child) {
        super(child);
    }

    @Override
    public final IValue evaluate(Environment<IValue> variableDeclarations) {
        return _child.evaluate(variableDeclarations);
    }
}
