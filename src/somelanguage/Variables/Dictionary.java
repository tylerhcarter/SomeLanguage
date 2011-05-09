package somelanguage.Variables;

import java.util.ArrayList;

/**
 *
 * @author Tyler(Chacha) chacha@chacha102.com
 */
public class Dictionary <T> {

    private ArrayList<Definition> definitions = new ArrayList<Definition>();

    public void set(String key, T value){
        int index = this.getIndex(key);
        if(index == -1)
            this.definitions.add(new Definition(key, value));
        else
            this.definitions.get(index).setValue(value);
    }

    public T get(String key){
        int index = this.getIndex(key);
        if(index == -1)
            return null;
        else
            return this.definitions.get(index).getValue();
    }

    public T remove(String key){
        int index = this.getIndex(key);
        if(index == -1)
            return null;
        else
            return this.definitions.remove(index).getValue();
    }

    public int size(){
        return this.definitions.size();
    }

    private int getIndex(String key){
        for(int i = 0; i < this.definitions.size(); i++)
            if(this.definitions.get(i).getKey().equals(key))
                return i;

        return -1;
    }

    class Definition {

        private String key = "";
        private T value;

        public Definition(String key){
            this.key = key;
        }

        public Definition(String key, T value){
            this.key = key;
            this.value = value;
        }

        /**
         * @return the key
         */
        public String getKey() {
            return key;
        }

        /**
         * @param key the key to set
         */
        public void setKey(String key) {
            this.key = key;
        }

        /**
         * @return the value
         */
        public T getValue() {
            return value;
        }

        /**
         * @param value the value to set
         */
        public void setValue(T value) {
            this.value = value;
        }

    }

}
