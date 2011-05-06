package somelanguage.Value;

import somelanguage.Parser.Token.Token;
import somelanguage.Parser.Token.TokenType;

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
    public String toString(){
        return this.value + "";
    }

    @Override
    public ValueType getType() {
        return ValueType.BOOLEAN;
    }

    @Override
    public Token toToken() {
        return new Token(TokenType.BOOLEAN, this);
    }

}
