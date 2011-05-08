package somelanguage.Interpreter;

import somelanguage.Variables.ComplexScope;
import java.util.ArrayList;
import somelanguage.Parser.Token.Token;
import somelanguage.Value.Value;
import somelanguage.Variables.Scope;

/**
 * Creates and environment and processes tokens inside of it
 * @author tylercarter
 */
public class Function {
    private final ArrayList<Token> tokens;
    private final Processor runner;
    private final Scope localScope;

    public Function (Processor runner, ArrayList<Token> tokens, ComplexScope scope){
        
        // Save a copy of the current scope
        this.localScope = scope.local;

        this.tokens = tokens;
        this.runner = runner;
    }

    public Value run(ComplexScope parentScope) throws Exception{

        // Set up environment
        ComplexScope funcScope = createScope(parentScope);

        // Run Script
        Value value = this.runner.run(this.tokens, funcScope);

        // @todo Discard old complex scope

        return value;
    }

    private ComplexScope createScope(ComplexScope parentScope){

        // Make new scope
        ComplexScope funcScope = new ComplexScope(parentScope.global, this.localScope);

        return funcScope;
    }

}
