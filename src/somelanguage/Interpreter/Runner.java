package somelanguage.Interpreter;

import java.util.ArrayList;
import somelanguage.Variables.ComplexScope;
import somelanguage.Parser.Token.Token;
import somelanguage.Parser.Token.TokenType;
import somelanguage.Value.NullValue;
import somelanguage.Value.ReturnValue;
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

        boolean returnValue = false;

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

        }

        // Global Declaration
        if(token.getTokenType() == TokenType.GLOBAL_DECLARE){

            // Add variable to scope
            String name = statement.next(2).getTokenValue().toString();
            declareVariable(name, fullScope, false);

            // Trim statement
            statement.next();
            statement = statement.getTokenToEndStatement();

        }

        // Return Value
        if(token.getTokenType() == TokenType.RETURN){
            returnValue = true;

            // Trim statement
            statement.next();
            statement = statement.getTokenToEndStatement();
        }

        if(statement.getTokens().isEmpty())
            return new NullValue();

        Value value =  evaluateOperation(statement, fullScope);

        if(returnValue == true){
            value = new ReturnValue(value);
        }

        return value;
        
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

        // Get next tokens
        Scanner operation = scanner.getTokenToEndStatement();

        ExpressionEngine math = new ExpressionEngine();

        return math.evaluate(operation.getTokens(), scope);
    }

}
