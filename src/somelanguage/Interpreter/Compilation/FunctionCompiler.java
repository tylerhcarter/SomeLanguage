package somelanguage.Interpreter.Compilation;

import java.util.ArrayList;
import somelanguage.Interpreter.Expressions.ExpressionProcessor;
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
    public void compile(ArrayList<Token> tokens, ComplexScope scope, ExpressionProcessor processor) throws Exception {

        for(int i = 0; i < tokens.size(); i++){
            Token token = tokens.get(i);
            System.out.println(token);
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

                ArrayList<StringValue> argumentValues = new ArrayList<StringValue>();
                if(!parameterValues.isEmpty()){
                    for(int x = 0; x < parameterValues.size(); x++){
                        if(parameterValues.get(x).size() == 0){
                            continue;
                        }

                        if(parameterValues.get(x).size() > 1){
                            throw new Exception("Badly Formed Parameter List");
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
