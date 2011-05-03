package somelanguage.Value;

import java.util.ArrayList;
import somelanguage.Parser.Token;

/**
 *
 * @author tylercarter
 */
public class FunctionValue extends Value{
    private final ArrayList<Token> tokens;

    public FunctionValue(ArrayList<Token> value){
        this.tokens = value;
    }

    public Value call() throws Exception{
        throw new Exception("Not Implemented Yet");
    }

    @Override
    public ValueType getType() {
        return ValueType.FUNCTION;
    }

}
