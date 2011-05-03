package somelanguage.Value;

/**
 *
 * @author tylercarter
 */
public abstract class Value {

    public abstract ValueType getType();

    public String toString(){
        return getType() + "";
    }

}
