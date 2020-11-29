package ast.special;

import ast.ASTNode;
import codeGeneration.Frame;
import codeGeneration.IntermediateCodeGenerator;
import codeGeneration.JVMInstructionSet;
import codeGeneration.environment.Environment;
import util.Pair;

public final class ASTVarRefNode implements ASTNode {

    private final String _variable;

    public ASTVarRefNode(final String variable) {
        _variable = variable;
    }

    @Override
    public final int evaluate(Environment<Integer> variableDeclarations) {
        return variableDeclarations.find(_variable);
    }

    @Override
    public final void compile(Environment<Pair<Integer, String>> variableLinker, IntermediateCodeGenerator generator) {
        final Pair<Integer, String> varCoordinates = variableLinker.find(_variable);
        int dereferenceCount = variableLinker.getDepth() - varCoordinates.getX();
        Frame curFrame = generator.loadFrame();
        for(; dereferenceCount > 0; dereferenceCount--) {
            generator.pushInstruction(JVMInstructionSet.FIELD_GET, curFrame.getSlot(Frame.PARENT_SLOT));
            curFrame = curFrame.getParent();
        }
        generator.pushInstruction(JVMInstructionSet.FIELD_GET, curFrame.getSlot(varCoordinates.getY()));
    }
}
