package ast.relational;

import ast.JasvaASTNode;
import util.JasvaInstructionSet;
import values.BoolValue;
import values.IntValue;

public final class ASTGreatEqNode extends ASTComparisonNode {

    public ASTGreatEqNode(final JasvaASTNode left, final JasvaASTNode right) {
        super(left, right, "greater or equals", JasvaInstructionSet.CGE);
    }

    @Override
    protected final BoolValue compare(IntValue left, IntValue right) {
        return left.isGreaterEqual(right);
    }

    @Override
    public String toString() {
        return "ASTGreatEqNode";
    }
}
