package ast.variable;

import ast.JasvaASTNode;
import codeGeneration.environment.Environment;
import codeGeneration.programGeneration.BaseTypes;
import codeGeneration.programGeneration.CompiledClass;
import compilation.Frame;
import compilation.JasminCompiler;
import compilation.TypeException;
import util.JasvaInstructionSet;
import util.Pair;
import types.Type;
import values.Value;

public final class ASTIdNode implements JasvaASTNode {

    private final String _identifier;

    public ASTIdNode(final String symbol) {
        _identifier = symbol;
    }

    @Override
    public Value<?> evaluate(Environment<Value<?>> symbolTable) {
        return symbolTable.find(_identifier);
    }

    @Override
    public void compile(CompiledClass<JasminCompiler> classContext, JasminCompiler compiler, Environment<Pair<Integer, String>> symbolLinker) {
        final Pair<Integer, String> coordinates = symbolLinker.find(_identifier);
        int dereferences = symbolLinker.depth() - coordinates.getX();
        Frame curFrame = compiler.loadFrame();
        for(; dereferences > 0; dereferences--) {
            compiler.pushInstruction(JasvaInstructionSet.GET.instance(curFrame.getSlot(Frame.SL_SLOT)));
            curFrame = curFrame.getParent();
        }
        compiler.pushInstruction(JasvaInstructionSet.GET.instance(curFrame.getSlot(coordinates.getY()), true));
    }

    @Override
    public Type typeCheck(Environment<Type> symbolTable) throws TypeException {
        return symbolTable.find(_identifier);
    }

    @Override
    public final void printNode(String prefixHead, String prefixBody) {
        System.out.printf("%s%s\n", prefixHead, this);
    }

    @Override
    public String toString() {
        return String.format("ASTIdNode(%s)", _identifier);
    }
}
