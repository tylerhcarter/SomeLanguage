package somelanguage.Value;

import somelanguage.Parser.Token.Token;
import somelanguage.Parser.Token.TokenType;

/**
 *
 * @author tylercarter
 */
public class IntegerValue extends Value{
    private final int value;

    public IntegerValue(int value){
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }

    @Override
    public ValueType getType() {
        return ValueType.INTEGER;
    }

    public int toInteger(){
        return this.value;
    }

    public String toString(){
        return this.value + "";
    }

    @Override
    public Token toToken() {
        return new Token(TokenType.INTEGER, this);
    }

}
