package somelanguage;

import somelanguage.Parser.Token;
import somelanguage.Parser.Parser;
import somelanguage.Parser.LexicalParser;
import java.util.ArrayList;
import somelanguage.Interpreter.SyntaxChecker;

/**
 *
 * @author tylercarter
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Parser parser = new LexicalParser();
        ArrayList<Token> tokens = parser.parse("var t = 5 + 5;");

        SyntaxChecker checker = new SyntaxChecker();
        try {
            checker.run(tokens);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

}
