package ast;

import compilation.JasminCompiler;

public interface JasvaASTNode extends ASTNode<JasminCompiler> {

    void printNode(final String prefixHead,
                   final String prefixBody);

}
