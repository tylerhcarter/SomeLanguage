/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package somelanguage.Value;

import somelanguage.Parser.Token.Token;

/**
 *
 * @author tylercarter
 */
public class UndefinedValue extends NullValue{

    @Override
    public ValueType getType() {
        return ValueType.UNDEFINED;
    }

}
