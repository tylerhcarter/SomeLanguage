package somelanguage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Stack;
import somelanguage.Value.UndefinedValue;
import somelanguage.Value.Value;

/**
 *
 * @author tylercarter
 */
public class StackBasedScope extends Scope{

    protected Stack<ArrayList<Variable>> scopeStack = new Stack();

    public StackBasedScope(String name){
        super(name);
    }

    /*
     * Delves into a new stack
     */
    public void addStack(String name){
        System.out.println("----- Add Stack (" + name + ")");
        this.scopeStack.push(this.variables);
        this.variables = new ArrayList<Variable>();

    }

    /*
     *  Returns one level up
     */
    public void removeStack(String name){
        System.out.println("----- Remove Stack (" + name + ")");
        this.variables = this.scopeStack.pop();

    }

    @Override
    public Value getVariable(String name){

        ArrayList<ArrayList<Variable>> stack = new ArrayList<ArrayList<Variable>>();
        for(int i = 0; i < this.scopeStack.size(); i++){
            stack.add(this.scopeStack.get(i));
        }
        
        stack.add(variables);

        System.out.println(stack);

        // Go up the scope stack
        for(int x = stack.size() - 1; x >= 0 ; x--){

            ArrayList<Variable> vars = stack.get(x);
            
            for(int i = 0; i < vars.size(); i++){
                if(vars.get(i).getName().equals(name)){
                    return vars.get(i).getValue();
                }
            }

        }

        return new UndefinedValue();

    }

    @Override
    public Variable getVariableObject(String name){

        // Go up the scope stack
        for(int x = this.scopeStack.size() - 1; x > 0 ; x++){

            ArrayList<Variable> vars = this.scopeStack.get(x);
            for(int i = 0; i < vars.size(); i++){
                if(vars.get(i).getName().equals(name)){
                    return vars.get(i);
                }
            }

        }

        // Create a new variable
        Variable var = new Variable(name);
        this.addVariable(var);
        return var;

    }

}
