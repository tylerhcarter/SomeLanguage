package somelanguage.Interpreter;

import somelanguage.Parser.Token.TokenScanner;
import somelanguage.Interpreter.Constructs.Construct;
import somelanguage.Interpreter.Expressions.ExpressionProcessor;
import java.util.ArrayList;
import somelanguage.Variables.ComplexScope;
import somelanguage.Parser.Token.Token;
import somelanguage.Parser.Token.TokenType;
import somelanguage.Value.NullValue;
import somelanguage.Value.ReturnValue;
import somelanguage.Value.Value;
import somelanguage.Value.ValueType;

/**
 * Processes a list of tokens and preforms actions based on them
 * @author tylercarter
 */
public class Processor {
    private final ExpressionProcessor processor;
    private ArrayList<Construct> constructs;

    public Processor (ExpressionProcessor processor, ArrayList<Construct> constructs){
        this.processor = processor;
        this.constructs = constructs;
    }

    public Value run(ArrayList<Token> tokens, ComplexScope scope) throws Exception{

        // Add a end statement to whatever we are working with
        tokens.add(new Token(TokenType.END_STATEMENT));

        // Create a scanner
        TokenScanner scanner = new TokenScanner(tokens);

        // Run
        while(scanner.hasNext()){

            // Get Line
            TokenScanner statement = scanner.getTokenToEndStatement();

            // Parse Line
            Value value = parseLine(statement, scope);

            if(value.getType() == ValueType.RETURN){
                return ((ReturnValue) value).getValue();
            }
            
        }

        // No return value returns null
        return new NullValue();


    }

    private Value parseLine(TokenScanner statement, ComplexScope fullScope) throws Exception{

        Token token = statement.next(false);

        for(Construct construct:constructs){
            if(token.getTokenType() == construct.getToken()){
                return construct.process(this, statement.getTokens(), fullScope);
            }
        }


        return evaluateOperation(statement.getTokens(), fullScope);
        
    }

    /*
     * Create and run an inline function
     * This is useful for body statements that have the same scope, but aren't
     * always executed.
     */

    public Value runFunction(ArrayList<Token> tokens, ComplexScope scope) throws Exception{

        // Make new function and run
        Function bodyOp = new Function(this, tokens, scope);
        Value value = bodyOp.run(scope);

        return value;

    }

    /*
     * Evaulating an operation is going to require taking from the assignment operator
     * to the end statement as a new scanner.
     *
     * Scanner should return a scanner with the next X tokens in it, and advance
     * the current scanner to the end of it
     */
    public Value evaluateOperation(ArrayList<Token> tokens, ComplexScope scope) throws Exception {

        return this.processor.evaluate(tokens, scope);
        
    }

}
