package somelanguage.Value;

import java.util.ArrayList;

/**
 *
 * @author tylercarter
 */
public class MultiStringValue extends StringValue{

    protected ArrayList<StringValue> strings = new ArrayList<StringValue>();

    public MultiStringValue(String string){
        super(string);
        this.strings.add(new StringValue(string));
    }

    @Override
    public String getValue(){
        return getFull();
    }

    public void add(StringValue string){
        this.strings.add(string);
    }

    public void add(String string){
        this.strings.add(new StringValue(string));
    }

    private String getFull(){
        String output = "";
        for(int i = 0; i < this.strings.size(); i++){

            output += this.strings.get(i).getValue();

        }

        return output;
    }

    @Override
    public ValueType getType() {
        return ValueType.STRING;
    }

    @Override
    public String toString(){
        return getFull();
    }

}
