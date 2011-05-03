/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package somelanguage.Value;

/**
 *
 * @author tylercarter
 */
public class NullValue extends Value{

    @Override
    public ValueType getType() {
        return ValueType.NULL;
    }

    public String toString(){
        return "(null)";
    }

}
