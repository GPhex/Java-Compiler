package ast.logical;

import ast.JasvaASTNode;
import codeGeneration.environment.Environment;
import codeGeneration.programGeneration.CompiledClass;
import compilation.JasminCompiler;
import compilation.TypeException;
import types.BoolType;
import types.Type;
import util.JasvaInstructionSet;
import util.Pair;
import values.BoolValue;
import values.Value;

public final class ASTOrNode implements JasvaASTNode {

    private final JasvaASTNode _left;
    private final JasvaASTNode _right;

    public ASTOrNode(final JasvaASTNode left, final JasvaASTNode right) {
        _left = left;
        _right = right;
    }

    @Override
    public final Value<?> evaluate(Environment<Value<?>> symbolTable) {
        final Value<?> leftVal = _left.evaluate(symbolTable);
        if(leftVal instanceof BoolValue) {
            final Value<?> rightVal = _right.evaluate(symbolTable);
            if(rightVal instanceof BoolValue) {
                return ((BoolValue) leftVal).or((BoolValue) rightVal);
            }
            throw new TypeException("boolean or", "right hand side is not a boolean value");
        }
        throw new TypeException("boolean or", "left hand side is not a boolean value");
    }

    @Override
    public final void compile(CompiledClass<JasminCompiler> classContext, JasminCompiler compiler, Environment<Pair<Integer, String>> symbolLinker) {
        _left.compile(classContext, compiler, symbolLinker);
        _right.compile(classContext, compiler, symbolLinker);
        compiler.pushInstruction(JasvaInstructionSet.BOR.instance());
    }

    @Override
    public final Type typeCheck(Environment<Type> symbolTable) {
        final Type leftType = _left.typeCheck(symbolTable);
        if(leftType instanceof BoolType) {
            final Type rightType = _right.typeCheck(symbolTable);
            if(rightType instanceof BoolType) {
                return new BoolType();
            }
            throw new TypeException("boolean or", "right hand side is not a boolean value");
        }
        throw new TypeException("boolean or", "left hand side is not a boolean value");
    }

    @Override
    public final void printNode(String prefixHead, String prefixBody) {
        System.out.printf("%s%s\n", prefixHead, this);
        _left.printNode(prefixBody + "├─", prefixBody + "│ ");
        _right.printNode(prefixBody + "└─", prefixBody + "  ");
    }

    @Override
    public String toString() {
        return "ASTOrNode";
    }
}
