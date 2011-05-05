package somelanguage.Interpreter;

import java.util.ArrayList;
import somelanguage.Interpreter.Math.Add;
import somelanguage.Interpreter.Math.And;
import somelanguage.Interpreter.Math.Divide;
import somelanguage.Interpreter.Math.Equality;
import somelanguage.Interpreter.Math.MathOperation;
import somelanguage.Interpreter.Math.Multiply;
import somelanguage.Interpreter.Math.Or;
import somelanguage.Interpreter.Math.Subtract;
import somelanguage.Variables.ComplexScope;
import somelanguage.Parser.Token.Token;
import somelanguage.Parser.Token.TokenType;
import somelanguage.Value.BooleanValue;
import somelanguage.Value.FunctionValue;
import somelanguage.Value.NullValue;
import somelanguage.Value.UserFunctionValue;
import somelanguage.Value.Value;
import somelanguage.Value.StringValue;

/**
 *
 * @author tylercarter
 */
public class ExpressionEngine {

    private ArrayList<MathOperation> operations = new ArrayList<MathOperation>();

    public ExpressionEngine(){

        // Add Math Operations
        this.operations.add(new Add(this));
        this.operations.add(new Subtract(this));
        this.operations.add(new Multiply(this));
        this.operations.add(new Divide(this));

        this.operations.add(new Equality(this));
        this.operations.add(new And(this));
        this.operations.add(new Or(this));
    }

    /*
     * Evaluates a single token to a value
     */
    public Value evaluate(Token token, ComplexScope scope) throws Exception{
        ArrayList<Token> tokens = new ArrayList<Token>();
        tokens.add(token);
        
        return getToken(tokens, 0, scope);
    }

    /*
     * Evaluates a series of tokens to a single value
     */
    public Value evaluate(ArrayList<Token> tokens, ComplexScope scope) throws Exception{

        if(tokens.isEmpty())
            return new NullValue();

        // Turn functions into values
        compileFunctions(tokens, scope);
        
        // Preform function calls
        doFunctionCalls(tokens, scope);
        
        // Math
        doBrackets(tokens, scope);

        for(MathOperation op:this.operations){
            op.doOperation(tokens, scope);
        }

        doAssignment(tokens, scope);

        if(tokens.size() > 1){
            throw new Exception("Badly Formed Expression.");
        }

        return getToken(tokens, 0, scope);
    }

     /*
     * Returns token's value
     */
    private Value getToken(ArrayList<Token> tokens, int i, ComplexScope scope) throws Exception {
        Token token = tokens.get(i);

        if(token.getTokenType() == TokenType.STRING){
            Value value = scope.getVariable(((StringValue) token.getTokenValue()).toString());
            return value;

        }else{
            return token.getTokenValue();
        }
    }

    /*
     * Searches for function calls and executes them
     */
    private void doFunctionCalls(ArrayList<Token> tokens, ComplexScope scope) throws Exception{

        if(tokens.isEmpty())
            return;

        for(int i = 0; i < tokens.size() - 1; i++){

            Token token = tokens.get(i);

            if(token.getTokenType() == TokenType.CLOSEBRACKET){
                throw new Exception("Unmatched Close Bracket.");
            }

            else if(token.getTokenType() == TokenType.STRING
                    && tokens.get(i + 1).getTokenType() == TokenType.OPENBRACKET){

                // Get Variable Name
                String name = token.getTokenValue().toString();

                // Get Variable Value
                Value v = scope.getVariable(name);

                // Convert Variable Value to FunctionValue
                FunctionValue value;
                try{
                    value = (FunctionValue) v;
                }catch(ClassCastException ex){
                    System.out.println(ex);
                    throw new Exception("Attempted to call a non-function.");
                }

                // Get Arguments
                int closeBracket = getCloseBracket(tokens, i + 1);
                if(closeBracket == -1){
                    throw new Exception("Unmatched Open Bracket.");
                }

                ArrayList<Token> statement = slice(tokens, i + 1, closeBracket);
                tokens.remove(i);

                ArrayList<ArrayList<Token>> arguments = new ArrayList<ArrayList<Token>>();

                arguments.add(new ArrayList<Token>());
                int k = 0;
                for(int o = 0; o < statement.size(); o++){

                    if(statement.get(o).getTokenType() == TokenType.COMMA){
                        arguments.add(new ArrayList<Token>());
                        k++;
                    }else{
                        arguments.get(k).add(statement.get(o));
                    }

                }

                ArrayList<Value> argumentValues = new ArrayList<Value>();
                for(int x = 0; x < arguments.size(); x++){
                    Value t = evaluate(arguments.get(x), scope);
                    argumentValues.add(t);
                }

                // Call it
                Value returnValue = value.call(argumentValues, scope);

                // Insert Return Value
                tokens.add(i, returnValue.toToken());
            }

        }

    }

