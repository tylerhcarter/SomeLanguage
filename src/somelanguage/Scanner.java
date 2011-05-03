
package somelanguage;

import java.util.ArrayList;
import somelanguage.Parser.Token;
import somelanguage.Parser.TokenType;

/**
 *
 * @author tylercarter
 */
public class Scanner {
    
    private ArrayList<Token> tokens;
    private int counter = -1;
    
    public Scanner(ArrayList<Token> tokens){
        this.tokens = tokens;
    }

    public Token get(int index){
        return this.tokens.get(index);
    }

    public Token getCurrent(){
        if(counter == -1)
            return next();
        
        return get(counter);
    }

    public Token next(){
        return next(true);
    }

    public Token next(boolean advanceCounter){
        if(!hasNext())
            return new Token(TokenType.UNDEFINED);

        if(advanceCounter == true){
            counter++;
            return getCurrent();
        }else{
            return get(counter + 1);
        }

    }

    public Token next(int advance){
        if((counter + advance) >= this.tokens.size())
            return new Token(TokenType.UNDEFINED);

        return get(counter + advance);
    }

    public ArrayList<Token> getTokens(){
        return this.tokens;
    }

    public Scanner getTokens(int num){

        ArrayList<Token> subset = new ArrayList<Token>();

        for(int i = counter + 1; i <= counter + num; i++){
            if(i >= this.tokens.size())
                break;

            subset.add(get(i));
        }

        counter = counter + num + 1;
        return new Scanner(subset);
    }

    public Scanner getTokenToEndStatement(){

        int count = counter + 1;
        int start = counter + 1;
        while(count < this.tokens.size()){
            if(this.tokens.get(count).getTokenType() == TokenType.END_STATEMENT){
                break;
            }
            count++;
        }
        return getTokens(count - start);
    }

    public boolean hasNext() {
        if((counter + 1) < this.tokens.size()){
            return true;
        }else{
            return false;
        }
    }

    public void reset(){
        counter = -1;
    }

}
