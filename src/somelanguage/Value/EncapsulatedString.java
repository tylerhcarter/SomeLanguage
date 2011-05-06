package somelanguage.Value;

import java.util.ArrayList;
import somelanguage.Parser.Token.Token;
import somelanguage.Parser.Token.TokenType;

/**
 *
 * @author tylercarter
 */
public class EncapsulatedString extends Value{

    protected ArrayList<StringValue> strings = new ArrayList<StringValue>();

    public EncapsulatedString(String string){
        this.strings.add(new StringValue(string));
    }

    public EncapsulatedString(ArrayList<StringValue> strings){
        for(StringValue string:strings){
            this.strings.add(string);
        }
    }

    public String getValue(){
        return getFull();
    }

    public void add(StringValue string){
        this.strings.add(string);
    }

    public void add(String string){
        this.strings.add(new StringValue(string));
    }

    private String getFull(){
        String output = "";
        for(int i = 0; i < this.strings.size(); i++){

            output += this.strings.get(i).getValue() + " ";

        }

        return output;
    }

    @Override
    public ValueType getType() {
        return ValueType.ENCAPSULATED_STRING;
    }

    @Override
    public String toString(){
        return getFull();
    }

    @Override
    public Token toToken() {
        return new Token(TokenType.ENCAPSULATED_STRING, this);
    }

}