    /*
     * Searches for bracketed expressions and evaluates them
     */
    private void doBrackets(ArrayList<Token> tokens, ComplexScope scope) throws Exception{

        if(tokens.isEmpty())
            return;

        for(int i = 0; i < tokens.size(); i++){

            Token token = tokens.get(i);

            if(token.getTokenType() == TokenType.CLOSEBRACKET){
                throw new Exception("Unmatched Close Bracket.");
            }

            else if(token.getTokenType() == TokenType.OPENBRACKET)
            {
                int closeBracket = getCloseBracket(tokens, i);
                if(closeBracket == -1){
                    throw new Exception("Unmatched Open Bracket.");
                }

                ArrayList<Token> subExpression = slice(tokens, i, closeBracket);

                // Evaluate the expression and insert it in place of the expression
                tokens.add(i, new Token(TokenType.INTEGER, evaluate(subExpression, scope)));
            }

        }

        return;
    }

    /*
     * Searchs for assignments and preforms them
     */
    private void doAssignment(ArrayList<Token> tokens, ComplexScope scope) throws Exception{

        // Loop through each token
        for(int i = 1; i < tokens.size(); i++){

            Token token = tokens.get(i);

            // Check if this is a divider
            if(token.getTokenType() == TokenType.ASSIGNMENT){

                // Check Left
                if((i - 1) < 0){
                    throw new Exception ("Expected STRING, found ADD");
                }
                String name = tokens.get(i - 1).getTokenValue().toString();

                // Check Right
                if((i + 1) >= tokens.size()){
                    throw new Exception ("Expected INTEGER, found END_STATEMENT");
                }

                Value value = getToken(tokens, i+1, scope);

                // Divide and replace with new token
                scope.setVariable(name, value);

                slice(tokens, i-1, i+1);
                tokens.add(i - 1, new Token(TokenType.BOOLEAN, new BooleanValue("true")));

                i = 0;

            }

        }

    }

     /*
     * Converts function declarations into function references
     */
    private void compileFunctions(ArrayList<Token> tokens, ComplexScope scope) throws Exception {

        for(int i = 0; i < tokens.size(); i++){
            Token token = tokens.get(i);

            if(token.getTokenType() == TokenType.FUNCTION_DECLARE){

                Value value = getFunction(tokens, scope);
                String name = ((FunctionValue) value).getName();
                scope.global.addVariable(name, value);

                tokens.add(i, new Token(TokenType.USERFUNC, value));

                i = 0;
            }
        }

    }

    /*
     * Removes tokens inside of a function delcaration and returns them
     */
    private Value getFunction(ArrayList<Token> tokens, ComplexScope scope) throws Exception{

        for(int i = 0; i < tokens.size(); i++){

            Token token = tokens.get(i);

            // Look for opening brace
            if(token.getTokenType() == TokenType.OPENBRACES){

                // Get close brace
                int closeBrace = getCloseBrace(tokens, i);

                // Get all braces between
                ArrayList<Token> statement = slice(tokens, i - 1, closeBrace);

                statement.remove(0);

                // Return them as a function
                return new UserFunctionValue(statement, scope);

            }

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

