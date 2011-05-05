package somelanguage.Interpreter;

import somelanguage.Variables.ComplexScope;
import somelanguage.Variables.StackBasedScope;
import somelanguage.Variables.Variable;
import java.util.ArrayList;
import somelanguage.Interpreter.Runner;
import somelanguage.Parser.Token.Token;
import somelanguage.Value.Value;

/**
 *
 * @author tylercarter
 */
public class Function {
    private final ArrayList<Token> tokens;
    private final Runner runner;
    private final ArrayList<Variable> localScope;

    public Function (Runner runner, ArrayList<Token> tokens, ComplexScope scope){
        
        // Save a copy of the current scope
        this.localScope = scope.local.getVariables();

        this.tokens = tokens;
        this.runner = runner;
    }

    public Value run(ArrayList<Value> arguments, ComplexScope parentScope) throws Exception{

        // Set up environemtn
        ComplexScope funcScope = createScope(parentScope);

        setArguments(arguments, funcScope);

        System.out.println("Starting Function " + arguments);

        // Run Script
        Value value = this.runner.run(this.tokens, funcScope);

        // @todo Discard old complex scope

        return value;
    }

    private ComplexScope createScope(ComplexScope parentScope){

        // Make new scope
        ComplexScope funcScope = new ComplexScope("User Function", parentScope.global,
                                                    new StackBasedScope(""));

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
