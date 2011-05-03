package somelanguage.Interpreter;

import java.util.ArrayList;
import somelanguage.ComplexScope;
import somelanguage.Parser.Token;
import somelanguage.Parser.TokenType;
import somelanguage.Scanner;
import somelanguage.Scope;

/**
 *
 * @author tylercarter
 */
public class SyntaxChecker {

    public void run(ArrayList<Token> tokens) throws Exception{

        tokens.add(new Token(TokenType.END_STATEMENT));
        Scanner scanner = new Scanner(tokens);

        // @todo to separate global and local scope, but allow references to each
        Scope globalScope = new Scope();
        Scope localScope = new Scope();

        ComplexScope fullScope = new ComplexScope(globalScope, localScope);

        while(scanner.hasNext()){
            System.out.println("---");
            
            Token token = scanner.next(false);

            // Process individual statements
            Scanner statement = scanner.getTokenToEndStatement();
            
            System.out.print(token.getTokenType() + " :");
            System.out.println(statement.getTokens());
            
            // Local Declaration
            if(token.getTokenType() == TokenType.LOCAL_DECLARE){

                declareVariable(statement, fullScope, true);
                
            }
            
            // Global Declaration
            if(token.getTokenType() == TokenType.GLOBAL_DECLARE){

                declareVariable(statement, fullScope, false);

            }

            // @todo somewhere around here we will need to process
            // function calls
            // Probably a good idea to treat functions the same as
            // variables, and variables with () are called
            
            // Unencapsulated Strings are treated as variable names
            if(token.getTokenType() == TokenType.STRING){

                assignVariable(statement, fullScope);
            }

            System.out.println("Scope: " + fullScope);


        }


    }

    private void declareVariable(Scanner scanner, ComplexScope scope) throws Exception{
        declareVariable(scanner, scope, false);
    }

    private void declareVariable(Scanner scanner, ComplexScope scope, boolean local) throws Exception{

        if(scanner.next(false).getTokenType() == TokenType.LOCAL_DECLARE || scanner.next(false).getTokenType() == TokenType.GLOBAL_DECLARE ){
            scanner.next();
        }

        // Next token is the name
        String name = scanner.next().getTokenValue();

        // Check for assignment operator
        if(scanner.next().getTokenType() != TokenType.ASSIGNMENT){
            throw new Exception("Expecting Assignment Operator after '" + name +"', found" + scanner.getCurrent().getTokenType());
        }

        // Get the string
        String value = getValue(scanner.getTokenToEndStatement(), scope);

        if(local == true){
            scope.local.addVariable(name, value);
        }
        else{
            scope.global.addVariable(name, value);
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
        String value = getValue(scanner.getTokenToEndStatement(), scope);

        // Check if there is a local variable with this name
        scope.setVariable(name, value);

    }

    private String getValue(Scanner scanner, Scope scope) throws Exception{
        
        TokenType nextType = scanner.next(false).getTokenType();

        // Get an Encapsulated String
        if(nextType == TokenType.QUOTE){
            return getEncapsulatedString(scanner);
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

            throw new Exception("Expecting " + TokenType.QUOTE + ". "  + nextType + " given.");
        }

    }

    private String getEncapsulatedString(Scanner scanner) throws Exception{

        String value = "";

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

            value +=  t.getTokenValue();

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
    private String evaluateOperation(Scanner scanner, Scope scope) throws Exception {

        // Get next tokens
        Scanner operation = scanner.getTokenToEndStatement();

        Math math = new Math();

        return math.evaluate(operation.getTokens(), scope) + "";
    }

}
