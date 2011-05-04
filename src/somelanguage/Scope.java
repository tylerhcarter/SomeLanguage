package somelanguage;

import java.util.ArrayList;
import somelanguage.Value.NullValue;
import somelanguage.Value.UndefinedValue;
import somelanguage.Value.Value;

/**
 *
 * @author tylercarter
 */
public class Scope {

    protected ArrayList<Variable> variables = new ArrayList<Variable>();
    
    public Scope(){
        
    }

    public void addVariable(String name) {

        addVariable(name, new NullValue());
        
    }

    public void addVariable(String name, Value value){

        int index = findVariable(name);
        if(index == -1){
            this.variables.add(new Variable(name, value));
        }
        else{
            System.out.println("Warning: Redeclaring Local Variable "+ name);
            
            Variable object = this.variables.get(index);
            object.setValue(value);
        }
        
    }

    public void setVariable(String name, Value value){

        int index = findVariable(name);
        if(index == -1){
            System.out.println("Warning: Using local variable " + name + " without declaration.");

            this.variables.add(new Variable(name, value));
        }
        else{
            Variable object = this.variables.get(index);
            object.setValue(value);
        }


    }

    public void deleteVariable(String name){

        int index = findVariable(name);
        if(index == -1){
            System.out.println("Warning: Deleting undefined variable " + name);
        }else{
            this.variables.remove(index);
        }

    }

    private int findVariable(String name){

        for(int i = 0; i < this.variables.size(); i++){
            if(this.variables.get(i).getName().equals(name)){
                return i;
            }
        }

        return -1;

    }

    public Value getVariable(String name){

        Variable object = getVariableObject(name);
        if(object == null){
            return new UndefinedValue();
        }else{
            return object.getValue();
        }

    }

    private Variable getVariableObject(String name){

        int index = findVariable(name);
        if(index == -1){
            return null;
        }
        else{
            return this.variables.get(index);
        }
    }

    public String toString(){

        String output = variables.toString();
        return output;

    }

    public ArrayList<Variable> getVariables() {
        return variables;
    }

}
