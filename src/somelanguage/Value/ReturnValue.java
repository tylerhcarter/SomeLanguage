/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package somelanguage.Value;

import somelanguage.Parser.Token;

/**
 *
 * @author tylercarter
 */
public class ReturnValue extends Value{
    private Value value;

    public ReturnValue(Value value){
        this.value = value;
    }

    public Value getValue(){
        return this.value;
    }

    @Override
    public ValueType getType() {
        return ValueType.RETURN;
    }

    @Override
    public Token toToken() {
        return this.value.toToken();
    }

}
