package ast.variable;

import ast.JasvaASTNode;
import codeGeneration.environment.Environment;
import codeGeneration.programGeneration.CompiledClass;
import codeGeneration.programGeneration.Modifiers;
import compilation.Frame;
import compilation.JasminCompiler;
import util.JasvaInstructionSet;
import util.Pair;
import types.Type;
import values.Value;

public final class ASTDefineNode implements JasvaASTNode {

    protected final String _symbol;
    protected final Type _type;
    protected final JasvaASTNode _expression;

    public ASTDefineNode(final String symbol, final Type type, final JasvaASTNode expValue) {
        _symbol = symbol;
        _type = type;
        _expression = expValue;
    }

    @Override
    public Value<?> evaluate(Environment<Value<?>> symbolTable) {
        final Value<?> expValue = _expression.evaluate(symbolTable);
        symbolTable.associate(_symbol, expValue);
        return expValue;
    }

    @Override
    public void compile(CompiledClass<JasminCompiler> classContext, JasminCompiler compiler, Environment<Pair<Integer, String>> symbolLinker) {
        final Frame defScope = compiler.loadFrame();
        final String slotName = defScope.addSlot(_type.getCompilerType(), Modifiers.PUBLIC);
        _expression.compile(classContext, compiler, symbolLinker);
        compiler.pushInstruction(JasvaInstructionSet.PUT.instance(defScope.getSlot(slotName), true));
        symbolLinker.associate(_symbol, new Pair<>(symbolLinker.depth(), slotName));
    }

    @Override
    public Type typeCheck(Environment<Type> symbolTable) {
        final Type expType = _expression.typeCheck(symbolTable);
        symbolTable.associate(_symbol, expType);
        return expType;
    }

    @Override
    public void printNode(String prefixHead, String prefixBody) {
        System.out.printf("%s%s\n", prefixHead, this);
        _expression.printNode(prefixBody + "└─", prefixBody + "  ");
    }

    @Override
    public String toString() {
        return String.format("ASTDefineNode(%s, type %s)", _symbol, _type);
    }
}
