/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package somelanguage.Interpreter.Constructs;

import java.util.ArrayList;
import somelanguage.Interpreter.Processor;
import somelanguage.Parser.Token.Token;
import somelanguage.Parser.Token.TokenType;
import somelanguage.Value.Value;
import somelanguage.Variables.ComplexScope;

/**
 *
 * @author tylercarter
 */
public class LocalDeclare extends Construct{

    public TokenType getToken(){
        return TokenType.LOCAL_DECLARE;
    }

    @Override
    public Value process(Processor processor, ArrayList<Token> tokens, ComplexScope scope) throws Exception{

        // Declare variable
        processDeclaration(tokens, scope);

        // Remove declaration token
        tokens.remove(0);

        // Evaulate
        return processor.evaluateOperation(tokens, scope);
        
    }

    private void processDeclaration(ArrayList<Token> tokens, ComplexScope scope) throws Exception{
        Token name = tokens.get(1);
        declareVariable(name.getTokenValue().toString(), scope, true);
    }

    private void declareVariable(String name, ComplexScope scope, boolean local) throws Exception{
        scope.getLocal().addVariable(name);
    }

}
