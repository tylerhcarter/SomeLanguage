package somelanguage.Parser;

import somelanguage.Parser.Token.TokenType;

/**
 *
 * @author tylercarter
 */
public class Keyword {
    private final String keyword;
    private final TokenType tokenType;

    public Keyword(String keyword, TokenType tokenType){
        this.keyword = keyword;
        this.tokenType = tokenType;
    }

    /**
     * @return the keyword
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * @return the tokenType
     */
    public TokenType getTokenType() {
        return tokenType;
    }

}
