package somelanguage.Parser.Token;

import java.util.ArrayList;

/**
 *
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

    /*
     * Returns the closest close bracket
     */
    private static int getCloseBracket(ArrayList<Token> tokens, int openBracket) {

        int scopeLevel = 1;
        for(int i = openBracket + 1; i < tokens.size(); i++){

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
