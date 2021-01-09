package ast.relational;

import ast.JasvaASTNode;
import codeGeneration.environment.Environment;
import codeGeneration.programGeneration.CompiledClass;
import compilation.JasminCompiler;
import compilation.TypeException;
import types.BoolType;
import types.IntType;
import types.RefType;
import types.Type;
import util.JasvaInstructionSet;
import util.Pair;
import values.BoolValue;
import values.IntValue;
import values.Value;

public abstract class ASTComparisonNode implements JasvaASTNode {

    private final JasvaASTNode _left, _right;
    private final JasvaInstructionSet _cmpInstruction;
    private final String _comparisonType;

    public ASTComparisonNode(final JasvaASTNode left,
                             final JasvaASTNode right,
                             final String comparisonType,
                             final JasvaInstructionSet instruction) {
        _left = left;
        _right = right;
        _cmpInstruction = instruction;
        _comparisonType = comparisonType;
    }

    protected abstract BoolValue compare(final IntValue left, final IntValue right);

    @Override
    public final Value<?> evaluate(Environment<Value<?>> symbolTable) {
        final Value<?> leftValue = _left.evaluate(symbolTable);
        final Value<?> rightValue = _right.evaluate(symbolTable);
        if(leftValue instanceof IntValue && rightValue instanceof IntValue)
            return compare((IntValue) leftValue, (IntValue) rightValue);
        throw new TypeException(_comparisonType + " comparison", "incompatible types: comparison requires integer values");
    }

    @Override
    public final void compile(CompiledClass<JasminCompiler> classContext, JasminCompiler compiler, Environment<Pair<Integer, String>> symbolLinker) {
        _left.compile(classContext, compiler, symbolLinker);
        _right.compile(classContext, compiler, symbolLinker);
        final String labelTrue = compiler.genLabel();
        final String labelEnd = compiler.genLabel();
        compiler.pushInstruction(_cmpInstruction.instance(labelTrue));
        compiler.pushInstruction(JasvaInstructionSet.CST.instance(0));
        compiler.pushInstruction(JasvaInstructionSet.GOT.instance(labelEnd));
        compiler.pushLabel(labelTrue);
        compiler.pushInstruction(JasvaInstructionSet.CST.instance(1));
        compiler.pushLabel(labelEnd);
    }

    @Override
    public final Type typeCheck(Environment<Type> symbolTable) throws TypeException {
        final Type leftType = _left.typeCheck(symbolTable);
        final Type rightType = _right.typeCheck(symbolTable);

        if(leftType instanceof IntType && rightType instanceof IntType)
            return new BoolType();

        throw new TypeException(_comparisonType + " comparison", "incompatible types: comparison requires integer values");
    }

    @Override
    public final void printNode(String prefixHead, String prefixBody) {
        System.out.printf("%s%s\n", prefixHead, this);
        _left.printNode(prefixBody + "├─", prefixBody + "│ ");
        _right.printNode(prefixBody + "└─", prefixBody + "  ");
    }
}
