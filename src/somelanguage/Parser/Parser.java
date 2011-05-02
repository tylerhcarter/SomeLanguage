package somelanguage.Parser;

import java.util.ArrayList;

/**
 *
 * @author tylercarter
 */
public interface Parser {

    public ArrayList<Token> parse (String text);

}
