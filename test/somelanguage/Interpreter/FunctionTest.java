package somelanguage.Interpreter;

import java.util.ArrayList;
import org.junit.Test;
import somelanguage.Interpreter.Constructs.Construct;
import somelanguage.Interpreter.Constructs.Return;
import somelanguage.Interpreter.Expressions.ExpressionProcessor;
import somelanguage.Main;
import static org.junit.Assert.*;
import somelanguage.Value.IntegerValue;
import somelanguage.Value.Value;
import somelanguage.Variables.ComplexScope;

/**
 *
 * @author tylercarter
 */
public class FunctionTest {
    
    private final ExpressionProcessor expression;
    private final Processor processor;

    public FunctionTest() {

        // Make Processor
        this.expression = new ExpressionProcessor();
        ArrayList<Construct> constructs = new ArrayList<Construct>();
        constructs.add(new Return());
        this.processor = new Processor(this.expression, constructs);

    }

    /**
     * Test of run method, of class Function.
     */
    @Test
    public void testRun() throws Exception {

        System.out.println("run");
        ComplexScope parentScope = Main.getScope();
        Function instance = new Function(this.processor, Main.getParser().parse("return 1;"), parentScope);
        
        Value expResult = new IntegerValue(1);
        Value result = instance.run(parentScope);
        assertEquals(expResult.toString(), result.toString());
        
    }

}