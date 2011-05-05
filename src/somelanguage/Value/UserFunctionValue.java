package somelanguage.Value;

import somelanguage.Variables.Variable;
import java.util.ArrayList;
import somelanguage.Variables.ComplexScope;
import somelanguage.Interpreter.Function;
import somelanguage.Main;
import somelanguage.Parser.Token.Token;
import somelanguage.Parser.Token.TokenType;
import somelanguage.Variables.Scope;
import somelanguage.Variables.StackBasedScope;

/**
 *
 * @author tylercarter
 */
public class UserFunctionValue extends FunctionValue{
    public final Function script;

    public UserFunctionValue(ArrayList<Token> tokens, ComplexScope scope){

        // Create function to run later
        this.script = new Function(Main.runner, tokens, scope);
        
    }

    public Value call(ArrayList<Value> arguments, ComplexScope parentScope) throws Exception{

        // Run function and return value
        Value value = script.run(arguments, parentScope);
        return value;
        
    }

    @Override
    public ValueType getType() {
        return ValueType.FUNCTION;
    }

    @Override
    public Token toToken() {
        return new Token(TokenType.USERFUNC, this);
    }

}
