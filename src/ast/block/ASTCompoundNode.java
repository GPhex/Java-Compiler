package ast.block;

import ast.JasvaASTNode;
import codeGeneration.environment.Environment;
import codeGeneration.programGeneration.CompiledClass;
import compilation.JasminCompiler;
import util.JasvaInstructionSet;
import types.Type;
import util.Pair;
import values.Value;

public final class ASTCompoundNode implements JasvaASTNode {

    private final JasvaASTNode _left, _right;

    public ASTCompoundNode(final JasvaASTNode left, final JasvaASTNode right) {
        _left = left;
        _right = right;
    }

    @Override
    public Value<?> evaluate(Environment<Value<?>> symbolTable) {
        _left.evaluate(symbolTable);
        return _right.evaluate(symbolTable);
    }

    @Override
    public void compile(CompiledClass<JasminCompiler> classContext, JasminCompiler compiler, Environment<Pair<Integer, String>> symbolLinker) {
        _left.compile(classContext, compiler, symbolLinker);
        compiler.pushInstruction(JasvaInstructionSet.POP.instance());
        _right.compile(classContext, compiler, symbolLinker);
    }

    @Override
    public Type typeCheck(Environment<Type> symbolTable) {
        _left.typeCheck(symbolTable);
        return _right.typeCheck(symbolTable);
    }

    @Override
    public final void printNode(String prefixHead, String prefixBody) {
        System.out.printf("%s%s\n", prefixHead, this);
        _left.printNode(prefixBody + "├─", prefixBody + "│ ");
        _right.printNode(prefixBody + "└─", prefixBody + "  ");
    }

    @Override
    public String toString() {
        return "ASTCompoundNode";
    }
}
