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

public final class ASTIfNode implements JasvaASTNode {

    private final JasvaASTNode _condition;
    private final JasvaASTNode _tBranch, _fBranch;

    public ASTIfNode(final JasvaASTNode condition, final JasvaASTNode trueBranch, final JasvaASTNode falseBranch) {
        _condition = condition;
        _tBranch = trueBranch;
        _fBranch = falseBranch;
    }

    @Override
    public final Value<?> evaluate(Environment<Value<?>> symbolTable) {
        final Value<?> conditionalValue = _condition.evaluate(symbolTable);

        if(conditionalValue instanceof BoolValue) {
            if(((BoolValue) conditionalValue).getValue())
                return _tBranch.evaluate(symbolTable);
            else if(_fBranch != null)
                return _fBranch.evaluate(symbolTable);
            else return new BoolValue(false);
        }
        throw new TypeException("conditional", "condition is not a boolean value");
    }

    @Override
    public final void compile(CompiledClass<JasminCompiler> classContext, JasminCompiler compiler, Environment<Pair<Integer, String>> symbolLinker) {
        _condition.compile(classContext, compiler, symbolLinker);
        final String labelElse = compiler.genLabel();
        final String labelEnd = compiler.genLabel();
        compiler.pushInstruction(JasvaInstructionSet.IFE.instance(labelElse));
        _tBranch.compile(classContext, compiler, symbolLinker);
        compiler.pushInstruction(JasvaInstructionSet.GOT.instance(labelEnd));
        compiler.pushLabel(labelElse);
        if(_fBranch != null)
            _fBranch.compile(classContext, compiler, symbolLinker);
        else
            compiler.pushInstruction(JasvaInstructionSet.CST.instance(0));
        compiler.pushLabel(labelEnd);
    }

    @Override
    public final Type typeCheck(Environment<Type> symbolTable) {
        final Type conditionType = _condition.typeCheck(symbolTable);
        if(conditionType instanceof BoolType) {
            final Type tBranchType = _tBranch.typeCheck(symbolTable);
            if(_fBranch != null) {
                final Type fBranchType = _fBranch.typeCheck(symbolTable);
                if(tBranchType.equals(fBranchType))
                    return tBranchType;
                throw new TypeException("conditional", "mismatch in then/else branch types");
            }
            return tBranchType;
        }
        throw new TypeException("conditional", "condition is not a boolean value");
    }

    @Override
    public final void printNode(String prefixHead, String prefixBody) {
        System.out.printf("%s%s\n", prefixHead, this);
        _condition.printNode(prefixBody + "├─", prefixBody + "│ ");
        if(_fBranch != null) {
            _tBranch.printNode(prefixBody + "├─", prefixBody + "│ ");
            _fBranch.printNode(prefixBody + "└─", prefixBody + "  ");
        }
        else
            _tBranch.printNode(prefixBody + "└─", prefixBody + "  ");
    }

    @Override
    public String toString() {
        return "ASTIfNode";
    }
}
