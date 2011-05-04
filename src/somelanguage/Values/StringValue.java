package somelanguage.Values;

/**
 *
 * @author Tyler(Chacha) chacha@chacha102.com
 */
public class StringValue implements Value{
    private final String value;

    public StringValue(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return this.value;
    }

}
