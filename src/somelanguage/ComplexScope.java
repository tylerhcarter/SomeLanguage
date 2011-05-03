package somelanguage;

import java.util.ArrayList;
import java.util.Collection;
import somelanguage.Parser.Token;

/**
 *
 * @author Tyler(Chacha) chacha@chacha102.com
 */
public class ComplexScope extends Scope {
    public final Scope global;
    public final Scope local;

    public ComplexScope(Scope global, Scope local){
        this.global = global;
        this.local = local;
    }

    @Override
    public void addVariable(String name, String value){

    }

    @Override
    public void setVariable(String name, String value){

        // First Try Local Scope
        if(!this.local.getVariable(name).equals("undefined")){
            this.local.setVariable(name, value);
        }
        // Otherwise global
        else{
            this.global.setVariable(name, value);
        }

    }

    @Override
    public void deleteVariable(String name){

        // First Try Local Scope
        if(!this.local.getVariable(name).equals("undefined")){
            this.local.deleteVariable(name);
        }
        // Otherwise global
        else{
            this.global.deleteVariable(name);
        }

    }

    @Override
    public String getVariable(String name){

        // First Try Local Scope
        if(!this.local.getVariable(name).equals("undefined")){
            return this.local.getVariable(name);
        }
        // Otherwise global
        else{
            return this.global.getVariable(name);
        }

    }

    @Override
    public String toString(){

        ArrayList<Variable> variables = new ArrayList<Variable>();
        variables.addAll((Collection<Variable>)this.global.getVariables());
        variables.addAll((Collection<Variable>)this.local.getVariables());
        return variables.toString();

    }

}
