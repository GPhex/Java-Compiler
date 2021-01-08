package ast.binary;

import IValue.*;
import ast.ASTBinaryNode;
import ast.ASTNode;
import codeGeneration.JVMInstructionSet;
import codeGeneration.environment.Environment;

public class ASTOr extends ASTBinaryNode {
    public ASTOr(final ASTNode leftNode, final ASTNode rightNode) {
        super(leftNode, rightNode, JVMInstructionSet.ADD);
    }

    @Override
    public final IValue evaluate(Environment<IValue> variableDeclarations) {
        IValue v1 = _lhs.evaluate(variableDeclarations);
        if (v1 instanceof VBool) {
            IValue v2 = _rhs.evaluate(variableDeclarations);
            if (v2 instanceof VBool) {
                return new VBool(((VBool) v1).getval() || ((VBool) v2).getval());
            }
        }

        throw new ArgumentIsNotAnValueError("boolean");
    }
}
