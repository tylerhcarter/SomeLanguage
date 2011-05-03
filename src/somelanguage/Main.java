package somelanguage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
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
        SyntaxChecker checker = new SyntaxChecker();
        
        try {
            String text = readFile("H:/cha_java/SomeLanguage/src/somelanguage/file.txt");
            text = cleanCode(text);
            ArrayList<Token> tokens = parser.parse(text);
            System.out.println("Finished Parsing");
            
            checker.run(tokens);
            System.out.println("Finished Running");
        } catch (Exception ex) {
            System.out.println(ex);
        }

    }

    private static String cleanCode(String text){

        while(true){

            int index = text.indexOf("\r\n");
            if(index == -1){
                break;
            }

            text = text.substring(0, index) + " " + text.substring(index + "\r\n".length());
        }

        return text;
    }

    private static String readFile(String path) throws IOException {
        FileInputStream stream = new FileInputStream(new File(path));
        try {
            FileChannel fc = stream.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            /* Instead of using default, pass in a decoder. */
            return Charset.defaultCharset().decode(bb).toString();
        }
        finally {
            stream.close();
        }
    }

}
