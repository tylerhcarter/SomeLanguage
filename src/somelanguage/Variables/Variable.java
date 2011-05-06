package somelanguage.Variables;
import somelanguage.Value.IntegerValue;
import somelanguage.Value.NullValue;
import somelanguage.Value.Value;
import somelanguage.Value.StringValue;

/**
 *
 * @author tylercarter
 */
public class Variable {
    private String name;
    private VariableValue value = new VariableValue();

    public Variable(String name) {
        this.name = name;
        this.value.setValue(new NullValue());
    }

    public Variable(String name, int value) {
        this.name = name;
        this.value.setValue(new IntegerValue(value));
    }

    public Variable(String name, String value) {
        this.name = name;
        this.value.setValue(new StringValue(value));
    }

    public Variable(String name, Value value) {
        this.name = name;
        this.value.setValue(value);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the value
     */
    public VariableValue getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value.setValue(new StringValue(value));
    }
    
    /**
     * @param value the value to set
     */
    public void setValue(Value value) {
        this.value.setValue(value);
    }



    public String toString(){
        return this.name + ":" + this.value;
    }

}
