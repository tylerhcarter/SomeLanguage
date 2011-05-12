package somelanguage.Interpreter.Constructs;

import java.util.ArrayList;
import somelanguage.Interpreter.Processor;
import somelanguage.Parser.Token.Token;
import somelanguage.Parser.Token.TokenType;
import somelanguage.Value.ReturnValue;
import somelanguage.Value.Value;
import somelanguage.Variables.ComplexScope;

/**
 *
 * @author tylercarter
 */
public class Return extends Construct{

    public TokenType getToken(){
        return TokenType.RETURN;
    }

    @Override
    public Value process(Processor processor, ArrayList<Token> tokens, ComplexScope scope) throws Exception{

        // Remove return token
        tokens.remove(0);

        // Evaulate
        Value value = processor.evaluateOperation(tokens, scope);
        return new ReturnValue(value);

    }

}
