package somelanguage.Value;

import java.util.ArrayList;

/**
 *
 * @author tylercarter
 */
public abstract class DefinedFunctionValue extends FunctionValue{

    public abstract Value call(ArrayList<Value> arguments) throws Exception;

}
