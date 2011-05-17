package somelanguage.Value;

import somelanguage.Parser.Token.Token;

/**
 *
 * @author tylercarter
 */
public abstract class Value {

    public abstract ValueType getType();

    public String toString(){
        return getType() + "";
    }

    public abstract Token toToken();

}
