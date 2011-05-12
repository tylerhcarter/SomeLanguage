package somelanguage.Interpreter.Functions;

import java.util.ArrayList;
import somelanguage.Variables.ComplexScope;
import somelanguage.Value.DefinedFunctionValue;
import somelanguage.Value.NullValue;
import somelanguage.Value.Value;

/**
 *
 * @author tylercarter
 */
public class Echo extends DefinedFunctionValue{

    @Override
    public Value call(ArrayList<Value> arguments, ComplexScope scope) throws Exception {

        for(Value value:arguments){
            System.out.println(value.toString());
        }

        return new NullValue();
    }

}
