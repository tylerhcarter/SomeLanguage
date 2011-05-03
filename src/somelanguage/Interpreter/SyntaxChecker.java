package somelanguage.Interpreter;

import java.util.ArrayList;
import somelanguage.Parser.Token;
import somelanguage.Parser.TokenType;
import somelanguage.Scanner;
import somelanguage.Scope;

/**
 *
 * @author tylercarter
 */
public class SyntaxChecker {

    private Scanner scanner;

    public void run(ArrayList<Token> tokens) throws Exception{

        tokens.add(new Token(TokenType.END_STATEMENT));
        this.scanner = new Scanner(tokens);

        // @todo to separate global and local scope, but allow references to each
        Scope globalScope = new Scope();
        Scope localScope = new Scope();

        while(scanner.hasNext()){
            Token token = scanner.next();

            // Process individual statements
            
            // Local Declaration
            if(token.getTokenType() == TokenType.LOCAL_DECLARE){

                declareVariable(localScope);
                
            }
            
            // Global Declaration
            if(token.getTokenType() == TokenType.GLOBAL_DECLARE){

                declareVariable(globalScope);

            }

            // @todo somewhere around here we will need to process
            // function calls
            // Probably a good idea to treat functions the same as
            // variables, and variables with () are called
            
            // Unencapsulated Strings are treated as variable names
            if(token.getTokenType() == TokenType.STRING){

                assignVariable(globalScope, localScope);
            }

        }

        System.out.println(localScope);

    }

    private void declareVariable(Scope localScope) throws Exception{

        // Next token is the name
        String name = scanner.next().getTokenValue();

        // Check for assignment operator
        if(scanner.next().getTokenType() != TokenType.ASSIGNMENT){
            throw new Exception("Expecting Assignment Operator after '" + name +"', found" + scanner.getCurrent().getTokenType());
        }

        // Get the string
        String value = getValue(localScope);

        localScope.addVariable(name, value);
    }

    private void assignVariable(Scope globalScope, Scope localScope) throws Exception {

        // Current token is the name
        String name = scanner.getCurrent().getTokenValue();

        // Check for assignment operator
        if(scanner.next().getTokenType() != TokenType.ASSIGNMENT){
            throw new Exception("Expecting Assignment Operator after '" + name +"', found" + scanner.getCurrent().getTokenType());
        }

        // Get the string
        String value = getValue(localScope);

        // Check if there is a local variable with this name
        if(localScope.getVariable(name).equals("undefined")){
            globalScope.setVariable(name, value);
        }else{
            localScope.setVariable(name, value);
        }

    }

    private String getValue(Scope localScope) throws Exception{

        TokenType nextType = scanner.next(false).getTokenType();

        // Get an Encapsulated String
        if(nextType == TokenType.QUOTE){
            return getEncapsulatedString();
        }

        // Get Integer
        else if(nextType == TokenType.INTEGER){

            // Check if this is the only thing on the line
            if(scanner.next(2).getTokenType() == TokenType.END_STATEMENT){

                // Good, we're done
                return scanner.next().getTokenValue();
                
            }else{

                // We have more processing to do
                return evaluateOperation(localScope);
            }
            
        }

        // Get a variable's value
        else if(nextType == TokenType.STRING){
            
            return localScope.getVariable(scanner.next().getTokenValue());
            
        }

        // @todo somewhere around here we will need to process
        // function calls

        // #todo we also need to handle mathmatical functions

        // Error
        else{

            throw new Exception("Expecting " + TokenType.QUOTE + ". "  + nextType + " given.");
        }

    }

    private String getEncapsulatedString() throws Exception{

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
    private String evaluateOperation(Scope localScope) throws Exception {

        // Get next tokens
        Scanner operation = this.scanner.getTokenToEndStatement();

        Math math = new Math();

        return math.evaluate(operation.getTokens(), localScope) + "";
    }

}
