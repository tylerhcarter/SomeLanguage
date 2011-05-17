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
public class CallingCompiler implements Compiler{

    public void process(ArrayList<Token> tokens, ComplexScope scope, ExpressionProcessor processor) throws Exception{

        for(int i = 0; i < tokens.size() - 1; i++){

            Token token = tokens.get(i);

            if(token.getTokenType() == TokenType.CLOSEBRACKET){
                throw new SyntaxException("Unmatched Close Bracket.", tokens);
            }

            else if(token.getTokenType() == TokenType.USERFUNC){

                // Skip if no brackets are afterwards
                if(tokens.size() <= i + 1)
                    continue;
                if(tokens.get(i + 1).getTokenType() != TokenType.OPENBRACKET)
                    continue;

                Value v = token.getTokenValue();

                // Convert Variable Value to FunctionValue
                FunctionValue value;
                try{
                    value = (FunctionValue) v;
                }catch(ClassCastException ex){
                    System.out.println(ex);
                    throw new SyntaxException("Attempted to call a non-function.", tokens);
                }
                
                ArrayList<Token> statement = Tokens.sliceBody(tokens, TokenType.OPENBRACKET, i + 1);
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
                    Value t = processor.evaluate(arguments.get(x), scope);
                    argumentValues.add(t);
                }

                // Call it
                Value returnValue = value.call(argumentValues, scope);

                // Insert Return Value
                tokens.add(i, returnValue.toToken());

            }

            else if(token.getTokenType() == TokenType.STRING
                    && tokens.get(i + 1).getTokenType() == TokenType.OPENBRACKET){

                // Get Variable Name
                String name = token.getTokenValue().toString();

                // Get Variable Value
                Value v = scope.getVariable(name).getValue();

                // Convert Variable Value to FunctionValue
                FunctionValue value;
                try{
                    value = (FunctionValue) v;
                }catch(ClassCastException ex){
                    System.out.println(ex);
                    throw new SyntaxException("Attempted to call a non-function.", tokens);
                }

                ArrayList<Token> statement = Tokens.sliceBody(tokens, TokenType.OPENBRACKET, i + 1);
                tokens.remove(i);

                ArrayList<ArrayList<Token>> arguments = new ArrayList<ArrayList<Token>>();
                ArrayList<Value> argumentValues = new ArrayList<Value>();

                if(statement.size() > 0 ){
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


                    for(int x = 0; x < arguments.size(); x++){
                        Value t = processor.evaluate(arguments.get(x), scope);
                        argumentValues.add(t);
                    }
                }

                // Call it
                Value returnValue = value.call(argumentValues, scope);

                // Insert Return Value
                tokens.add(i, returnValue.toToken());
            }

        }

    }

}
