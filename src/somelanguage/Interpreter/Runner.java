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

    public void run(ArrayList<Token> tokens) throws Exception{

        tokens.add(new Token(TokenType.END_STATEMENT));
        Scanner scanner = new Scanner(tokens);

        Scope globalScope = new Scope();
        StackBasedScope localScope = new StackBasedScope();
        localScope.addStack();

        // Combines the two scopes
        ComplexScope fullScope = new ComplexScope(globalScope, localScope);

        Compiler compiler = new Compiler();

        compiler.run(tokens, fullScope);
        System.out.println(fullScope);

        scanner.reset();

        // Run File
        while(scanner.hasNext()){
            
            Scanner statement = scanner.getTokenToEndStatement();
            parseLine(statement, fullScope);
            System.out.println(fullScope);
        }


    }

    private void parseLine(Scanner statement, ComplexScope fullScope) throws Exception{

        Token token = statement.next(false);

        if(token.getTokenType() == TokenType.CLOSEBRACES){
            fullScope.local.removeStack();

            // Scan past the bracket
            statement.next();
            token = statement.next(false);
            statement = statement.getTokenToEndStatement();
        }

        if(token.getTokenType() == TokenType.OPENBRACES){
            fullScope.local.addStack();

            // Scan past the bracket
            statement.next();
            token = statement.next(false);
            statement = statement.getTokenToEndStatement();
        }

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

            if(statement.next(2).getTokenType() == TokenType.ASSIGNMENT){
                assignVariable(statement, fullScope);    
            }else{
                evaluateOperation(statement, fullScope);
            }

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
        Value value = getValue(scanner.getTokenToEndStatement(), scope);

        if(local == true){
            scope.getLocal().addVariable(name, value);
        }
        else{
            scope.getGlobal().addVariable(name, value);
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
