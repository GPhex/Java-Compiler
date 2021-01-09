package ast.relational;

import ast.JasvaASTNode;
import util.JasvaInstructionSet;
import values.BoolValue;
import values.IntValue;

public final class ASTLessEqNode extends ASTComparisonNode {

    public ASTLessEqNode(final JasvaASTNode left, final JasvaASTNode right) {
        super(left, right, "less or equal", JasvaInstructionSet.CLE);
    }

    @Override
    protected final BoolValue compare(IntValue left, IntValue right) {
        return left.isLesserEqual(right);
    }

    @Override
    public String toString() {
        return "ASTLessEqNode";
    }
}
