package somelanguage.Interpreter;

import java.util.ArrayList;
import somelanguage.ComplexScope;
import somelanguage.Parser.Token;
import somelanguage.Parser.TokenType;
import somelanguage.Scanner;
import somelanguage.Scope;
import somelanguage.StackBasedScope;
import somelanguage.Value.MultiStringValue;
import somelanguage.Value.NullValue;
import somelanguage.Value.Value;

/**
 *
 * @author tylercarter
 */
public class Runner {

    public void run(ArrayList<Token> tokens, ComplexScope scope) throws Exception{

        // Add a end statement to whatever we are working with
        tokens.add(new Token(TokenType.END_STATEMENT));

        // Compile
        Compiler compiler = new Compiler();
        compiler.run(tokens, scope);

        // Create a scanner
        Scanner scanner = new Scanner(tokens);

        // Run
        while(scanner.hasNext()){

            // Get Line
            Scanner statement = scanner.getTokenToEndStatement();

            // Parse Line
            parseLine(statement, scope);

            // Feedback
            System.out.println(scope);
            
        }


    }

    private void parseLine(Scanner statement, ComplexScope fullScope) throws Exception{

        Token token = statement.next(false);

        // First Resolve Scope
        if(token.getTokenType() == TokenType.CLOSEBRACES 
                || token.getTokenType() == TokenType.OPENBRACES){

            if(token.getTokenType() == TokenType.CLOSEBRACES)
                fullScope.local.removeStack();
            else
                fullScope.local.addStack();

            // Scan past the bracket
            statement.next();
            token = statement.next(false);
            statement = statement.getTokenToEndStatement();
        }

        // Local Declaration
        if(token.getTokenType() == TokenType.LOCAL_DECLARE){

            // Add variable to scope
            String name = statement.next(2).getTokenValue();
            declareVariable(name, fullScope, true);

            // Trim statement
            statement.next();
            statement = statement.getTokenToEndStatement();

        }

        // Global Declaration
        if(token.getTokenType() == TokenType.GLOBAL_DECLARE){

            // Add variable to scope
            String name = statement.next(2).getTokenValue();
            declareVariable(name, fullScope, true);

            // Trim statement
            statement.next();
            statement = statement.getTokenToEndStatement();

        }

        if(statement.getTokens().isEmpty())
            return;

        evaluateOperation(statement, fullScope);

        
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

    private void assignVariable(Scanner scanner, Scope scope) throws Exception {

        if(scanner.next(false).getTokenType() == TokenType.LOCAL_DECLARE || scanner.next(false).getTokenType() == TokenType.GLOBAL_DECLARE ){
            scanner.next();
        }

        // Current token is the name
        String name = scanner.getCurrent().getTokenValue();
        
        // Check for assignment operator
        if(scanner.next().getTokenType() != TokenType.ASSIGNMENT){
            throw new Exception("Expecting Assignment Operator after '" + name +"', found" + scanner.getCurrent().getTokenType());
        }

        // Get the string
        Value value = getValue(scanner.getTokenToEndStatement(), scope);

        // Check if there is a local variable with this name
        scope.setVariable(name, value);

    }

    private Value getValue(Scanner scanner, Scope scope) throws Exception{
        
        TokenType nextType = scanner.next(false).getTokenType();

        // Get an Encapsulated String
        if(nextType == TokenType.QUOTE){
            return getEncapsulatedString(scanner);
        }

        else if(nextType == TokenType.NULL){
            return new NullValue();
        }

        // Evaluate the Statement if it starts with a number, variable, or open brace
        else if(nextType == TokenType.INTEGER 
                || nextType == TokenType.STRING
                || nextType == TokenType.OPENBRACKET){
           return evaluateOperation(scanner.getTokenToEndStatement(), scope);
        }

        // @todo somewhere around here we will need to process
        // function calls

        // #todo we also need to handle mathmatical functions

        // Error
        else{
            throw new Exception("Unexpected Token " + nextType);
        }

    }

    private Value getEncapsulatedString(Scanner scanner) throws Exception{

        MultiStringValue value = new MultiStringValue("");

        // First get open string
        if(scanner.next().getTokenType() != TokenType.QUOTE){
            throw new Exception("Expect Quote. Found " + scanner.getCurrent().getTokenType());
        }
        
        // Get all values until close quote
        while(scanner.hasNext()){

            Token t = scanner.next();
            if(t.getTokenType() == TokenType.QUOTE){
                break;
            }

            value.add(t.getTokenValue());

        }

        return value;

    }

    /*
     * Evaulating an operation is going to require taking from the assignment operator
     * to the end statement as a new scanner.
     *
     * Scanner should return a scanner with the next X tokens in it, and advance
     * the current scanner to the end of it
     */
    private Value evaluateOperation(Scanner scanner, Scope scope) throws Exception {

        // Get next tokens
        Scanner operation = scanner.getTokenToEndStatement();

        Math math = new Math();

        return math.evaluate(operation.getTokens(), scope);
    }

}
