package ast;

import codeGeneration.IntermediateCodeGenerator;
import codeGeneration.environment.Environment;
import util.Pair;

public interface ASTNode {

    int evaluate(final Environment<Integer> variableDeclarations);

    void compile(final Environment<Pair<Integer, String>> variableLinker,
                 final IntermediateCodeGenerator builder);

}
