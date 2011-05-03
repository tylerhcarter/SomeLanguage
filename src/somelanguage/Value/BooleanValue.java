/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package somelanguage.Value;

/**
 *
 * @author tylercarter
 */
public class BooleanValue extends Value {
    private final boolean value;

    public BooleanValue(boolean value){
        this.value = value;
    }

    public BooleanValue(String value){
        if(value.equals("false")){
            this.value = false;
        }else{
            this.value = true;
        }
    }

    public boolean getValue(){
        return this.value;
    }

    @Override
    public ValueType getType() {
        return ValueType.BOOLEAN;
    }

}
