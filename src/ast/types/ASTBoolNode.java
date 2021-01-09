package ast.types;

import ast.JasvaASTNode;
import codeGeneration.environment.Environment;
import codeGeneration.programGeneration.CompiledClass;
import compilation.JasminCompiler;
import types.BoolType;
import util.JasvaInstructionSet;
import util.Pair;
import values.BoolValue;
import types.Type;
import values.Value;

public final class ASTBoolNode implements JasvaASTNode {

    private final boolean _value;

    public ASTBoolNode(final boolean value) {
        _value = value;
    }

    @Override
    public final Value<?> evaluate(Environment<Value<?>> symbolTable) {
        return new BoolValue(_value);
    }

    @Override
    public final void compile(CompiledClass<JasminCompiler> classContext, JasminCompiler compiler, Environment<Pair<Integer, String>> symbolLinker) {
        compiler.pushInstruction(JasvaInstructionSet.PSH.instance(_value ? 1 : 0));
    }

    @Override
    public final Type typeCheck(Environment<Type> symbolTable) {
        return new BoolType();
    }

    @Override
    public final void printNode(String prefixHead, String prefixBody) {
        System.out.printf("%s%s\n", prefixHead, this);
    }

    @Override
    public String toString() {
        return String.format("ASTBoolNode(%s)", _value);
    }
}
