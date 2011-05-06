package somelanguage.Parser.Token;

import java.util.ArrayList;

/**
 * Library of functions for dealing with Tokens
 * @author tylercarter
 */
public class Tokens {

    /*
     * Removes elements between start and end from token array and returns them
     */
    public static ArrayList<Token> slice(ArrayList<Token> tokens, int start, int end){

        ArrayList<Token> result = new ArrayList<Token>();

        for(int i = start; i < end - 1; i++){
            Token token = tokens.remove(start + 1);
            result.add(token);
        }

        // Get rid of old brackets
        tokens.remove(start);
        tokens.remove(start);

        return result;

    }

    public static ArrayList<Token> sliceBody(ArrayList<Token> tokens, TokenType type, int start) throws Exception{

        Token open = tokens.get(start);
        if(open.getTokenType() != type){
            throw new Exception("Expecting " + type + " found " + open.getTokenType());
        }

        // Get close position
        int close;
        if(type == TokenType.OPENBRACES){
            close = getCloseBrace(tokens, start);
        }else if(type == TokenType.OPENBRACKET){
            close = getCloseBracket(tokens, start);
        }else{
            throw new Exception("Expecting OPENBRACE or OPENBRACKET.");
        }

        // Get values inbetween
        ArrayList<Token> body = slice(tokens, start, close);
        return body;

    }

    public static Token getToken(ArrayList<Token> tokens, TokenType searchToken){
        return tokens.get(findToken(tokens, searchToken));
    }

    public static Token getToken(ArrayList<Token> tokens, TokenType[] searchTokens){
        return tokens.get(findToken(tokens, searchTokens));
    }

    public static Token getToken(ArrayList<Token> tokens, ArrayList<TokenType> searchTokens){
        return tokens.get(findToken(tokens, searchTokens));
    }

    public static int findToken(ArrayList<Token> tokens, TokenType searchToken){

        for(int i = 0; i < tokens.size(); i++)
            if(tokens.get(i).getTokenType() == searchToken)
                return i;

        return -1;
        
    }

    public static int findToken(ArrayList<Token> tokens, TokenType[] searchTokens){

        for(int i = 0; i < tokens.size(); i++)
            for(TokenType type:searchTokens)
                if(tokens.get(i).getTokenType() == type)
                    return i;

        return -1;

    }

    public static int findToken(ArrayList<Token> tokens, ArrayList<TokenType> searchTokens){

        for(int i = 0; i < tokens.size(); i++)
            for(int x = 0; x < searchTokens.size(); x++)
                if(tokens.get(i).getTokenType() == searchTokens.get(x))
                    return i;

        return -1;

    }

    public static boolean hasToken(ArrayList<Token> tokens, TokenType searchToken){
        if(findToken(tokens, searchToken) != -1){
            return true;
        }else{
            return false;
        }
    }

    public static boolean hasToken(ArrayList<Token> tokens, TokenType[] searchTokens){
        if(findToken(tokens, searchTokens) != -1){
            return true;
        }else{
            return false;
        }
    }

    public static boolean hasToken(ArrayList<Token> tokens, ArrayList<TokenType> searchTokens){
        if(findToken(tokens, searchTokens) != -1){
            return true;
        }else{
            return false;
        }
    }

    /*
     * Returns the closest close bracket
     */
    private static int getCloseBracket(ArrayList<Token> tokens, int openBracket) {

        int scopeLevel = 1;
        for(int i = openBracket + 1; i < tokens.size(); i++) {

            if(tokens.get(i).getTokenType() == TokenType.OPENBRACKET){
                scopeLevel += 1;
            }
            else if(tokens.get(i).getTokenType() == TokenType.CLOSEBRACKET){

                scopeLevel -= 1;
                if(scopeLevel == 0)
                    return i;
            }

        }

        return -1;

    }

    /*
     * Returns closest close brace
     */
    private static int getCloseBrace(ArrayList<Token> tokens, int openBracket) {

        int scopeLevel = 1;
        for(int i = openBracket + 1; i < tokens.size(); i++){

            if(tokens.get(i).getTokenType() == TokenType.OPENBRACES){
                scopeLevel += 1;
            }
            else if(tokens.get(i).getTokenType() == TokenType.CLOSEBRACES){

                scopeLevel -= 1;
                if(scopeLevel == 0)
                    return i;
            }

        }

        return -1;
     }

}
