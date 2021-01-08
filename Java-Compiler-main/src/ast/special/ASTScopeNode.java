package ast.special;

import IValue.IValue;
import ast.ASTNode;
import codeGeneration.Frame;
import codeGeneration.IntermediateCodeGenerator;
import codeGeneration.JVMInstructionSet;
import codeGeneration.environment.Environment;
import util.ConstantTypes;
import util.Pair;
import util.Type;

import java.util.List;

public final class ASTScopeNode implements ASTNode {

    private final List<Pair<String, ASTNode>> _assignments;
    private final ASTNode _scope;

    public ASTScopeNode(final List<Pair<String, ASTNode>> assignments,
                        final ASTNode scope) {
        _assignments = assignments;
        _scope = scope;
    }

    @Override
    public final IValue evaluate(Environment<IValue> variableDeclarations) {
        final Environment<IValue> scopedEnvironment = variableDeclarations.beginScope();
        _assignments.forEach(pair -> scopedEnvironment.associate(pair.getX(), pair.getY().evaluate(scopedEnvironment)));
        final IValue scopeValue = _scope.evaluate(scopedEnvironment);
        scopedEnvironment.endScope();
        return scopeValue;
    }

    @Override
    public final void compile(Environment<Pair<Integer, String>> variableLinker, IntermediateCodeGenerator generator) {
        final Environment<Pair<Integer, String>> scopeVariableLinker = variableLinker.beginScope();
        Frame defFrame = generator.createFrame();
        _assignments.forEach(assignment -> {
            final String slotName = defFrame.addSlot(ConstantTypes.INTEGER);
            generator.loadFrame();
            assignment.getY().compile(scopeVariableLinker, generator);
            generator.pushInstruction(JVMInstructionSet.FIELD_PUT, defFrame.getSlot(slotName));
            scopeVariableLinker.associate(assignment.getX(), new Pair<>(scopeVariableLinker.getDepth(), slotName));
        });
        _scope.compile(scopeVariableLinker, generator);
        generator.popFrame();
        scopeVariableLinker.endScope();
    }
}
