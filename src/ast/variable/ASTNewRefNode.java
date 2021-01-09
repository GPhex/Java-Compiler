package ast.variable;

import ast.JasvaASTNode;
import codeGeneration.environment.Environment;
import codeGeneration.programGeneration.BaseTypes;
import codeGeneration.programGeneration.CompiledClass;
import codeGeneration.programGeneration.Modifiers;
import compilation.JasminCompiler;
import compilation.TypeException;
import compilation.fileGeneration.CachedProgramGenerator;
import types.RefType;
import types.Type;
import util.JasvaInstructionSet;
import util.Pair;
import values.CellValue;
import values.Value;

public final class ASTNewRefNode implements JasvaASTNode {

    private final JasvaASTNode _exp;
    private RefType _expType;

    public ASTNewRefNode(final JasvaASTNode expression) {
        _exp = expression;
        _expType = null;
    }

    @Override
    public final Value<?> evaluate(Environment<Value<?>> symbolTable) {
        return new CellValue(_exp.evaluate(symbolTable));
    }

    @Override
    public final void compile(CompiledClass<JasminCompiler> classContext, JasminCompiler compiler, Environment<Pair<Integer, String>> symbolLinker) {
        CompiledClass<JasminCompiler> referenceClass = CachedProgramGenerator.getInstance().getClass(_expType.getCompilerType());
        if(referenceClass == null) {
            referenceClass = CachedProgramGenerator.getInstance().addClass(_expType.getCompilerType(), BaseTypes.OBJECT);
            referenceClass.addSlot("value", _expType.getRefType().getCompilerType(), Modifiers.PUBLIC);
        }
        compiler.pushInstruction(JasvaInstructionSet.NEW.instance(referenceClass));
        compiler.pushInstruction(JasvaInstructionSet.DUP.instance());
        compiler.pushInstruction(JasvaInstructionSet.IVK.instance(referenceClass.getConstructor("init")));
        compiler.pushInstruction(JasvaInstructionSet.DUP.instance());
        _exp.compile(classContext, compiler, symbolLinker);
        compiler.pushInstruction(JasvaInstructionSet.PUT.instance(referenceClass.getSlot("value")));
    }

    @Override
    public final Type typeCheck(Environment<Type> symbolTable) throws TypeException {
        _expType = new RefType(_exp.typeCheck(symbolTable));
        return _expType;
    }

    @Override
    public final void printNode(String prefixHead, String prefixBody) {
        System.out.printf("%s%s\n", prefixHead, this);
        _exp.printNode(prefixBody + "└─", prefixBody + "  ");
    }

    @Override
    public String toString() {
        return "ASTNewRefNode";
    }
}
