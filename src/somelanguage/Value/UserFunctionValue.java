package somelanguage.Value;

import java.util.ArrayList;
import somelanguage.ComplexScope;
import somelanguage.Main;
import somelanguage.Parser.Token;
import somelanguage.Parser.TokenType;

/**
 *
 * @author tylercarter
 */
public class UserFunctionValue extends FunctionValue{
    private final ArrayList<Token> tokens;
    private final ComplexScope scope;

    public UserFunctionValue(ArrayList<Token> value, ComplexScope scope){
        this.tokens = value;
        this.scope = scope;
    }

    public Value call(ArrayList<Value> arguments) throws Exception{

        ComplexScope scope = this.scope;
        scope.local.addStack("User Function");

        int i = 0;
        for(Value value:arguments){
            i++;
            scope.local.addVariable("argument" + i, value);
        }
        
        Value value = Main.runner.run(tokens, scope);
        scope.local.removeStack("User Function");

        return value;
    }

    @Override
    public ValueType getType() {
        return ValueType.FUNCTION;
    }

    @Override
    public Token toToken() {
        return new Token(TokenType.STRING, getName());
    }

}
