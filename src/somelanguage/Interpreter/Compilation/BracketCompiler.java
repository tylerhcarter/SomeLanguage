package somelanguage.Interpreter.Compilation;

import java.util.ArrayList;
import somelanguage.Interpreter.Expressions.ExpressionProcessor;
import somelanguage.Interpreter.SyntaxException;
import somelanguage.Variables.ComplexScope;
import somelanguage.Parser.Token.*;

/**
 *
 * @author tylercarter
 */
public class BracketCompiler implements Compiler{

    public void process(ArrayList<Token> tokens, ComplexScope scope, ExpressionProcessor processor) throws Exception{

        for(int i = 0; i < tokens.size(); i++){

            Token token = tokens.get(i);

            if(token.getTokenType() == TokenType.CLOSEBRACKET){
                throw new SyntaxException("Unmatched Close Bracket.", tokens);
            }

            else if(token.getTokenType() == TokenType.OPENBRACKET)
            {

                ArrayList<Token> subExpression = Tokens.sliceBody(tokens, TokenType.OPENBRACKET, i);

                // Evaluate the expression and insert it in place of the expression
                tokens.add(i, new Token(TokenType.INTEGER, processor.evaluate(subExpression, scope)));
            }

        }

        return;

    }

}
