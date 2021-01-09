package ast.relational;

import ast.JasvaASTNode;
import util.JasvaInstructionSet;
import values.BoolValue;
import values.IntValue;

public final class ASTEqualsNode extends ASTComparisonNode {

    public ASTEqualsNode(final JasvaASTNode left, final JasvaASTNode right) {
        super(left, right, "equality", JasvaInstructionSet.CEQ);
    }

    @Override
    protected final BoolValue compare(IntValue left, IntValue right) {
        return left.isEqual(right);
    }

    @Override
    public String toString() {
        return "ASTEqualsNode";
    }
}
