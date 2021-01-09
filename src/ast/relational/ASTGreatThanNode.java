package ast.relational;

import ast.JasvaASTNode;
import util.JasvaInstructionSet;
import values.BoolValue;
import values.IntValue;

public final class ASTGreatThanNode extends ASTComparisonNode {

    public ASTGreatThanNode(final JasvaASTNode left, final JasvaASTNode right) {
        super(left, right, "greater than", JasvaInstructionSet.CGT);
    }

    @Override
    protected final BoolValue compare(IntValue left, IntValue right) {
        return left.isGreaterThan(right);
    }

    @Override
    public String toString() {
        return "ASTGreatThanNode";
    }
}
