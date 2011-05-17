package somelanguage.Interpreter.Compilation;

import java.util.ArrayList;
import somelanguage.Interpreter.Expressions.ExpressionProcessor;
import somelanguage.Interpreter.SyntaxException;
import somelanguage.Variables.ComplexScope;
import somelanguage.Parser.Token.*;
import somelanguage.Value.*;
import somelanguage.Variables.VariableValue;

/**
 *
 * @author tylercarter
 */
public class ReferenceCompiler implements Compiler {

    /*
     * Converts function declarations into function references
     */
    public void process(ArrayList<Token> tokens, ComplexScope scope, ExpressionProcessor processor) throws Exception {

        for(int i = 0; i < tokens.size(); i++){
            Token token = tokens.get(i);
            if(token.getTokenType() == TokenType.OBJECT){

                if(tokens.size() <= i + 1)
                    continue;
                
                if(tokens.get(i + 1).getTokenType() == TokenType.DOT){
                    
                    // Next should be a string
                    if(tokens.get(i + 2).getTokenType() != TokenType.STRING){
                        throw new SyntaxException("Expecting String Indentifier.", tokens);
                    }

                    String identifier = tokens.get(i + 2).getTokenValue().toString();

                    // Get Variable Container
                    VariableValue container = (VariableValue) token.getTokenValue();

                    // Get Object
                    ObjectValue object = (ObjectValue) container.getValue();

                    // Get Object Member
                    Value value = object.getValue(identifier);

                    // Replace
                    tokens.remove(i);
                    tokens.remove(i);
                    tokens.remove(i);
                    tokens.add(i, value.toToken());

                    i = 0;
                }
                

            }
            else if(token.getTokenType() == TokenType.STRING){

                // Get variable container
                Value value = scope.getVariable(token.toString());
                if(value == null){
                    scope.local.addVariable(token.toString());
                    value = scope.local.getVariable(token.toString());
                }

                // Replace with Variable Container
                tokens.remove(i);
                tokens.add(i, value.toToken());

                // Reprocess
                process(tokens, scope, processor);
            }
        }

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

    private ObjectValue getObject(ArrayList<Token> tokens) throws SyntaxException {

        // First break stuff up by commas
        ArrayList<ArrayList<Token>> fields = separate(tokens, TokenType.COMMA);

        // Create the new object
        ObjectValue object = new ObjectValue();

        // Go through each value
        for(ArrayList<Token> field:fields){
            proccessField(field, object);
        }

        return object;
    }

    private ArrayList<ArrayList<Token>> separate(ArrayList<Token> tokens, TokenType divide){

        ArrayList<ArrayList<Token>> result = new ArrayList<ArrayList<Token>>();

        result.add(new ArrayList<Token>());
        int i = 0;
        for(Token token:tokens){
            if(token.getTokenType() == divide){
                result.add(new ArrayList<Token>());
                i++;
            }else{
                result.get(i).add(token);
            }
        }

        return result;

    }

    private void proccessField(ArrayList<Token> field, ObjectValue object) throws SyntaxException {

        Tokens.cleanEndStatements(field);

        // First we need a string
        String name;
        if(field.get(0).getTokenType() != TokenType.STRING && field.get(0).getTokenType() != TokenType.ENCAPSULATED_STRING){
            throw new SyntaxException("Expected String.", field);
        }

        name = field.get(0).toString();

        // Next we need a colon
        if(field.get(1).getTokenType() != TokenType.COLON){
            throw new SyntaxException("Expected Colon.", field);
        }

        // Finally, the last piece is value
        Value value = field.get(2).getTokenValue();

        object.addValue(name, value);
    }

}
