package somelanguage.Interpreter;

import java.util.ArrayList;
import somelanguage.Variables.ComplexScope;
import somelanguage.Parser.Token.Token;
import somelanguage.Parser.Token.TokenType;
import somelanguage.Value.NullValue;
import somelanguage.Value.ReturnValue;
import somelanguage.Value.BooleanValue;
import somelanguage.Value.Value;
import somelanguage.Value.ValueType;

/**
 *
 * @author tylercarter
 */
public class Runner {

    public Value run(ArrayList<Token> tokens, ComplexScope scope) throws Exception{

        // Add a end statement to whatever we are working with
        tokens.add(new Token(TokenType.END_STATEMENT));

        // Create a scanner
        Scanner scanner = new Scanner(tokens);

        // Run
        while(scanner.hasNext()){

            // Get Line
            Scanner statement = scanner.getTokenToEndStatement();

            // Parse Line
            Value value = parseLine(statement, scope);

            if(value.getType() == ValueType.RETURN){
                return ((ReturnValue) value).getValue();
            }
            
        }

        // No return value returns null
        return new NullValue();


    }

    private Value parseLine(Scanner statement, ComplexScope fullScope) throws Exception{

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
        if(token.getTokenType() == TokenType.LOCAL_DECLARE){

            // Add variable to scope
            String name = statement.next(2).getTokenValue().toString();
            declareVariable(name, fullScope, true);

            // Trim statement
            statement.next();
            statement = statement.getTokenToEndStatement();

            return evaluateOperation(statement, fullScope);

        }

        // Global Declaration
        else if(token.getTokenType() == TokenType.GLOBAL_DECLARE){

            // Add variable to scope
            String name = statement.next(2).getTokenValue().toString();
            declareVariable(name, fullScope, false);

            // Trim statement
            statement.next();
            statement = statement.getTokenToEndStatement();

            return  evaluateOperation(statement, fullScope);
        }

        // Return Value
        else if(token.getTokenType() == TokenType.RETURN){

            // Trim statement
            statement.next();
            statement = statement.getTokenToEndStatement();

            return new ReturnValue(evaluateOperation(statement, fullScope));
        }

        else if(token.getTokenType() == TokenType.IF){

            // Trim statement
            statement.next();
            statement = statement.getTokenToEndStatement();
            
            return evaluateConditional(statement, fullScope);

        }

        else {

            return evaluateOperation(statement, fullScope);

        }
        
    }

    private void declareVariable(String name, ComplexScope scope) throws Exception{
        declareVariable(name, scope, false);
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
    private Value evaluateOperation(Scanner scanner, ComplexScope scope) throws Exception {
        return evaluateOperation(scanner.getTokens(), scope);
    }

    private Value evaluateOperation(ArrayList<Token> tokens, ComplexScope scope) throws Exception {

        ExpressionEngine math = new ExpressionEngine();
        return math.evaluate(tokens, scope);
        
    }

    private Value evaluateConditional(Scanner statement, ComplexScope fullScope) throws Exception {

        ArrayList<Token> tokens = statement.getTokens();

        System.out.println(tokens);

        // Next token should be a open bracket
        Token openBracket = tokens.get(0);
        if(openBracket.getTokenType() != TokenType.OPENBRACKET){
            throw new Exception("Expecting OPENBRACKET. Found " + openBracket.getTokenType());
        }

        // Find the close bracket
        int closeBracket = getCloseBracket(tokens, 0);

        // Get Conditional
        ArrayList<Token> conditional = slice(tokens, 0, closeBracket);

        System.out.println(conditional);

        System.out.println("Evaluating Conditional: ");
        // Check if it evaluates to true
        Value value = evaluateOperation(conditional, fullScope);
        System.out.println(value);
        BooleanValue proceed = new BooleanValue("false");
        
        try{
            proceed = (BooleanValue) value;
        }catch(ClassCastException ex){
            System.out.println("Could not convert conditional to boolean.");
        }

        // Check whether to proceed with body
        if(proceed.getValue() == true){

            // Get the body
            Token openBrace = tokens.get(0);
            if(openBrace.getTokenType() != TokenType.OPENBRACES){
                throw new Exception("Expecting OPENBRACE. Found " + openBracket.getTokenType());
            }

            // Get Close Brace
            int closeBrace = getCloseBrace(tokens, 0);

            // Get Conditional
            ArrayList<Token> body = slice(tokens, 0, closeBrace);

            // Make new function and run
            Function bodyOp = new Function(this, body, fullScope);
            Value v = bodyOp.run(new ArrayList<Value>(), fullScope);

            return v;

        }

        return new NullValue();
    }

    /*
     * Removes elements between start and end from token array and returns them
     */
    private ArrayList<Token> slice(ArrayList<Token> tokens, int start, int end){

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

    /*
     * Returns the closest close bracket
     */
    private int getCloseBracket(ArrayList<Token> tokens, int openBracket) {

        int scopeLevel = 1;
        for(int i = openBracket + 1; i < tokens.size(); i++){

            if(tokens.get(i).getTokenType() == TokenType.OPENBRACKET){
                scopeLevel += 1;
            }
            else if(tokens.get(i).getTokenType() == TokenType.CLOSEBRACKET){

                scopeLevel -= 1;
                if(scopeLevel == 0)
                    return i;
            }

        }

        return -1;

    }

    /*
     * Returns closest close brace
     */
    private int getCloseBrace(ArrayList<Token> tokens, int openBracket) {

        int scopeLevel = 1;
        for(int i = openBracket + 1; i < tokens.size(); i++){

            if(tokens.get(i).getTokenType() == TokenType.OPENBRACES){
                scopeLevel += 1;
            }
            else if(tokens.get(i).getTokenType() == TokenType.CLOSEBRACES){

                scopeLevel -= 1;
                if(scopeLevel == 0)
                    return i;
            }

        }

        return -1;
     }

}
