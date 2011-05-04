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

    public UserFunctionValue(ArrayList<Token> value){
        this.tokens = value;
    }

    public Value call(ArrayList<Value> arguments) throws Exception{

        ComplexScope scope = Main.scope;
        scope.local.addStack();

        int i = 0;
        for(Value value:arguments){
            i++;
            scope.local.addVariable("argument" + i, value);
        }
        
        Value value = Main.runner.run(tokens, scope);
        System.out.println(value);
        scope.local.removeStack();

        System.out.println("Return Value: " + value);

        return value;
    }

    @Override
    public ValueType getType() {
        return ValueType.FUNCTION;
    }

    @Override
    public Token toToken() {
        return new Token(TokenType.FUNCTION);
    }

}
