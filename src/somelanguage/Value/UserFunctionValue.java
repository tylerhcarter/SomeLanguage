package somelanguage.Value;

import java.util.ArrayList;
import somelanguage.Variables.ComplexScope;
import somelanguage.Interpreter.Function;
import somelanguage.Main;
import somelanguage.Parser.Token.Token;
import somelanguage.Parser.Token.TokenType;

/**
 *
 * @author tylercarter
 */
public class UserFunctionValue extends FunctionValue{
    public final Function script;
    private final ArrayList<StringValue> parameters;

    public UserFunctionValue(ArrayList<Token> tokens, ArrayList<StringValue> parameters, ComplexScope scope){

        // Create function to run later
        this.script = new Function(Main.runner, tokens, scope);

        this.parameters = parameters;
        
    }

    public Value call(ArrayList<Value> arguments, ComplexScope parentScope) throws Exception{

        augmentScope(arguments, parentScope);

        // Run function and return value
        Value value = script.run(parentScope);
        return value;
        
    }

    private void augmentScope(ArrayList<Value> arguments, ComplexScope scope) throws Exception{

        for(int i = 0; i < parameters.size(); i++){

            StringValue name = parameters.get(i);

            if(arguments.size() <= i){
                throw new Exception("Function call requires " + parameters.size() + " arguments. Only " + arguments.size() + " given.");
            }
            scope.local.addVariable(name.toString(), arguments.get(i));
        }

    }

    @Override
    public ValueType getType() {
        return ValueType.FUNCTION;
    }

    @Override
    public Token toToken() {
        return new Token(TokenType.USERFUNC, this);
    }

}
