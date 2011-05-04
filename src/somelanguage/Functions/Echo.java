package somelanguage.Functions;

import java.util.ArrayList;
import somelanguage.Value.DefinedFunctionValue;
import somelanguage.Value.NullValue;
import somelanguage.Value.Value;

/**
 *
 * @author tylercarter
 */
public class Echo extends DefinedFunctionValue{

    @Override
    public Value call(ArrayList<Value> arguments) throws Exception {

        for(Value value:arguments){
            System.out.println("Printing: " + value);
        }

        return new NullValue();
    }

}
