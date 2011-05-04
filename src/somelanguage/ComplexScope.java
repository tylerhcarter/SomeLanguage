package somelanguage;

import java.util.ArrayList;
import java.util.Collection;
import somelanguage.Value.Value;
import somelanguage.Value.ValueType;

/**
 *
 * @author Tyler(Chacha) chacha@chacha102.com
 */
public class ComplexScope extends Scope {
    public Scope global;
    public StackBasedScope local;

    public ComplexScope(Scope global, StackBasedScope local){
        this.global = global;
        this.local = local;
    }

    @Override
    public void addVariable(String name, Value value){

    }

    @Override
    public void setVariable(String name, Value value){

        // First Try Local Scope
        if(this.local.getVariable(name).getType() != ValueType.UNDEFINED){
            this.getLocal().setVariable(name, value);
        }
        // Otherwise global
        else{
            this.getGlobal().setVariable(name, value);
        }

    }

    @Override
    public void deleteVariable(String name){

        // First Try Local Scope
        if(this.local.getVariable(name).getType() != ValueType.UNDEFINED){
            this.getLocal().deleteVariable(name);
        }
        // Otherwise global
        else{
            this.getGlobal().deleteVariable(name);
        }

    }

    @Override
    public Value getVariable(String name){
        
        // First Try Local Scope
        if(this.local.getVariable(name).getType() != ValueType.UNDEFINED){
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
    public StackBasedScope getLocal() {
        return local;
    }

    /**
     * @param local the local to set
     */
    public void setLocal(StackBasedScope local) {
        this.local = local;
    }

}
