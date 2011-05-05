package somelanguage.Value;

import java.util.ArrayList;
import somelanguage.Variables.ComplexScope;

/**
 *
 * @author tylercarter
 */
public abstract class DefinedFunctionValue extends FunctionValue{

    public abstract Value call(ArrayList<Value> arguments, ComplexScope scope) throws Exception;

}
