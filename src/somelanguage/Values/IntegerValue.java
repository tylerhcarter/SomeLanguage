package somelanguage.Values;

/**
 *
 * @author Tyler(Chacha) chacha@chacha102.com
 */
public class IntegerValue implements Value{
    private final int value;

    public IntegerValue(int value){
        this.value = value;
    }

    public int toInteger(){
        return this.value;
    }

}
