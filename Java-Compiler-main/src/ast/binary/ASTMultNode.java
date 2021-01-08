package ast.binary;

import IValue.*;
import ast.ASTBinaryNode;
import ast.ASTNode;
import codeGeneration.JVMInstructionSet;
import codeGeneration.environment.Environment;

public final class ASTMultNode extends ASTBinaryNode {

    public ASTMultNode(final ASTNode leftNode, final ASTNode rightNode) {
        super(leftNode, rightNode, JVMInstructionSet.MUL);
    }

    @Override
    public final IValue evaluate(Environment<IValue> variableDeclarations) {
        IValue v1 = _lhs.evaluate(variableDeclarations);
        if (v1 instanceof VInt) {
            IValue v2 = _rhs.evaluate(variableDeclarations);
            if (v2 instanceof VInt) {
                return new VInt(((VInt) v1).getval() * ((VInt) v2).getval());
            }
        }

        throw new ArgumentIsNotAnValueError("integer");
    }
}