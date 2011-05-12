package somelanguage;

import somelanguage.Interpreter.Function;
import somelanguage.Variables.Scope;
import somelanguage.Variables.ComplexScope;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import somelanguage.Parser.Token.Token;
import somelanguage.Parser.Parser;
import java.util.ArrayList;
import somelanguage.Functions.Echo;
import somelanguage.Functions.Print;
import somelanguage.Interpreter.Construct;
import somelanguage.Interpreter.Expressions.ExpressionProcessor;
import somelanguage.Interpreter.Processor;
import somelanguage.Interpreter.SyntaxException;
import somelanguage.Parser.Token.TokenType;

/**
 *
 * @author tylercarter
 */
public class Main {

    public static Processor runner;
    public static ComplexScope scope;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        Parser parser = getParser();
        Main.runner = getProcessor();
        
        try {
            String text = readFile("/Users/tylercarter/Code/SomeLanguage/src/somelanguage/file.txt");
            text = cleanCode(text);
            ArrayList<Token> tokens = parser.parse(text);

            Scope globalScope = new Scope();
            globalScope.addVariable("echo", new Echo());
            globalScope.addVariable("print", new Print());

            Scope localScope = new Scope();

            // Combines the two scopes
            Main.scope = new ComplexScope(globalScope, localScope);

            Function main = new Function(Main.runner, tokens, Main.scope);
            main.run(Main.scope);
            
        }

        catch (SyntaxException ex) {
            System.out.println("Cause: " + ex.getTokens());
            throw ex;
        }
        
        catch (Exception ex) {
            throw ex;
        }

    }

    private static Processor getProcessor(){
        ExpressionProcessor expressions = new ExpressionProcessor();

        // Constructs
        ArrayList<Construct> constructs = new ArrayList<Construct>();
        constructs.add(new somelanguage.Constructs.GlobalDeclare());
        constructs.add(new somelanguage.Constructs.LocalDeclare());
        constructs.add(new somelanguage.Constructs.Return());
        constructs.add(new somelanguage.Constructs.Conditional());
        constructs.add(new somelanguage.Constructs.Echo());

        return new Processor(expressions, constructs);
    }

    private static Parser getParser(){
        somelanguage.Parser.Configuration pConfig = new somelanguage.Parser.Configuration();

        // Create Keywords
        pConfig.addKeyword("global", TokenType.GLOBAL_DECLARE);
        pConfig.addKeyword("var", TokenType.LOCAL_DECLARE);
        pConfig.addKeyword("function", TokenType.FUNCTION_DECLARE);
        pConfig.addKeyword("return", TokenType.RETURN);
        pConfig.addKeyword("if", TokenType.IF);
        pConfig.addKeyword("elif", TokenType.ELIF);
        pConfig.addKeyword("else", TokenType.ELSE);

        pConfig.addKeyword("echo", TokenType.ECHO);

        // Create Symbols
        pConfig.addSymbol(";", TokenType.END_STATEMENT);
        pConfig.addSymbol("=", TokenType.ASSIGNMENT);
        pConfig.addSymbol("==", TokenType.EQUALITY);
        pConfig.addSymbol("&&", TokenType.AND);
        pConfig.addSymbol("||", TokenType.OR);
        pConfig.addSymbol("+", TokenType.ADD);
        pConfig.addSymbol("-", TokenType.SUBTRACT);
        pConfig.addSymbol("*", TokenType.MULTIPLY);
        pConfig.addSymbol("/", TokenType.DIVIDE);
        pConfig.addSymbol("\"", TokenType.QUOTE);
        pConfig.addSymbol(",", TokenType.COMMA);
        pConfig.addSymbol("(", TokenType.OPENBRACKET);
        pConfig.addSymbol(")", TokenType.CLOSEBRACKET);
        pConfig.addSymbol("{", TokenType.OPENBRACES);
        pConfig.addSymbol("}", TokenType.CLOSEBRACES);

        return new Parser(pConfig);
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
