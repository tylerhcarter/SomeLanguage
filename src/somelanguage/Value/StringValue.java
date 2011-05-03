package somelanguage.Value;

/**
 *
 * @author tylercarter
 */
public class StringValue extends Value{
    private final String value;

    public StringValue(String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }

    @Override
    public ValueType getType() {
        return ValueType.STRING;
    }

    public String toString(){
        return this.value;
    }

}
