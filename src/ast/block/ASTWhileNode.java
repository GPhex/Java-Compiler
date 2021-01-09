package ast.block;

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

public final class ASTWhileNode implements JasvaASTNode {

    private final JasvaASTNode _condition;
    private final JasvaASTNode _body;

    public ASTWhileNode(final JasvaASTNode condition, final JasvaASTNode body) {
        _condition = condition;
        _body = body;
    }

    @Override
    public final Value<?> evaluate(Environment<Value<?>> symbolTable) {
        Value<?> condState = _condition.evaluate(symbolTable);
        if(!(condState instanceof BoolValue))
            throw new TypeException("while", "condition is not a boolean value");
        if(((BoolValue) condState).getValue()) {
            _body.evaluate(symbolTable);
            evaluate(symbolTable);
        }
        return new BoolValue(false);
    }

    @Override
    public final void compile(CompiledClass<JasminCompiler> classContext, JasminCompiler compiler, Environment<Pair<Integer, String>> symbolLinker) {
        final String loopLabel = compiler.pushLabel();
        _condition.compile(classContext, compiler, symbolLinker);
        final String endLabel = compiler.genLabel();
        compiler.pushInstruction(JasvaInstructionSet.IFE.instance(endLabel));
        _body.compile(classContext, compiler, symbolLinker);
        compiler.pushInstruction(JasvaInstructionSet.POP.instance());
        compiler.pushInstruction(JasvaInstructionSet.GOT.instance(loopLabel));
        compiler.pushLabel(endLabel);
        compiler.pushInstruction(JasvaInstructionSet.CST.instance(0));
    }

    @Override
    public final Type typeCheck(Environment<Type> symbolTable) {
        final Type conditionType = _condition.typeCheck(symbolTable);
        _body.typeCheck(symbolTable);
        if(!(conditionType instanceof BoolType))
            throw new TypeException("while", "condition is not a boolean value");

        return new BoolType();
    }

    @Override
    public final void printNode(String prefixHead, String prefixBody) {
        System.out.printf("%s%s\n", prefixHead, this);
        _condition.printNode(prefixBody + "├─", prefixBody + "│ ");
        _body.printNode(prefixBody + "└─", prefixBody + "  ");
    }

    @Override
    public String toString() {
        return "ASTWhileNode";
    }
}
