package ast.binary;

import IValue.*;
import ast.ASTBinaryNode;
import ast.ASTNode;
import codeGeneration.IntermediateCodeGenerator;
import codeGeneration.JVMInstructionSet;
import codeGeneration.environment.Environment;
import util.Pair;

public final class ASTAssign extends ASTBinaryNode {
    public ASTAssign(final ASTNode leftNode, final ASTNode rightNode) {
        super(leftNode, rightNode, JVMInstructionSet.ADD);
    }


    @Override
    public final IValue evaluate(Environment<IValue> variableDeclarations) {
        IValue v1 = _lhs.evaluate(variableDeclarations);
        if (v1 instanceof VMCell) {
            IValue v2 = _rhs.evaluate(variableDeclarations);
            ((VMCell) v1).set(v2);
            return v2;
        }
        throw new ArgumentIsNotAnValueError("integer");
    }

}
