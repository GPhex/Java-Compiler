package ast;

import codeGeneration.programGeneration.CompiledClass;
import codeGeneration.Compiler;
import codeGeneration.environment.Environment;
import compilation.TypeException;
import types.Type;
import util.Pair;
import values.Value;

public interface ASTNode<C extends Compiler<?, ?, ?>> {

    Value<?> evaluate(final Environment<Value<?>> symbolTable);

    void compile(final CompiledClass<C> classContext,
                 final C compiler,
                 final Environment<Pair<Integer, String>> symbolLinker);

    Type typeCheck(final Environment<Type> symbolTable) throws TypeException;

}
