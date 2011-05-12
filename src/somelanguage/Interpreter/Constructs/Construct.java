package somelanguage.Interpreter.Constructs;

import java.util.ArrayList;
import somelanguage.Interpreter.Processor;
import somelanguage.Parser.Token.Token;
import somelanguage.Parser.Token.TokenType;
import somelanguage.Value.Value;
import somelanguage.Variables.ComplexScope;

/**
 *
 * @author tylercarter
 */
public abstract class Construct{

    public abstract TokenType getToken();

    public abstract Value process(Processor processor,
            ArrayList<Token> tokens,
            ComplexScope scope) throws Exception;

}
