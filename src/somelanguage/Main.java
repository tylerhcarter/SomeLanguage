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
import somelanguage.Interpreter.Runner;

/**
 *
 * @author tylercarter
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        Parser parser = new LexicalParser();
        Runner checker = new Runner();
        
        try {
            String text = readFile("/Users/tylercarter/Code/SomeLanguage/src/somelanguage/file.txt");
            text = cleanCode(text);
            ArrayList<Token> tokens = parser.parse(text);
            
            checker.run(tokens);
        } catch (Exception ex) {
            throw ex;
        }

    }

    private static String cleanCode(String text){

        while(true){

            int returnIndex = text.indexOf("\r");
            int newlineIndex = text.indexOf("\n");
            int tabIndex = text.indexOf("\t");

            int index;
            if(returnIndex != -1){
                index = returnIndex;
            }else if(newlineIndex != -1){
                index = newlineIndex;
            }else if(tabIndex != -1){
                index = tabIndex;
            }else{
                break;
            }

            text = text.substring(0, index) + " " + text.substring(index + "\r".length());
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
