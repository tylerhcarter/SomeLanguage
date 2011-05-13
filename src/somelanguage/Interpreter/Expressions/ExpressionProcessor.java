package somelanguage.Interpreter.Expressions;

import java.util.ArrayList;
import somelanguage.Interpreter.Compilation.CallingCompiler;
import somelanguage.Interpreter.Compilation.FunctionCompiler;
import somelanguage.Interpreter.Compilation.Compiler;
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
    private ArrayList<Compiler> compilers = new ArrayList<Compiler>();

    public ExpressionProcessor(){

        // Add Math Operations
        this.operations.add(new Add(this));
        this.operations.add(new Subtract(this));
        this.operations.add(new Multiply(this));
        this.operations.add(new Divide(this));

        this.operations.add(new Equality(this));
        this.operations.add(new And(this));
        this.operations.add(new Or(this));

        // Add Compiler Operations
        this.compilers.add(new FunctionCompiler());
        this.compilers.add(new CallingCompiler());
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

        // Do Compile
        for(Compiler compiler:this.compilers){
            compiler.compile(tokens, scope, this);
        }
        
        // Math
        doBrackets(tokens, scope);

        for(MathOperation op:this.operations){
            op.doOperation(tokens, scope);
        }
        
        doAssignment(tokens, scope);

        // Get rid of excess end statements
        cleanEndStatements(tokens);

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
            Value value = scope.getVariable(((StringValue) token.getTokenValue()).toString()).getValue();
            return value;

        }else{
            return token.getTokenValue();
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

     

    private void cleanEndStatements(ArrayList<Token> tokens) {
        for(int i = 0; i < tokens.size(); i++){
            if(tokens.get(i).getTokenType() == TokenType.END_STATEMENT){
                tokens.remove(i);
                i = 0;
            }
        }
    }

}

