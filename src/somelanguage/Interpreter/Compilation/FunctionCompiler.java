package somelanguage.Interpreter.Compilation;

import java.util.ArrayList;
import somelanguage.Interpreter.Expressions.ExpressionProcessor;
import somelanguage.Interpreter.SyntaxException;
import somelanguage.Variables.ComplexScope;
import somelanguage.Parser.Token.*;
import somelanguage.Value.*;

/**
 *
 * @author tylercarter
 */
public class FunctionCompiler implements Compiler {

    /*
     * Converts function declarations into function references
     */
    public void process(ArrayList<Token> tokens, ComplexScope scope, ExpressionProcessor processor) throws Exception {

        for(int i = 0; i < tokens.size(); i++){
            Token token = tokens.get(i);
            if(token.getTokenType() == TokenType.FUNCTION_DECLARE){

                // Remove the function declare
                tokens.remove(i);

                // Get function
                FunctionValue value = getFunction(tokens, scope);
                
                // Add reference to statement
                tokens.add(i, new Token(TokenType.USERFUNC, value));

                i = 0;
            }
        }

    }

    /*
     * Removes tokens inside of a function delcaration and returns them
     */
    private FunctionValue getFunction(ArrayList<Token> tokens, ComplexScope scope) throws Exception{

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

                ArrayList<ArrayList<Token>> values = parseParameters(tokens, i);

                ArrayList<StringValue> names = new ArrayList<StringValue>();
                if(!values.isEmpty()){
                    for(int x = 0; x < values.size(); x++){

                        if(values.get(x).isEmpty())
                            throw new SyntaxException("Badly Formed Parameter List. Expecting Parameter Name.", tokens);

                        if(values.get(x).size() > 1)
                            throw new SyntaxException("Badly Formed Parameter List. Expecting , or ).", tokens);

                        Value value = values.get(x).get(0).getTokenValue();

                        // Try to convert to a string value
                        try{
                            StringValue name = new StringValue( value.toString() );
                            names.add(name);
                        }catch(ClassCastException ex){
                            throw new SyntaxException("Expecting STRING found" + value.getType(), tokens);
                        }
                        
                    }

                }
                return names;
            }
        }
        throw new Exception("Did not find parameter list.");

    }

    private ArrayList<ArrayList<Token>> parseParameters(ArrayList<Token> tokens, int index) throws Exception{

        ArrayList<Token> pTokens = Tokens.sliceBody(tokens, TokenType.OPENBRACKET, index);

        ArrayList<ArrayList<Token>> parameterValues = new ArrayList<ArrayList<Token>>();

        // Separate tokens by comma
        parameterValues.add(new ArrayList<Token>());
        int parameterCount = 0;
        for(int i = 0; i < pTokens.size(); i++){
            Token token = pTokens.get(i);

            if(token.getTokenType() == TokenType.COMMA){
                parameterCount++;
                parameterValues.add(new ArrayList<Token>());
                continue;
            }

            parameterValues.get(parameterCount).add(pTokens.get(i));

        }

        return parameterValues;
    }

    private ArrayList<Token> getBody(ArrayList<Token> tokens) throws Exception{

        for(int i = 0; i < tokens.size(); i++){
            
            Token token = tokens.get(i);
            // Look for opening brace
            if(token.getTokenType() == TokenType.OPENBRACES)
                return Tokens.sliceBody(tokens, TokenType.OPENBRACES, i);
            
        }
        throw new Exception("Could not find function body.");
    }

}
