package somelanguage;

import java.util.ArrayList;

/**
 *
 * @author tylercarter
 */
public class Scope {

    private ArrayList<Variable> variables = new ArrayList<Variable>();
    
    public Scope(){
        
    }

    public void addVariable(String name, String value){

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

    public void setVariable(String name, String value){

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

    public String getVariable(String name){

        Variable object = getVariableObject(name);
        if(object == null){
            return "undefined";
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

}
