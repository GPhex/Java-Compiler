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

public final class ASTNegNode implements JasvaASTNode {

    private final JasvaASTNode _exp;

    public ASTNegNode(final JasvaASTNode expression) {
        _exp = expression;
    }

    @Override
    public final Value<?> evaluate(Environment<Value<?>> symbolTable) {
        final Value<?> expVal = _exp.evaluate(symbolTable);
        if(expVal instanceof IntValue) {
            return ((IntValue) expVal).neg();
        }
        throw new TypeException("negation", "argument is not an integer value");
    }

    @Override
    public final void compile(CompiledClass<JasminCompiler> classContext, JasminCompiler compiler, Environment<Pair<Integer, String>> symbolLinker) {
        _exp.compile(classContext, compiler, symbolLinker);
        compiler.pushInstruction(JasvaInstructionSet.NEG.instance());
    }

    @Override
    public final Type typeCheck(Environment<Type> symbolTable) {
        final Type expType = _exp.typeCheck(symbolTable);
        if(expType instanceof IntType) {
            return new IntType();
        }
        throw new TypeException("negation", "left hand side is not an integer value");
    }

    @Override
    public final void printNode(String prefixHead, String prefixBody) {
        System.out.printf("%s%s\n", prefixHead, this);
        _exp.printNode(prefixBody + "└─", prefixBody + "  ");
    }

    @Override
    public String toString() {
        return "ASTNegNode";
    }
}
