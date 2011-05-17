package somelanguage.Value;

import java.util.ArrayList;
import somelanguage.Parser.Token.Token;
import somelanguage.Parser.Token.TokenType;

/**
 *
 * @author Tyler(Chacha) chacha@chacha102.com
 */
public class ObjectValue extends Value{

    private ArrayList<Property> properties = new ArrayList<Property>();

    private class Property{
        private String name;
        private Value value;

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
        public void setValue(Value value) {
            this.value = value;
        }

        public String toString(){
            return this.name + ":" + this.value;
        }
    }

    public void addValue(String name, Value value){

        Property p = new Property();
        p.setName(name);
        p.setValue(value);

        this.properties.add(p);

    }

    public void setValue(String name, Value value){

        for(Property p:this.properties){
            if(p.name.equals(name)){
                p.value = value;
            }
        }

        addValue(name, value);
    }

    public Value getValue(String name){

        for(Property p:this.properties){
            if(p.name.equals(name)){
                return p.value;
            }
        }

        return null;

    }

    public void removeValue(String name){
        for(int i = 0; i <  this.properties.size(); i++){
            if(this.properties.get(i).name.equals(name)){
                this.properties.remove(i);
            }
        }
    }

    @Override
    public ValueType getType() {
        return ValueType.OBJECT;
    }

    @Override
    public Token toToken() {
        return new Token(TokenType.OBJECT, this);
    }

    public String toString(){
        return this.properties.toString();
    }

}
