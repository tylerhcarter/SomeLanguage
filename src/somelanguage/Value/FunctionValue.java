package somelanguage.Value;

import java.util.ArrayList;
import somelanguage.Parser.Token;
import somelanguage.Parser.TokenType;

/**
 *
 * @author tylercarter
 */
public abstract class FunctionValue extends Value{

    public abstract Value call(ArrayList<Value> arguments) throws Exception;

    @Override
    public ValueType getType() {
        return ValueType.FUNCTION;
    }

    @Override
    public Token toToken() {
        return new Token(TokenType.FUNCTION);
    }

}
