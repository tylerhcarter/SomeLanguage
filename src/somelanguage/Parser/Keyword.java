package somelanguage.Parser;

import somelanguage.Parser.Token.TokenType;

/**
 *
 * @author tylercarter
 */
public class Keyword {
    private final String keyword;
    private final TokenType tokenType;
    private final boolean breaking;

    public Keyword(String keyword, TokenType tokenType, boolean breaking){
        this.keyword = keyword;
        this.tokenType = tokenType;
        this.breaking = breaking;
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

    public boolean isBreaking(){
        return breaking;
    }

}
