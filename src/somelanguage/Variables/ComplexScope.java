package somelanguage.Variables;

import java.util.ArrayList;
import java.util.Collection;
import somelanguage.Value.Value;

/**
 *
 * @author Tyler(Chacha) chacha@chacha102.com
 */
public class ComplexScope extends Scope {
    public Scope global;
    public Scope local;

    public ComplexScope(Scope global, Scope local){
        this.global = global;
        this.local = local;
    }

    @Override
    public void addVariable(String name, Value value){

    }

    @Override
    public Value setVariable(String name, Value value){

        // First Try Local Scope
        if(this.local.getVariable(name) != null){
            return this.getLocal().setVariable(name, value);
        }
        // Otherwise global
        else{
            return this.getGlobal().setVariable(name, value);
        }

    }

    @Override
    public Value deleteVariable(String name){

        // First Try Local Scope
        if(this.local.getVariable(name) != null){
            return this.getLocal().deleteVariable(name);
        }
        // Otherwise global
        else{
            return this.getGlobal().deleteVariable(name);
        }

    }

    @Override
    public Value getVariable(String name){
        
        // First Try Local Scope
        if(this.local.getVariable(name) != null){
            return this.getLocal().getVariable(name);
        }
        // Otherwise global
        else{
            return this.getGlobal().getVariable(name);
        }

    }

    @Override
    public String toString(){

        ArrayList<Variable> variables = new ArrayList<Variable>();
        variables.addAll((Collection<Variable>)this.getGlobal().getVariables());
        variables.addAll((Collection<Variable>)this.getLocal().getVariables());
        return variables.toString();

    }

    /**
     * @return the global
     */
    public Scope getGlobal() {
        return global;
    }

    /**
     * @param global the global to set
     */
    public void setGlobal(Scope global) {
        this.global = global;
    }

    /**
     * @return the local
     */
    public Scope getLocal() {
        return local;
    }

    /**
     * @param local the local to set
     */
    public void setLocal(Scope local) {
        this.local = local;
    }

}
