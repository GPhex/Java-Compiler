package ast.arithmetic;

import ast.JasvaASTNode;
import codeGeneration.environment.Environment;
import codeGeneration.programGeneration.CompiledClass;
import compilation.JasminCompiler;
import compilation.TypeException;
import types.IntType;
import util.JasvaInstructionSet;
import util.Pair;
import values.IntValue;
import types.Type;
import values.Value;

public final class ASTModNode implements JasvaASTNode {

    private final JasvaASTNode _left;
    private final JasvaASTNode _right;

    public ASTModNode(final JasvaASTNode left, final JasvaASTNode right) {
        _left = left;
        _right = right;
    }

    @Override
    public final Value<?> evaluate(Environment<Value<?>> symbolTable) {
        final Value<?> leftVal = _left.evaluate(symbolTable);
        if(leftVal instanceof IntValue) {
            final Value<?> rightVal = _right.evaluate(symbolTable);
            if(rightVal instanceof IntValue) {
                return ((IntValue) leftVal).mod((IntValue) rightVal);
            }
            throw new TypeException("modulus", "right hand side is not an integer value");
        }
        throw new TypeException("modulus", "left hand side is not an integer value");
    }

    @Override
    public final void compile(CompiledClass<JasminCompiler> classContext, JasminCompiler compiler, Environment<Pair<Integer, String>> symbolLinker) {
        _left.compile(classContext, compiler, symbolLinker);
        _right.compile(classContext, compiler, symbolLinker);
        compiler.pushInstruction(JasvaInstructionSet.MOD.instance());
    }

    @Override
    public final Type typeCheck(Environment<Type> symbolTable) {
        final Type leftType = _left.typeCheck(symbolTable);
        if(leftType instanceof IntType) {
            final Type rightType = _right.typeCheck(symbolTable);
            if(rightType instanceof IntType) {
                return new IntType();
            }
            throw new TypeException("modulus", "right hand side is not an integer value");
        }
        throw new TypeException("modulus", "left hand side is not an integer value");
    }

    @Override
    public final void printNode(String prefixHead, String prefixBody) {
        System.out.printf("%s%s\n", prefixHead, this);
        _left.printNode(prefixBody + "├─", prefixBody + "│ ");
        _right.printNode(prefixBody + "└─", prefixBody + "  ");
    }

    @Override
    public String toString() {
        return "ASTModNode";
    }
}
