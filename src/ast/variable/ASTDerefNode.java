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

public final class ASTDerefNode implements JasvaASTNode {

    private final JasvaASTNode _exp;
    private RefType _expType;

    public ASTDerefNode(final JasvaASTNode expression) {
        _exp = expression;
        _expType = null;
    }

    @Override
    public final Value<?> evaluate(Environment<Value<?>> symbolTable) {
        final Value<?> expVal = _exp.evaluate(symbolTable);
        if(expVal instanceof CellValue)
            return ((CellValue) expVal).getValue();
        throw new TypeException("dereference", "expression is not a memory cell");
    }

    @Override
    public final void compile(CompiledClass<JasminCompiler> classContext, JasminCompiler compiler, Environment<Pair<Integer, String>> symbolLinker) {
        CompiledClass<JasminCompiler> referenceClass = CachedProgramGenerator.getInstance().getClass(_expType.getCompilerType());
        if(referenceClass == null) {
            referenceClass = CachedProgramGenerator.getInstance().addClass(_expType.getCompilerType(), BaseTypes.OBJECT);
            referenceClass.addSlot("value", _expType.getRefType().getCompilerType(), Modifiers.PUBLIC);
        }
        _exp.compile(classContext, compiler, symbolLinker);
        compiler.pushInstruction(JasvaInstructionSet.CHC.instance(_expType.getCompilerType().asLiteral()));
        compiler.pushInstruction(JasvaInstructionSet.GET.instance(referenceClass.getSlot("value")));
    }

    @Override
    public final Type typeCheck(Environment<Type> symbolTable) throws TypeException {
        final Type expType = _exp.typeCheck(symbolTable);
        if(expType instanceof RefType) {
            _expType = (RefType) expType;
            return ((RefType) expType).getRefType();
        }
        throw new TypeException("dereference", "expression is not a memory cell");
    }

    @Override
    public final void printNode(String prefixHead, String prefixBody) {
        System.out.printf("%s%s\n", prefixHead, this);
        _exp.printNode(prefixBody + "└─", prefixBody + "  ");
    }

    @Override
    public String toString() {
        return "ASTDerefNode";
    }
}
