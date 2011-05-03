package somelanguage.Interpreter;

import java.util.ArrayList;
import somelanguage.Parser.Token;
import somelanguage.Parser.TokenType;
import somelanguage.Scope;
import somelanguage.Value.IntegerValue;
import somelanguage.Value.Value;
import somelanguage.Value.ValueType;

/**
 *
 * @author tylercarter
 */
public class Math {

    public Value evaluate(ArrayList<Token> tokens, Scope scope) throws Exception{
        
        doBrackets(tokens, scope);

        doDivision(tokens, scope);
        doMultiplication(tokens, scope);
        doSubtraction(tokens, scope);
        doAddition(tokens, scope);

        doAssignment(tokens, scope);

        if(tokens.size() > 1){
            System.out.println(tokens);
            throw new Exception("Badly Formed Expression.");
        }

        return getToken(tokens, 0, scope);
    }

    public void doBrackets(ArrayList<Token> expression, Scope scope) throws Exception{

        if(expression.isEmpty())
            return;

        for(int i = 0; i < expression.size(); i++){

            Token token = expression.get(i);
            
            if(token.getTokenType() == TokenType.CLOSEBRACKET){
                throw new Exception("Unmatched Close Bracket.");
            }

            else if(token.getTokenType() == TokenType.OPENBRACKET)
            {
                int closeBracket = getCloseBracket(expression, i);
                if(closeBracket == -1){
                    throw new Exception("Unmatched Open Bracket.");
                }

                ArrayList<Token> subExpression = slice(expression, i, closeBracket);

                // Evaluate the expression and insert it in place of the expression
                expression.add(i, new Token(TokenType.INTEGER, evaluate(subExpression, scope) + ""));
            }

        }

        return;
    }

    public void doAssignment(ArrayList<Token> tokens, Scope scope) throws Exception{

        // Loop through each token
        for(int i = 1; i < tokens.size(); i++){

            Token token = tokens.get(i);

            // Check if this is a divider
            if(token.getTokenType() == TokenType.ADD){

                // Check Left
                if((i - 1) < 0){
                    throw new Exception ("Expected STRING, found ADD");
                }
                String name = tokens.get(i - 1).getTokenValue();

                // Check Right
                if((i + 1) >= tokens.size()){
                    throw new Exception ("Expected INTEGER, found END_STATEMENT");
                }
                Value value = getToken(tokens, i+1, scope);

                // Divide and replace with new token
                scope.setVariable(name, value);

                slice(tokens, i-1, i+1);

                i = 0;

            }

        }

    }

    public void doAddition(ArrayList<Token> tokens, Scope scope) throws Exception{

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
                Token newToken = new Token(TokenType.INTEGER, (int)(numerator + divisor)+"");

                slice(tokens, i-1, i+1);
                tokens.add(i - 1, newToken);

                i = 0;

            }

        }
        
    }

    public void doSubtraction(ArrayList<Token> tokens, Scope scope) throws Exception{

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
                Token newToken = new Token(TokenType.INTEGER, (int)(numerator - divisor)+"");

                slice(tokens, i-1, i+1);
                tokens.add(i - 1, newToken);

                i = 0;

            }

        }

    }

    public void doMultiplication(ArrayList<Token> tokens, Scope scope) throws Exception{

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
                Token newToken = new Token(TokenType.INTEGER, (int)(numerator * divisor)+"");

                slice(tokens, i-1, i+1);
                tokens.add(i - 1, newToken);

                i = 0;

            }

        }


    }

    public void doDivision(ArrayList<Token> tokens, Scope scope) throws Exception{

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
                Token newToken = new Token(TokenType.INTEGER, (int)(numerator / divisor)+"");

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

    private Value getToken(ArrayList<Token> tokens, int i, Scope scope) throws Exception {

        Token token = tokens.get(i);
        if(token.getTokenType() == TokenType.INTEGER){
            return new IntegerValue(Integer.parseInt(token.getTokenValue()));
        }else{

            Value value = scope.getVariable(token.getTokenValue());
            if(value.getType() == ValueType.NULL){
                throw new Exception("Undefined variable " + token.getTokenValue());
            }else{
                return value;
            }

        }

    }

}

