package ast;

import IValue.IValue;
import codeGeneration.IntermediateCodeGenerator;
import codeGeneration.environment.Environment;
import util.Pair;

public interface ASTNode {

    IValue evaluate(final Environment<IValue> variableDeclarations);

    void compile(final Environment<Pair<Integer, String>> variableLinker,
                 final IntermediateCodeGenerator builder);

}
