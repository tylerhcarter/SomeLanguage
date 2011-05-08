package somelanguage.Interpreter.Expressions;

import java.util.ArrayList;
import somelanguage.Parser.Token.Token;
import somelanguage.Parser.Token.TokenType;
import somelanguage.Value.Value;
import somelanguage.Variables.ComplexScope;

/**
 * Searches for an operator in a list of tokens and preforms actions on them
 * @author Tyler(Chacha) chacha@chacha102.com
 */
public abstract class MathOperation {
    protected final ExpressionProcessor engine;

    public MathOperation(ExpressionProcessor engine){
        this.engine = engine;
    }

    protected abstract TokenType getSearchToken();

    protected abstract Token evaluateExpression(Expression expression, ComplexScope scope) throws Exception;

    public void doOperation(ArrayList<Token> tokens, ComplexScope scope) throws Exception{

        for(int i = 0; i < tokens.size(); i++){

            Token token = tokens.get(i);

            // Check if it is the search token
            if(token.getTokenType() == getSearchToken()){

                // Get the value before
                if((i - 1) < 0){
                    throw new Exception ("Badly Formed Expression.");
                }
                Value numerator = this.engine.evaluate(tokens.get(i - 1), scope);

                // Check Right
                if((i + 1) >= tokens.size()){
                    throw new Exception ("Badly Formed Expression.");
                }
                Value divisor = this.engine.evaluate(tokens.get(i + 1), scope);

                Expression expression = new Expression(numerator, divisor);

                // Divide and replace with new token
                Token newToken = evaluateExpression(expression, scope);

                slice(tokens, i-1, i+1);
                tokens.add(i - 1, newToken);

                i = 0;

            }

        }
    }

    /*
     * Removes elements between start and end from token array and returns them
     */
    protected final ArrayList<Token> slice(ArrayList<Token> tokens, int start, int end){

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
