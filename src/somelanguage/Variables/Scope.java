package somelanguage.Variables;

import somelanguage.Value.NullValue;
import somelanguage.Value.Value;

/**
 *
 * @author tylercarter
 */
public class Scope {

    protected Dictionary<Value> variables = new Dictionary<Value>();

    public void addVariable(String name) {
        this.variables.set(name, new NullValue());
    }

    public void addVariable(String name, Value value){

        Value previousValue = this.variables.get(name);
        if(previousValue != null){
            System.out.println("Warning: Redeclaring local variable " + name + ".");
        }

        this.variables.set(name, value);
        
    }

    public Value getVariable(String name){

        Value value = this.variables.get(name);
        if(value == null){
            System.out.println("Warning: Using local variable " + name + " without declaration.");
        }

        return value;
    }

    public Value setVariable(String name, Value value){

        Value previousValue = this.variables.get(name);
        if(previousValue == null){
            System.out.println("Warning: Using local variable " + name + " without declaration.");
        }
        
        this.variables.set(name, value);
        return previousValue;
    }

    public Value deleteVariable(String name){

        Value previousValue = this.variables.get(name);
        if(previousValue == null){
            System.out.println("Warning: Using local variable " + name + " without declaration.");
        }

        this.variables.remove(name);
        return previousValue;
    }


    public String toString(){

        String output = variables.toString();
        return output;

    }

    public Dictionary<Value> getVariables(){
        return variables;
    }

}
