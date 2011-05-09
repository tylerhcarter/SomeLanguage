package somelanguage.Interpreter.Expressions;

import java.util.ArrayList;
import somelanguage.Variables.ComplexScope;
import somelanguage.Parser.Token.Token;
import somelanguage.Parser.Token.TokenType;
import somelanguage.Parser.Token.Tokens;
import somelanguage.Value.BooleanValue;
import somelanguage.Value.FunctionValue;
import somelanguage.Value.NullValue;
import somelanguage.Value.UserFunctionValue;
import somelanguage.Value.Value;
import somelanguage.Value.StringValue;

/**
 * Proccesses a list of tokens and evaluates them to a single value
 * @author tylercarter
 */
public class ExpressionProcessor {

    private ArrayList<MathOperation> operations = new ArrayList<MathOperation>();

    public ExpressionProcessor(){

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

        System.out.println(tokens);

        // Math
        doBrackets(tokens, scope);

        for(MathOperation op:this.operations){
            op.doOperation(tokens, scope);
        }

        doAssignment(tokens, scope);

        if(tokens.size() > 1){
            System.out.println(tokens);
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

            else if(token.getTokenType() == TokenType.USERFUNC){

                Value v = token.getTokenValue();

                // Convert Variable Value to FunctionValue
                FunctionValue value;
                try{
                    value = (FunctionValue) v;
                }catch(ClassCastException ex){
                    System.out.println(ex);
                    throw new Exception("Attempted to call a non-function.");
                }

                ArrayList<Token> statement = Tokens.sliceBody(tokens, TokenType.OPENBRACKET, i + 1);
                tokens.remove(i);

                // Call it
                Value returnValue = callFunc(value, statement, scope);

                // Insert Return Value
                tokens.add(i, returnValue.toToken());

            }

            else if(token.getTokenType() == TokenType.STRING
                    && tokens.get(i + 1).getTokenType() == TokenType.OPENBRACKET){

                // Get Variable Name
                String name = token.getTokenValue().toString();

                // Get Variable Value
                Value value = scope.getVariable(name);

                ArrayList<Token> statement = Tokens.sliceBody(tokens, TokenType.OPENBRACKET, i + 1);
                tokens.remove(i);

                // Call it
                Value returnValue = callFunc(value, statement, scope);

                // Insert Return Value
                tokens.add(i, returnValue.toToken());
            }

        }

    }

    private Value callFunc(Value function, ArrayList<Token> argumentTokens, ComplexScope scope) throws Exception{

        // Convert Variable Value to FunctionValue
        FunctionValue fValue;
        try{
            fValue = (FunctionValue) function;
        }catch(ClassCastException ex){
            System.out.println(ex);
            throw new Exception("Attempted to call a non-function.");
        }

        ArrayList<ArrayList<Token>> arguments = new ArrayList<ArrayList<Token>>();

        arguments.add(new ArrayList<Token>());
        int k = 0;
        for(int o = 0; o < argumentTokens.size(); o++){

            if(argumentTokens.get(o).getTokenType() == TokenType.COMMA){
                arguments.add(new ArrayList<Token>());
                k++;
            }else{
                arguments.get(k).add(argumentTokens.get(o));
            }

        }

        ArrayList<Value> argumentValues = new ArrayList<Value>();
        for(int x = 0; x < arguments.size(); x++){
            Value t = evaluate(arguments.get(x), scope);
            argumentValues.add(t);
        }

        System.out.println(argumentValues);

        // Call it
        Value value = fValue.call(argumentValues, scope);

        return value;

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

                ArrayList<Token> subExpression = Tokens.sliceBody(tokens, TokenType.OPENBRACKET, i);

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

                Tokens.slice(tokens, i-1, i+1);
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

                // Remove the function declare
                tokens.remove(i);

                // Get function
                Value value = getFunction(tokens, scope);
                String name = ((FunctionValue) value).getName();

                // Add reference to global scope
                scope.global.addVariable(name, value);

                // Add reference to statement
                tokens.add(i, new Token(TokenType.USERFUNC, value));

                i = 0;
            }
        }

    }

    /*
     * Removes tokens inside of a function delcaration and returns them
     */
    private Value getFunction(ArrayList<Token> tokens, ComplexScope scope) throws Exception{

        // Get Parameters
        ArrayList<StringValue> parameters = getParameters(tokens);

        // Get function body
        ArrayList<Token> body = getBody(tokens);

        // Return them as a function
        return new UserFunctionValue(body, parameters, scope);
        
    }

    private ArrayList<StringValue> getParameters(ArrayList<Token> tokens) throws Exception {

        for(int i = 0; i < tokens.size(); i++){

            Token token = tokens.get(i);

            // Look for opening brace
            if(token.getTokenType() == TokenType.OPENBRACKET){

                // Get all braces between
                ArrayList<Token> parameterTokens = Tokens.sliceBody(tokens, TokenType.OPENBRACKET, i);

                // Separate by comma
                ArrayList<ArrayList<Token>> parameterValues = new ArrayList<ArrayList<Token>>();
                ArrayList<StringValue> argumentValues = new ArrayList<StringValue>();
                
                if(!parameterTokens.isEmpty()){
                    parameterValues.add(new ArrayList<Token>());
                    int k = 0;
                    for(int o = 0; o < parameterTokens.size(); o++){

                        if(parameterTokens.get(o).getTokenType() == TokenType.COMMA){
                            parameterValues.add(new ArrayList<Token>());
                            k++;
                        }else{
                            parameterValues.get(k).add(parameterTokens.get(o));
                        }

                    }


                    for(int x = 0; x < parameterValues.size(); x++){
                        if(parameterValues.get(x).size() > 1){
                            throw new Exception("Badly Fomred Parameter List");
                        }

                        try{
                            argumentValues.add((StringValue) parameterValues.get(x).get(0).getTokenValue());
                        }catch(ClassCastException ex){
                            throw new Exception("Expecting STRING found" + parameterValues.get(0).get(0).getTokenValue().getType());
                        }
                    }
                }

                return argumentValues;

            }

        }

        throw new Exception("Did not find parameter list.");

    }

    private ArrayList<Token> getBody(ArrayList<Token> tokens) throws Exception{

        for(int i = 0; i < tokens.size(); i++){

            Token token = tokens.get(i);

            // Look for opening brace
            if(token.getTokenType() == TokenType.OPENBRACES){

                // Get all braces between
                return Tokens.sliceBody(tokens, TokenType.OPENBRACES, i);

            }

        }

        throw new Exception("Could not find function body.");

    }

}

