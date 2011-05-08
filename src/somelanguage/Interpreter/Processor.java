package somelanguage.Interpreter;

import somelanguage.Interpreter.Math.ExpressionProcessor;
import java.util.ArrayList;
import somelanguage.Variables.ComplexScope;
import somelanguage.Parser.Token.Token;
import somelanguage.Parser.Token.TokenType;
import somelanguage.Parser.Token.Tokens;
import somelanguage.Value.NullValue;
import somelanguage.Value.ReturnValue;
import somelanguage.Value.BooleanValue;
import somelanguage.Value.Value;
import somelanguage.Value.ValueType;

/**
 * Processes a list of tokens and preforms actions based on them
 * @author tylercarter
 */
public class Processor {

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

        // First Resolve Scope
        if(token.getTokenType() == TokenType.CLOSEBRACES 
                || token.getTokenType() == TokenType.OPENBRACES){

            if(token.getTokenType() == TokenType.CLOSEBRACES)
                fullScope.local.removeStack("Anonymous Scope");
            else
                fullScope.local.addStack("Anonymous Scope");

            // Scan past the bracket
            statement.next();
            token = statement.next(false);
            statement = statement.getTokenToEndStatement();
        }

        // Local Declaration
        if(token.getTokenType() == TokenType.LOCAL_DECLARE
                || token.getTokenType() == TokenType.GLOBAL_DECLARE){

            // Declare variable
            processDeclaration(statement.getTokens(), fullScope);

            // Remove declaration token
            statement.getTokens().remove(0);

            // Evaulate
            return evaluateOperation(statement, fullScope);

        }

        // Return Value
        else if(token.getTokenType() == TokenType.RETURN){

            // Trim statement
            statement.next();
            statement = statement.getTokenToEndStatement();

            return new ReturnValue(evaluateOperation(statement, fullScope));
        }

        // Conditional block
        else if(token.getTokenType() == TokenType.IF){

            // Trim statement
            statement.next();
            statement = statement.getTokenToEndStatement();
            
            return executeConditional(statement, fullScope);

        }

        else {

            return evaluateOperation(statement, fullScope);

        }
        
    }

    private void processDeclaration(ArrayList<Token> tokens, ComplexScope scope) throws Exception{

        Token declare = tokens.get(0);
        Token name = tokens.get(1);

        if(declare.getTokenType() == TokenType.GLOBAL_DECLARE){
            declareVariable(name.getTokenValue().toString(), scope, false);
        }
        else if(declare.getTokenType() == TokenType.LOCAL_DECLARE){
            declareVariable(name.getTokenValue().toString(), scope, true);
        }
        else{
            throw new Exception("Expecting GLOBAL_DECLARE or LOCAL_DECLARE. Found " + declare.getTokenType());
        }

    }

    private void declareVariable(String name, ComplexScope scope, boolean local) throws Exception{

        if(local == true){
            scope.getLocal().addVariable(name);
        }
        else{
            scope.getGlobal().addVariable(name);
        }
    }

    /*
     * Evaulating an operation is going to require taking from the assignment operator
     * to the end statement as a new scanner.
     *
     * Scanner should return a scanner with the next X tokens in it, and advance
     * the current scanner to the end of it
     */
    private Value evaluateOperation(TokenScanner scanner, ComplexScope scope) throws Exception {
        return evaluateOperation(scanner.getTokens(), scope);
    }

    private Value evaluateOperation(ArrayList<Token> tokens, ComplexScope scope) throws Exception {

        ExpressionProcessor math = new ExpressionProcessor();
        return math.evaluate(tokens, scope);
        
    }

    /*
     * Evaluates a conditional block and executes the body if true
     */
    private Value executeConditional(TokenScanner statement, ComplexScope scope) throws Exception {
        return executeConditional(statement.getTokens(), scope);
    }
    private Value executeConditional(ArrayList<Token> tokens, ComplexScope scope) throws Exception {

        // Evaluate Conditional
        boolean conditional = evaluateConditional(tokens, scope);

        // Get Body
        ArrayList<Token> body = Tokens.sliceBody(tokens, TokenType.OPENBRACES, 0);

        // Execute body
        if(conditional){

            
            // Make new function and run
            Value value = runFunction(body, scope);

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
            return executeConditional(tokens, scope);

        }

        // Check if an else statement is present
        else if (tokens.get(0).getTokenType() == TokenType.ELSE){

            // Trim statement
            tokens.remove(0);

            // Get body
            ArrayList<Token> elseBody = Tokens.sliceBody(tokens, TokenType.OPENBRACES, 0);

            // Make new function and run
            Value value = runFunction(elseBody, scope);

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

    private boolean evaluateConditional(ArrayList<Token> tokens, ComplexScope fullScope) throws Exception {

        // Get Conditional
        ArrayList<Token> conditional = Tokens.sliceBody(tokens, TokenType.OPENBRACKET, 0);

        // Check if it evaluates to true
        Value value = evaluateOperation(conditional, fullScope);

        // Convert to boolean value
        BooleanValue proceed = new BooleanValue("false");
        try{
            proceed = (BooleanValue) value;
        }catch(ClassCastException ex){
            System.out.println("Could not convert conditional to boolean.");
        }

        return proceed.getValue();

    }

    /*
     * Create and run an inline function
     * This is useful for body statements that have the same scope, but aren't
     * always executed.
     */

    private Value runFunction(ArrayList<Token> tokens, ComplexScope scope) throws Exception{

        // Make new function and run
        Function bodyOp = new Function(this, tokens, scope);
        Value value = bodyOp.run(scope);

        return value;
        
    }

}
