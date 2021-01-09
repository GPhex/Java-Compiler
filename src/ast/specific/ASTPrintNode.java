package ast.specific;

import ast.JasvaASTNode;
import codeGeneration.environment.Environment;
import codeGeneration.programGeneration.CompiledClass;
import compilation.JasminCompiler;
import compilation.TypeException;
import types.RefType;
import types.Type;
import util.JasvaInstructionSet;
import util.Pair;
import values.BoolValue;
import values.CellValue;
import values.Value;

public final class ASTPrintNode implements JasvaASTNode {

    private final JasvaASTNode _expression;
    private final boolean _changeLine;

    public ASTPrintNode(final JasvaASTNode expression, final boolean changeLine) {
        _expression = expression;
        _changeLine = changeLine;
    }

    @Override
    public final Value<?> evaluate(Environment<Value<?>> symbolTable) {
        Value<?> expVal = new BoolValue(false);
        if(_expression != null) {
            expVal = _expression.evaluate(symbolTable);
            if(expVal instanceof CellValue)
                throw new TypeException("printing", "cannot print memory cells");
            if(_changeLine) System.out.println(expVal);
            else System.out.print(expVal);
        }
        else if(_changeLine)
            System.out.println();
        return expVal;
    }

    @Override
    public final void compile(CompiledClass<JasminCompiler> classContext, JasminCompiler compiler, Environment<Pair<Integer, String>> symbolLinker) {
        if(_expression != null) {
            _expression.compile(classContext, compiler, symbolLinker);
            compiler.pushInstruction(JasvaInstructionSet.DUP.instance());
            compiler.printValue(true, _changeLine);
        }
        else if(_changeLine)
            compiler.printValue(false, true);
    }

    @Override
    public final Type typeCheck(Environment<Type> symbolTable) throws TypeException {
        final Type expType = _expression.typeCheck(symbolTable);
        if(expType instanceof RefType)
            throw new TypeException("printing", "cannot print memory cells");
        return expType;
    }

    @Override
    public final void printNode(String prefixHead, String prefixBody) {
        System.out.printf("%s%s\n", prefixHead, this);
        _expression.printNode(prefixBody + "└─", prefixBody + "  ");
    }

    @Override
    public String toString() {
        return "ASTPrintNode";
    }
}
