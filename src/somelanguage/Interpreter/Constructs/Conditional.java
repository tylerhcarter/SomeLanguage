package somelanguage.Interpreter.Constructs;

import java.util.ArrayList;
import somelanguage.Interpreter.Processor;
import somelanguage.Interpreter.SyntaxException;
import somelanguage.Parser.Token.Token;
import somelanguage.Parser.Token.TokenType;
import somelanguage.Parser.Token.Tokens;
import somelanguage.Value.BooleanValue;
import somelanguage.Value.NullValue;
import somelanguage.Value.ReturnValue;
import somelanguage.Value.Value;
import somelanguage.Value.ValueType;
import somelanguage.Variables.ComplexScope;

/**
 *
 * @author tylercarter
 */
public class Conditional extends Construct{

    public TokenType getToken(){
        return TokenType.IF;
    }

    @Override
    public Value process(Processor processor, ArrayList<Token> tokens, ComplexScope scope) throws Exception{

        tokens.remove(0);

        // Evaulate
        Value value = executeConditional(processor, tokens, scope);
        return value;

    }

    /*
     * Evaluates a conditional block and executes the body if true
     */

    private Value executeConditional(Processor processor, ArrayList<Token> tokens, ComplexScope scope) throws Exception {

        // Evaluate Conditional
        boolean conditional = evaluateConditional(processor, tokens, scope);

        // Get Body
        ArrayList<Token> body = Tokens.sliceBody(tokens, TokenType.OPENBRACES, 0);

        // Execute body
        if(conditional){


            // Make new function and run
            Value value = processor.runFunction(body, scope);

            // Check if there was a return statement
            if(value.getType() != ValueType.NULL){

                return new ReturnValue(value);
            }

            return new NullValue();

        }

        // Check if we have more statements
        if(tokens.size() == 0){
            return new NullValue();
        }

        // Check if an else if statement is there
        if (tokens.get(0).getTokenType() == TokenType.ELIF){

            // Trim statement
            tokens.remove(0);
            return executeConditional(processor, tokens, scope);

        }

        // Check if an else statement is present
        else if (tokens.get(0).getTokenType() == TokenType.ELSE){

            // Trim statement
            tokens.remove(0);

            // Get body
            ArrayList<Token> elseBody = Tokens.sliceBody(tokens, TokenType.OPENBRACES, 0);

            // Make new function and run
            Value value = processor.runFunction(elseBody, scope);

            // Check if there was a return statement
            if(value.getType() != ValueType.NULL){

                return new ReturnValue(value);
            }

            return new NullValue();

        }

        // Something else is there...
        else{

            throw new Exception("Unexpected " + tokens.get(0).getTokenType());

        }

    }

    private boolean evaluateConditional(Processor processor, ArrayList<Token> tokens, ComplexScope fullScope) throws Exception {

        // Get Conditional
        ArrayList<Token> conditional = Tokens.sliceBody(tokens, TokenType.OPENBRACKET, 0);

        // Check if it evaluates to true
        Value value = processor.evaluateOperation(conditional, fullScope);

        // Convert to boolean value
        BooleanValue proceed = new BooleanValue("false");
        try{
            proceed = (BooleanValue) value;
        }catch(ClassCastException ex){
            throw new SyntaxException("Could not convert conditional to boolean.", tokens);
        }

        return proceed.getValue();

    }

}
