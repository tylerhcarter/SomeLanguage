package somelanguage.Interpreter;

import somelanguage.Variables.ComplexScope;
import somelanguage.Variables.StackBasedScope;
import somelanguage.Variables.Variable;
import java.util.ArrayList;
import somelanguage.Interpreter.Processor;
import somelanguage.Parser.Token.Token;
import somelanguage.Value.Value;

/**
 * Creates and environment and processes tokens inside of it
 * @author tylercarter
 */
public class Function {
    private final ArrayList<Token> tokens;
    private final Processor runner;
    private final ArrayList<Variable> localScope;

    public Function (Processor runner, ArrayList<Token> tokens, ComplexScope scope){
        
        // Save a copy of the current scope
        this.localScope = scope.local.getVariables();

        this.tokens = tokens;
        this.runner = runner;
    }

    public Value run(ArrayList<Value> arguments, ComplexScope parentScope) throws Exception{

        // Set up environment
        ComplexScope funcScope = createScope(parentScope);

        setArguments(arguments, funcScope);

        // Run Script
        Value value = this.runner.run(this.tokens, funcScope);

        // @todo Discard old complex scope

        return value;
    }

    private ComplexScope createScope(ComplexScope parentScope){

        // Make new scope
        ComplexScope funcScope = new ComplexScope(parentScope.global, new StackBasedScope());

        // Add the localScope
        funcScope.local.addStack(localScope);

        // Add function scope
        funcScope.local.addStack();

        return funcScope;
    }

    private void setArguments(ArrayList<Value> arguments, ComplexScope funcScope){

        // Loop through each argument
        int i = 0;
        for(Value value:arguments){

            // Add argument to scope
            i++;
            funcScope.local.addVariable("argument" + i, value);
            
        }

        // @todo add array of arguments

    }

}
