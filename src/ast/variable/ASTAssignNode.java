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

public final class ASTAssignNode implements JasvaASTNode {

    private final JasvaASTNode _left;
    private final JasvaASTNode _right;
    private RefType _leftType;

    public ASTAssignNode(final JasvaASTNode leftExp, final JasvaASTNode rightExp) {
        _left = leftExp;
        _right = rightExp;
        _leftType = null;
    }

    @Override
    public final Value<?> evaluate(Environment<Value<?>> symbolTable) {
        final Value<?> leftVal = _left.evaluate(symbolTable);
        if(leftVal instanceof CellValue) {
            final Value<?> rightVal = _right.evaluate(symbolTable);
            ((CellValue) leftVal).set(rightVal);
            return rightVal;
        }
        throw new TypeException("assignment", "left expression is not a memory cell");
    }

    @Override
    public final void compile(CompiledClass<JasminCompiler> classContext, JasminCompiler compiler, Environment<Pair<Integer, String>> symbolLinker) {
        CompiledClass<JasminCompiler> referenceClass = CachedProgramGenerator.getInstance().getClass(_leftType.getCompilerType());
        if(referenceClass == null) {
            referenceClass = CachedProgramGenerator.getInstance().addClass(_leftType.getCompilerType(), BaseTypes.OBJECT);
            referenceClass.addSlot("value", _leftType.getRefType().getCompilerType(), Modifiers.PUBLIC);
        }
        _right.compile(classContext, compiler, symbolLinker);
        compiler.pushInstruction(JasvaInstructionSet.DUP.instance());
        _left.compile(classContext, compiler, symbolLinker);
        compiler.pushInstruction(JasvaInstructionSet.CHC.instance(_leftType.getCompilerType().asLiteral()));
        compiler.pushInstruction(JasvaInstructionSet.SWP.instance());
        compiler.pushInstruction(JasvaInstructionSet.PUT.instance(referenceClass.getSlot("value")));
    }

    @Override
    public final Type typeCheck(Environment<Type> symbolTable) throws TypeException {
        Type leftType = _left.typeCheck(symbolTable);
        if(leftType instanceof RefType) {
            _leftType = (RefType) leftType;
            leftType = ((RefType) leftType).getRefType();
            if(leftType.equals(_right.typeCheck(symbolTable)))
                return leftType;
            throw new TypeException("assignment", "type mismatch for memory cell and expression");
        }
        throw new TypeException("assignment", "left expression is not a memory cell");
    }

    @Override
    public final void printNode(String prefixHead, String prefixBody) {
        System.out.printf("%s%s\n", prefixHead, this);
        _left.printNode(prefixBody + "├─", prefixBody + "│ ");
        _right.printNode(prefixBody + "└─", prefixBody + "  ");
    }

    @Override
    public String toString() {
        return "ASTAssignNode";
    }
}
