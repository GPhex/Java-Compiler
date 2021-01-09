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

public final class ASTNotNode implements JasvaASTNode {

    private final JasvaASTNode _exp;

    public ASTNotNode(final JasvaASTNode expression) {
        _exp = expression;
    }

    @Override
    public final Value<?> evaluate(Environment<Value<?>> symbolTable) {
        final Value<?> expVal = _exp.evaluate(symbolTable);
        if(expVal instanceof BoolValue) {
            return ((BoolValue) expVal).not();
        }
        throw new TypeException("boolean or", "expression is not a boolean value");
    }

    @Override
    public final void compile(CompiledClass<JasminCompiler> classContext, JasminCompiler compiler, Environment<Pair<Integer, String>> symbolLinker) {
        _exp.compile(classContext, compiler, symbolLinker);
        compiler.pushInstruction(JasvaInstructionSet.PSH.instance(1));
        compiler.pushInstruction(JasvaInstructionSet.SUB.instance());
        compiler.pushInstruction(JasvaInstructionSet.NEG.instance());
    }

    @Override
    public final Type typeCheck(Environment<Type> symbolTable) {
        final Type expType = _exp.typeCheck(symbolTable);
        if(expType instanceof BoolType)
            return new BoolType();
        throw new TypeException("boolean or", "expression is not a boolean value");
    }

    @Override
    public final void printNode(String prefixHead, String prefixBody) {
        System.out.printf("%s%s\n", prefixHead, this);
        _exp.printNode(prefixBody + "└─", prefixBody + "  ");
    }

    @Override
    public String toString() {
        return "ASTNotNode";
    }
}
