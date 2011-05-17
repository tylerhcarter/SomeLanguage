/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package somelanguage.Interpreter.Expressions;

import java.util.ArrayList;
import somelanguage.Variables.Scope;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import somelanguage.Main;
import static org.junit.Assert.*;
import somelanguage.Parser.Token.Token;
import somelanguage.Parser.Token.TokenType;
import somelanguage.Value.IntegerValue;
import somelanguage.Value.Value;
import somelanguage.Variables.ComplexScope;

/**
 *
 * @author tylercarter
 */
public class ExpressionProcessorTest {

    public ExpressionProcessorTest() {
    }

    public ComplexScope getNewScope(){
        return new ComplexScope(new Scope(), new Scope());
    }

    /**
     * Test of evaluate method, of class ExpressionProcessor.
     */
    @Test
    public void testEvaluate_Token_ComplexScope() throws Exception {
        System.out.println("evaluate");
        Token token = new Token(TokenType.INTEGER, new IntegerValue(1));
        ComplexScope scope = getNewScope();
        
        ExpressionProcessor instance = new ExpressionProcessor();
        
        Value expResult = new IntegerValue(1);
        Value result = instance.evaluate(token, scope);
        assertEquals(expResult.toString(), result.toString());
    }

    /**
     * Test of evaluate method, of class ExpressionProcessor.
     */
    @Test
    public void testEvaluate_ArrayList_ComplexScope() throws Exception {
        System.out.println("evaluate");
        ArrayList<Token> tokens = Main.getParser().parse("1;");
        ComplexScope scope = getNewScope();

        ExpressionProcessor instance = new ExpressionProcessor();

        Value expResult = new IntegerValue(1);
        Value result = instance.evaluate(tokens, scope);
        assertEquals(expResult.toString(), result.toString());
    }

}