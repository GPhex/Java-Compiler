package ast.types;

import ast.JasvaASTNode;
import codeGeneration.environment.Environment;
import codeGeneration.programGeneration.CompiledClass;
import compilation.JasminCompiler;
import types.IntType;
import util.JasvaInstructionSet;
import util.Pair;
import values.IntValue;
import types.Type;
import values.Value;

public final class ASTIntNode implements JasvaASTNode {

    private final int _value;

    public ASTIntNode(final int value) {
        _value = value;
    }

    @Override
    public final Value<?> evaluate(Environment<Value<?>> symbolTable) {
        return new IntValue(_value);
    }

    @Override
    public final void compile(CompiledClass<JasminCompiler> classContext, JasminCompiler compiler, Environment<Pair<Integer, String>> symbolLinker) {
        compiler.pushInstruction(JasvaInstructionSet.PSH.instance(_value));
    }

    @Override
    public final Type typeCheck(Environment<Type> symbolTable) {
        return new IntType();
    }

    @Override
    public final void printNode(String prefixHead, String prefixBody) {
        System.out.printf("%s%s\n", prefixHead, this);
    }

    @Override
    public String toString() {
        return String.format("ASTIntNode(%d)", _value);
    }
}
