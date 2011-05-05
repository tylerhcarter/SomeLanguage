package somelanguage.Interpreter;

import java.util.ArrayList;
import somelanguage.Variables.ComplexScope;
import somelanguage.Parser.Token.Token;
import somelanguage.Parser.Token.TokenType;
import somelanguage.Value.BooleanValue;
import somelanguage.Value.FunctionValue;
import somelanguage.Value.IntegerValue;
import somelanguage.Value.NullValue;
import somelanguage.Value.UserFunctionValue;
import somelanguage.Value.Value;
import somelanguage.Value.StringValue;
import somelanguage.Value.ValueType;

/**
 *
 * @author tylercarter
 */
public class ExpressionEngine {

    public Value evaluate(ArrayList<Token> tokens, ComplexScope scope) throws Exception{

        if(tokens.isEmpty())
            return new NullValue();

        // Turn functions into values
        doFunctionCompile(tokens, scope);
        
        // Preform function calls
        doFunctionCalls(tokens, scope);
        
        // Math
        doBrackets(tokens, scope);
        
        doDivision(tokens, scope);
        
        doMultiplication(tokens, scope);
        
        doSubtraction(tokens, scope);
        
        doAddition(tokens, scope);

        doAssignment(tokens, scope);
        

        if(tokens.size() > 1){
            throw new Exception("Badly Formed Expression.");
        }

        return getToken(tokens, 0, scope);
    }

    public void doFunctionCalls(ArrayList<Token> tokens, ComplexScope scope) throws Exception{

        if(tokens.isEmpty())
            return;

        for(int i = 0; i < tokens.size() - 1; i++){

            Token token = tokens.get(i);

            if(token.getTokenType() == TokenType.CLOSEBRACKET){
                throw new Exception("Unmatched Close Bracket.");
            }

            else if(token.getTokenType() == TokenType.STRING
                    && tokens.get(i + 1).getTokenType() == TokenType.OPENBRACKET){

                String name = token.getTokenValue().toString();
                Value v = scope.getVariable(name);

                try{

                    FunctionValue value = (FunctionValue) v;

                }catch(ClassCastException ex){

                    System.out.println(ex);
                    throw new Exception("Attempted to call a non-function.");
                    
                }

                FunctionValue value = (FunctionValue) v;

                // Find Close Brace
                int closeBracket = getCloseBracket(tokens, i + 1);
                if(closeBracket == -1){
                    throw new Exception("Unmatched Open Bracket.");
                }

                // Get Arguments
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

    public void doBrackets(ArrayList<Token> tokens, ComplexScope scope) throws Exception{

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

    public void doAssignment(ArrayList<Token> tokens, ComplexScope scope) throws Exception{

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

    public void doAddition(ArrayList<Token> tokens, ComplexScope scope) throws Exception{

        // Loop through each token
        for(int i = 1; i < tokens.size(); i++){

            Token token = tokens.get(i);

            // Check if this is a divider
            if(token.getTokenType() == TokenType.ADD){

                // Check Left
                if((i - 1) < 0){
                    throw new Exception ("Expected INTEGER, found ADD");
                }
                int numerator = ((IntegerValue)getToken(tokens, i-1, scope)).getValue();

                // Check Right
                if((i + 1) >= tokens.size()){
                    throw new Exception ("Expected INTEGER, found END_STATEMENT");
                }
                int divisor = ((IntegerValue)getToken(tokens, i+1, scope)).getValue();

                // Divide and replace with new token
                Token newToken = new Token(TokenType.INTEGER, new IntegerValue((int)(numerator + divisor)));

                slice(tokens, i-1, i+1);
                tokens.add(i - 1, newToken);

                i = 0;

            }

        }
        
    }

    public void doSubtraction(ArrayList<Token> tokens, ComplexScope scope) throws Exception{

        // Loop through each token
        for(int i = 1; i < tokens.size(); i++){

            Token token = tokens.get(i);

            // Check if this is a divider
            if(token.getTokenType() == TokenType.SUBTRACT){

                // Check Left
                if((i - 1) < 0){
                    throw new Exception ("Expected INTEGER, found SUBTRACT");
                }
                int numerator = ((IntegerValue)getToken(tokens, i-1, scope)).getValue();

                // Check Right
                if((i + 1) >= tokens.size()){
                    throw new Exception ("Expected INTEGER, found END_STATEMENT");
                }
                int divisor = ((IntegerValue)getToken(tokens, i+1, scope)).getValue();

                // Divide and replace with new token
                Token newToken = new Token(TokenType.INTEGER, new IntegerValue((int)(numerator - divisor)));

                slice(tokens, i-1, i+1);
                tokens.add(i - 1, newToken);

                i = 0;

            }

        }

    }

    public void doMultiplication(ArrayList<Token> tokens, ComplexScope scope) throws Exception{

        // Loop through each token
        for(int i = 1; i < tokens.size(); i++){

            Token token = tokens.get(i);

            // Check if this is a divider
            if(token.getTokenType() == TokenType.MULTIPLY){

                // Check Left
                if((i - 1) < 0){
                    throw new Exception ("Expected INTEGER, found MULTIPLY");
                }
                int numerator = ((IntegerValue)getToken(tokens, i-1, scope)).getValue();

                // Check Right
                if((i + 1) >= tokens.size()){
                    throw new Exception ("Expected INTEGER, found END_STATEMENT");
                }
                int divisor = ((IntegerValue)getToken(tokens, i+1, scope)).getValue();

                // Divide and replace with new token
                Token newToken = new Token(TokenType.INTEGER, new IntegerValue((int)(numerator * divisor)));

                slice(tokens, i-1, i+1);
                tokens.add(i - 1, newToken);

                i = 0;

            }

        }


    }

    public void doDivision(ArrayList<Token> tokens, ComplexScope scope) throws Exception{

        // Loop through each token
        for(int i = 1; i < tokens.size(); i++){

            Token token = tokens.get(i);

            // Check if this is a divider
            if(token.getTokenType() == TokenType.DIVIDE){

                // Check Left
                if((i - 1) < 0){
                    throw new Exception ("Expected INTEGER, found DIVIDE");
                }
                int numerator = ((IntegerValue)getToken(tokens, i-1, scope)).getValue();

                // Check Right
                if((i + 1) >= tokens.size()){
                    throw new Exception ("Expected INTEGER, found END_STATEMENT");
                }
                int divisor = ((IntegerValue)getToken(tokens, i+1, scope)).getValue();

                // Divide and replace with new token
                Token newToken = new Token(TokenType.INTEGER, new IntegerValue((int)(numerator / divisor)));

                slice(tokens, i-1, i+1);
                tokens.add(i - 1, newToken);

                i = 0;

            }

        }

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

    private void doFunctionCompile(ArrayList<Token> tokens, ComplexScope scope) throws Exception {
        
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

    public Value getFunction(ArrayList<Token> tokens, ComplexScope scope) throws Exception{

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

    private Value getToken(ArrayList<Token> tokens, int i, ComplexScope scope) throws Exception {
        Token token = tokens.get(i);

        if(token.getTokenType() == TokenType.STRING){
            Value value = scope.getVariable(((StringValue) token.getTokenValue()).toString());            
            return value;

        }else{
            return token.getTokenValue();
        }
    }

}

