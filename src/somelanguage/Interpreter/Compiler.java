package somelanguage.Interpreter;

import java.util.ArrayList;
import somelanguage.ComplexScope;
import somelanguage.Parser.Token;
import somelanguage.Parser.TokenType;
import somelanguage.Value.UserFunctionValue;

/**
 *
 * @author tylercarter
 */
public class Compiler {


    public ArrayList<Token> run(ArrayList<Token> tokens, ComplexScope scope) throws Exception{


        // Loops through tokens looking for function declarations.
        for(int i = 0; i < tokens.size() - 1; i++){

            Token token = tokens.get(i);
            Token next = tokens.get(i + 1);

            if(token.getTokenType() == TokenType.STRING &&
                    next.getTokenType() == TokenType.OPENBRACES){

                    String name = token.getTokenValue();

                    int closeBrace = getCloseBrace(tokens, i);
                    
                    ArrayList<Token> statement = slice(tokens, i, closeBrace);
                    
                    scope.local.addVariable(name, new UserFunctionValue(statement, scope));

            }

        }

        
        return tokens;
    }

     private int getCloseBrace(ArrayList<Token> tokens, int openBracket) {

        int scopeLevel = 1;
        for(int i = openBracket + 2; i < tokens.size(); i++){
            
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

    /*
     * Removes elements between start and end from token array and returns them
     */
    private ArrayList<Token> slice(ArrayList<Token> tokens, int start, int end){

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

}
