package somelanguage.Interpreter;

import java.util.ArrayList;
import somelanguage.Parser.Token.Token;

/**
 *
 * @author Tyler(Chacha) chacha@chacha102.com
 */
public class SyntaxException extends Exception {
    private final ArrayList<Token> tokens;

    public SyntaxException(String message, ArrayList<Token> tokens){
        super(message);
        this.tokens = tokens;
    }

    public ArrayList<Token> getTokens(){
        return tokens;
    }
}
