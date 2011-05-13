package somelanguage.Interpreter;

import somelanguage.Value.Value;

/**
 *
 * @author Tyler(Chacha) chacha@chacha102.com
 */
public class TypeException extends Exception {
    private final Value value;

    public TypeException(String message, Value value){
        super(message);
        this.value = value;
    }

    public Value getValue(){
        return value;
    }
}
