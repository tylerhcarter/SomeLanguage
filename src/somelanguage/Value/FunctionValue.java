package somelanguage.Value;

import java.util.ArrayList;
import somelanguage.Variables.ComplexScope;
import somelanguage.Parser.Token.Token;
import somelanguage.Parser.Token.TokenType;

/**
 *
 * @author tylercarter
 */
public abstract class FunctionValue extends Value{

    public abstract Value call(ArrayList<Value> arguments, ComplexScope scope) throws Exception;

    @Override
    public ValueType getType() {
        return ValueType.FUNCTION;
    }

    @Override
    public Token toToken() {
        return new Token(TokenType.USERFUNC, this);
    }

    public String getName() {
        return "func" + hashCode();
    }

}
