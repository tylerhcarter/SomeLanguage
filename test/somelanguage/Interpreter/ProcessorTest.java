/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package somelanguage.Interpreter;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import somelanguage.Main;
import static org.junit.Assert.*;
import somelanguage.Parser.Token.Token;
import somelanguage.Value.IntegerValue;
import somelanguage.Value.Value;
import somelanguage.Variables.ComplexScope;
import somelanguage.Variables.Scope;

/**
 *
 * @author tylercarter
 */
public class ProcessorTest {
    private final ArrayList<Token> tokens;

    public ProcessorTest() {
        somelanguage.Parser.Parser parser = new somelanguage.Parser.Parser(Main.getParserConfig());
        this.tokens = parser.parse("return 1;");
    }

    public ComplexScope getNewScope(){
        return new ComplexScope(new Scope(), new Scope());
    }

    /**
     * Test of run method, of class Processor.
     */
    @Test
    public void testRun() throws Exception {

        System.out.println("run");
        
        ArrayList<Token> tokens = this.tokens;
        ComplexScope scope = this.getNewScope();
        Processor instance = Main.getProcessor();
        
        Value expResult = new IntegerValue(1);
        Value result = instance.run(tokens, scope);
        assertEquals(expResult.toString(), result.toString());
        
    }

    /**
     * Test of runFunction method, of class Processor.
     */
    @Test
    public void testRunFunction() throws Exception {
        
        System.out.println("runFunction");

        ArrayList<Token> tokens = this.tokens;
        ComplexScope scope = this.getNewScope();
        Processor instance = Main.getProcessor();

        Value expResult = new IntegerValue(1);
        Value result = instance.runFunction(tokens, scope);
        assertEquals(expResult.toString(), result.toString());
        
    }

    /**
     * Test of evaluateOperation method, of class Processor.
     */
    @Test
    public void testEvaluateOperation() throws Exception {
        
        System.out.println("evaluateOperation");
        ArrayList<Token> tokens = this.tokens;
        ComplexScope scope = this.getNewScope();
        Processor instance = Main.getProcessor();

        Value expResult = new IntegerValue(1);
        Value result = instance.runFunction(tokens, scope);
        assertEquals(expResult.toString(), result.toString());
    }

}