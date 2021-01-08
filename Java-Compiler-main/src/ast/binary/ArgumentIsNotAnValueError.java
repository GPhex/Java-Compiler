package ast.binary;

public class ArgumentIsNotAnValueError extends RuntimeException{
    public ArgumentIsNotAnValueError(String value){
        super(String.format("Argument is not an %s",value));
    }
}
