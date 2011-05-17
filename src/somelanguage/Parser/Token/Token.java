package somelanguage.Parser.Token;

import somelanguage.Value.NullValue;
import somelanguage.Value.Value;

/**
 *
 * @author tylercarter
 */
public class Token {
    private TokenType tokenType;
    private Value tokenValue;

    public Token(TokenType tokenType){
        this.tokenType = tokenType;
        this.tokenValue = new NullValue();
    }

    public Token(TokenType tokenType, Value tokenValue){
        this.tokenType = tokenType;
        this.tokenValue = tokenValue;
    }

    /**
     * @return the tokenType
     */
    public TokenType getTokenType() {
        return tokenType;
    }

    /**
     * @return the tokenValue
     */
    public Value getTokenValue() {
        return tokenValue;
    }

    @Override
    public String toString(){
        if(getTokenValue().toString().isEmpty()){
            return getTokenType().toString();
        }
        return "(" + getTokenType().toString().toLowerCase() + ") " + getTokenValue();
    }

}