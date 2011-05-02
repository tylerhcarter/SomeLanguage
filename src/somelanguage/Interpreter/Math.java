package somelanguage.Interpreter;

import java.util.ArrayList;
import somelanguage.Parser.Token;
import somelanguage.Parser.TokenType;

/**
 *
 * @author tylercarter
 */
public class Math {

    public int evalute(ArrayList<Token> tokens){
        return 0;
    }

    public int doBrackets(ArrayList<Token> tokens) throws Exception{

        for(Token token:tokens){

            if(token.getTokenType() == TokenType.CLOSEBRACKET){
                throw new Exception("Unmatched Close Bracket");
            }

            if(token.getTokenType() == TokenType.OPENBRACKET){
                throw new Exception("Unmatched Close Bracket");
            }

        }

        return 0;
    }

}
