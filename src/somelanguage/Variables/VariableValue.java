package somelanguage.Variables;

import somelanguage.Parser.Token.Token;
import somelanguage.Value.Value;
import somelanguage.Value.ValueType;

/**
 *
 * @author tylercarter
 */
public class VariableValue extends Value {

    private Value value;

    /**
     * @return the value
     */
    public Value getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(Value value) {
        this.value = value;
    }

    public String toString(){
        return this.value.toString();
    }

    @Override
    public ValueType getType() {
        return this.value.getType();
    }

    @Override
    public Token toToken() {
        Token token = this.value.toToken();
        return new Token(token.getTokenType(), this);
    }

}
