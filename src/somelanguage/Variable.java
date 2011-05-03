package somelanguage;
import somelanguage.Value.IntegerValue;
import somelanguage.Value.NullValue;
import somelanguage.Value.Value;
import somelanguage.Value.StringValue;

/**
 *
 * @author tylercarter
 */
class Variable {
    private String name;
    private Value value;

    public Variable(String name) {
        this.name = name;
        this.value = new NullValue();
    }

    public Variable(String name, int value) {
        this.name = name;
        this.value = new IntegerValue(value);
    }

    public Variable(String name, String value) {
        this.name = name;
        this.value = new StringValue(value);
    }

    public Variable(String name, Value value) {
        this.name = name;
        this.value = value;
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
    public Value getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = new StringValue(value);
    }
    
    /**
     * @param value the value to set
     */
    public void setValue(Value value) {
        this.value = value;
    }



    public String toString(){
        return this.name + ":" + this.value;
    }

}
