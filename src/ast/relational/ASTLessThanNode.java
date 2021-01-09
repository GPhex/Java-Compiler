package ast.relational;

import ast.JasvaASTNode;
import util.JasvaInstructionSet;
import values.BoolValue;
import values.IntValue;

public final class ASTLessThanNode extends ASTComparisonNode {

    public ASTLessThanNode(final JasvaASTNode left, final JasvaASTNode right) {
        super(left, right, "less than", JasvaInstructionSet.CLT);
    }

    @Override
    protected final BoolValue compare(IntValue left, IntValue right) {
        return left.isLesserThan(right);
    }

    @Override
    public String toString() {
        return "ASTLessThanNode";
    }
}
