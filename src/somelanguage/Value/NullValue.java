package somelanguage.Value;

import somelanguage.Parser.Token;
import somelanguage.Parser.TokenType;

/**
 *
 * @author tylercarter
 */
public class NullValue extends Value{

    @Override
    public ValueType getType() {
        return ValueType.NULL;
    }

    public String toString(){
        return "(null)";
    }

    @Override
    public Token toToken() {
        return new Token(TokenType.NULL);
    }

}
