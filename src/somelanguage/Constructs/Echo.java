package somelanguage.Constructs;

import java.util.ArrayList;
import somelanguage.Interpreter.Processor;
import somelanguage.Interpreter.Construct;
import somelanguage.Parser.Token.Token;
import somelanguage.Parser.Token.TokenType;
import somelanguage.Value.ReturnValue;
import somelanguage.Value.Value;
import somelanguage.Variables.ComplexScope;

/**
 *
 * @author tylercarter
 */
public class Echo extends Construct{

    public TokenType getToken(){
        return TokenType.ECHO;
    }

    @Override
    public Value process(Processor processor,
                         ArrayList<Token> tokens,
                         ComplexScope scope) throws Exception{

        // Remove echo token
        tokens.remove(0);

        // Evaulate
        Value value = processor.evaluateOperation(tokens, scope);
        System.out.println(value.toString());
        return value;

    }

}
