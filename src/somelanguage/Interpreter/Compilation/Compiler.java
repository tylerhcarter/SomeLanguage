/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package somelanguage.Interpreter.Compilation;

import java.util.ArrayList;
import somelanguage.Interpreter.Expressions.ExpressionProcessor;
import somelanguage.Parser.Token.Token;
import somelanguage.Variables.ComplexScope;

/**
 *
 * @author tylercarter
 */
public interface Compiler {

    void compile(ArrayList<Token> tokens, ComplexScope scope, ExpressionProcessor processor) throws Exception;

}
