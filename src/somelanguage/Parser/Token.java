package somelanguage.Parser;

/**
 *
 * @author tylercarter
 */
public class Token {
    private TokenType tokenType;
    private String tokenValue;

    public Token(TokenType tokenType){
        this.tokenType = tokenType;
        this.tokenValue = "";
    }

    public Token(TokenType tokenType, String tokenValue){
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
    public String getTokenValue() {
        return tokenValue;
    }

    @Override
    public String toString(){
        if(getTokenValue().isEmpty()){
            return getTokenType().toString();
        }
        return "(" + getTokenType().toString().toLowerCase() + ") " + getTokenValue();
    }

}
