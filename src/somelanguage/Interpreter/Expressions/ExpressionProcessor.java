package somelanguage.Interpreter.Expressions;

import java.util.ArrayList;
import somelanguage.Interpreter.Compilation.BracketCompiler;
import somelanguage.Interpreter.Compilation.CallingCompiler;
import somelanguage.Interpreter.Compilation.FunctionCompiler;
import somelanguage.Interpreter.Compilation.Compiler;
import somelanguage.Interpreter.Compilation.ObjectCompiler;
import somelanguage.Interpreter.Compilation.ReferenceCompiler;
import somelanguage.Interpreter.SyntaxException;
import somelanguage.Variables.ComplexScope;
import somelanguage.Parser.Token.*;
import somelanguage.Value.*;
import somelanguage.Variables.VariableValue;

/**
 * Processes a list of tokens and evaluates them to a single value
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
        this.compilers.add(new ObjectCompiler());
        this.compilers.add(new ReferenceCompiler());
        this.compilers.add(new BracketCompiler());
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

        if(tokens.isEmpty()) return new NullValue();

        // Do Compile Processes
        for(Compiler compiler:this.compilers)
            compiler.process(tokens, scope, this);
        
        // Evaluate Expression
        for(MathOperation op:this.operations)
            op.process(tokens, scope);

        // Assign variables
        doAssignment(tokens, scope);
        
        // Get rid of excess end statements
        cleanEndStatements(tokens);

        // Check that we only have one token left
        if(tokens.size() > 1){
            throw new SyntaxException("Badly Formed Expression.", tokens);
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
     * Searchs for assignments and preforms them
     */
    private void doAssignment(ArrayList<Token> tokens, ComplexScope scope) throws Exception{

        // Loop through each token
        for(int i = 1; i < tokens.size(); i++){

            Token token = tokens.get(i);

            // Check if this is a divider
            if(token.getTokenType() == TokenType.ASSIGNMENT){

                // Check Left for variable container
                if((i - 1) < 0){
                    throw new SyntaxException ("Expected STRING, found ASSIGNMENT", tokens);
                }
                
                VariableValue variable = (VariableValue) tokens.get(i - 1).getTokenValue();

                // Check Right
                if((i + 1) >= tokens.size()){
                    throw new SyntaxException ("Expected INTEGER, found END_STATEMENT", tokens);
                }

                Value value = getToken(tokens, i+1, scope);

                // Divide and replace with new token
                variable.setValue(value);

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

