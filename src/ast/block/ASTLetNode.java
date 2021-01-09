package ast.block;

import ast.JasvaASTNode;
import ast.variable.ASTDefineNode;
import codeGeneration.environment.Environment;
import codeGeneration.programGeneration.CompiledClass;
import compilation.JasminCompiler;
import types.Type;
import util.Pair;
import values.Value;

import java.util.Iterator;
import java.util.List;

public final class ASTLetNode implements JasvaASTNode {

    private final List<ASTDefineNode> _assignments;
    private final JasvaASTNode _body;

    public ASTLetNode(final List<ASTDefineNode> assignments, final JasvaASTNode scope) {
        _assignments = assignments;
        _body = scope;
    }

    @Override
    public final Value<?> evaluate(Environment<Value<?>> symbolTable) {
        final Environment<Value<?>> scope = symbolTable.beginScope();
        _assignments.forEach(assignment -> assignment.evaluate(scope));
        final Value<?> bodyValue = _body.evaluate(scope);
        scope.endScope();
        return bodyValue;
    }

    @Override
    public final void compile(CompiledClass<JasminCompiler> classContext, JasminCompiler compiler, Environment<Pair<Integer, String>> symbolLinker) {
        final Environment<Pair<Integer, String>> scope = symbolLinker.beginScope();
        compiler.beginScope();
        _assignments.forEach(definitionNode -> definitionNode.compile(classContext, compiler, scope));
        _body.compile(classContext, compiler, scope);
        compiler.endScope();
        scope.endScope();
    }

    @Override
    public final Type typeCheck(Environment<Type> symbolTable) {
        final Environment<Type> scope = symbolTable.beginScope();
        _assignments.forEach(assignment -> assignment.typeCheck(scope));
        final Type bodyType = _body.typeCheck(scope);
        scope.endScope();
        return bodyType;
    }

    @Override
    public final void printNode(String prefixHead, String prefixBody) {
        System.out.printf("%s%s\n", prefixHead, this);
        Iterator<ASTDefineNode> it = _assignments.listIterator();
        while(it.hasNext()) {
            ASTDefineNode node = it.next();
            if(it.hasNext())
                node.printNode(prefixBody + "├─", prefixBody + "│ ");
            else
                node.printNode(prefixBody + "└─", prefixBody + "│ ");
        }
        _body.printNode(prefixBody + "└─", prefixBody + "  ");
    }

    @Override
    public String toString() {
        return "ASTLetNode";
    }
}
