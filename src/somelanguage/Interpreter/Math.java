package somelanguage.Interpreter;

import java.util.ArrayList;
import somelanguage.Parser.Token;
import somelanguage.Parser.TokenType;
import somelanguage.Scope;

/**
 *
 * @author tylercarter
 */
public class Math {

    public int evaluate(ArrayList<Token> tokens, Scope scope) throws Exception{
        
        System.out.println(tokens);
        doBrackets(tokens, scope);

        doDivision(tokens, scope);
        doMultiplication(tokens, scope);
        doSubtraction(tokens, scope);
        doAddition(tokens, scope);

        if(tokens.size() > 1){
            throw new Exception("Badly Formed Expression.");
        }

        return Integer.parseInt(tokens.get(0).getTokenValue());
    }

    public int doBrackets(ArrayList<Token> expression, Scope scope) throws Exception{

        if(expression.isEmpty())
            return 0;

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
                System.out.println(expression);
            }

        }

        return 0;
    }

    public void doAddition(ArrayList<Token> tokens, Scope scope) throws Exception{

        // Loop through each token
        for(int i = 1; i < tokens.size(); i++){

            System.out.println(tokens);

            Token token = tokens.get(i);

            // Check if this is a divider
            if(token.getTokenType() == TokenType.ADD){

                System.out.println("Preforming");

                // Check Left
                if((i - 1) < 0){
                    throw new Exception ("Expected INTEGER, found ADD");
                }
                int numerator = getToken(tokens, i-1, scope);

                // Check Right
                if((i + 1) >= tokens.size()){
                    throw new Exception ("Expected INTEGER, found END_STATEMENT");
                }
                int divisor = getToken(tokens, i+1, scope);

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

            System.out.println(tokens);

            Token token = tokens.get(i);

            // Check if this is a divider
            if(token.getTokenType() == TokenType.SUBTRACT){

                System.out.println("Preforming");

                // Check Left
                if((i - 1) < 0){
                    throw new Exception ("Expected INTEGER, found SUBTRACT");
                }
                int numerator = getToken(tokens, i-1, scope);

                // Check Right
                if((i + 1) >= tokens.size()){
                    throw new Exception ("Expected INTEGER, found END_STATEMENT");
                }
                int divisor = getToken(tokens, i+1, scope);

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

            System.out.println(tokens);

            Token token = tokens.get(i);

            // Check if this is a divider
            if(token.getTokenType() == TokenType.MULTIPLY){

                System.out.println("Preforming");

                // Check Left
                if((i - 1) < 0){
                    throw new Exception ("Expected INTEGER, found MULTIPLY");
                }
                int numerator = getToken(tokens, i-1, scope);

                // Check Right
                if((i + 1) >= tokens.size()){
                    throw new Exception ("Expected INTEGER, found END_STATEMENT");
                }
                int divisor = getToken(tokens, i+1, scope);

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

            System.out.println(tokens);

            Token token = tokens.get(i);

            // Check if this is a divider
            if(token.getTokenType() == TokenType.DIVIDE){

                System.out.println("Preforming");

                // Check Left
                if((i - 1) < 0){
                    throw new Exception ("Expected INTEGER, found DIVIDE");
                }
                int numerator = getToken(tokens, i-1, scope);

                // Check Right
                if((i + 1) >= tokens.size()){
                    throw new Exception ("Expected INTEGER, found END_STATEMENT");
                }
                int divisor = getToken(tokens, i+1, scope);

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

    private int getToken(ArrayList<Token> tokens, int i, Scope scope) throws Exception {

        Token token = tokens.get(i);
        if(token.getTokenType() == TokenType.INTEGER){
            return Integer.parseInt(token.getTokenValue());
        }else{

            String value = scope.getVariable(token.getTokenValue());
            if(value.equals("undefined")){
                throw new Exception("Undefined variable " + token.getTokenValue());
            }else{
                return Integer.parseInt(value);
            }

        }

    }

}

