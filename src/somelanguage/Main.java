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
import somelanguage.Interpreter.Functions.Echo;
import somelanguage.Interpreter.Functions.Print;
import somelanguage.Interpreter.Constructs.Construct;
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

            Main.scope = getScope();

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

    public static ComplexScope getScope(){

        Scope globalScope = new Scope();
        globalScope.addVariable("echo", new Echo());
        globalScope.addVariable("print", new Print());

        Scope localScope = new Scope();

        return new ComplexScope(globalScope, localScope);

    }

    public static Processor getProcessor(){
        ExpressionProcessor expressions = new ExpressionProcessor();

        // Constructs
        ArrayList<Construct> constructs = new ArrayList<Construct>();
        
        constructs.add(new somelanguage.Interpreter.Constructs.GlobalDeclare());
        constructs.add(new somelanguage.Interpreter.Constructs.LocalDeclare());
        constructs.add(new somelanguage.Interpreter.Constructs.Return());
        constructs.add(new somelanguage.Interpreter.Constructs.Conditional());
        constructs.add(new somelanguage.Interpreter.Constructs.Echo());

        return new Processor(expressions, constructs);
    }

    public static Parser getParser(){
        somelanguage.Parser.Configuration pConfig = getParserConfig();
        return new Parser(pConfig);
    }

    public static somelanguage.Parser.Configuration getParserConfig(){
        somelanguage.Parser.Configuration pConfig = new somelanguage.Parser.Configuration();

        // Basic Keywords
        pConfig.addKeyword("global", TokenType.GLOBAL_DECLARE, true);
        pConfig.addKeyword("var", TokenType.LOCAL_DECLARE, true);
        pConfig.addKeyword("function", TokenType.FUNCTION_DECLARE);
        pConfig.addKeyword("return", TokenType.RETURN, true);
        pConfig.addKeyword("if", TokenType.IF, true);
        pConfig.addKeyword("elif", TokenType.ELIF);
        pConfig.addKeyword("else", TokenType.ELSE);
        pConfig.addKeyword("echo", TokenType.ECHO, true);
        
        // Comon Symbols
        pConfig.addSymbol(";", TokenType.END_STATEMENT);
        pConfig.addSymbol("\"", TokenType.QUOTE);
        pConfig.addSymbol(",", TokenType.COMMA);
        
        // General Operators
        pConfig.addSymbol("+", TokenType.ADD);
        pConfig.addSymbol("-", TokenType.SUBTRACT);
        pConfig.addSymbol("*", TokenType.MULTIPLY);
        pConfig.addSymbol("/", TokenType.DIVIDE);
        pConfig.addSymbol("=", TokenType.ASSIGNMENT);
        
        // Equality Operators
        pConfig.addSymbol("==", TokenType.EQUALITY);
        pConfig.addSymbol("&&", TokenType.AND);
        pConfig.addSymbol("&", TokenType.AND);
        pConfig.addKeyword("AND", TokenType.AND);
        pConfig.addSymbol("||", TokenType.OR);
        pConfig.addSymbol("|", TokenType.OR);
        pConfig.addKeyword("OR", TokenType.OR);

        // Structural Symbols
        pConfig.addSymbol("(", TokenType.OPENBRACKET);
        pConfig.addSymbol(")", TokenType.CLOSEBRACKET);
        pConfig.addSymbol("{", TokenType.OPENBRACES);
        pConfig.addSymbol("}", TokenType.CLOSEBRACES);

        // Extra
        pConfig.addKeyword("variable", TokenType.LOCAL_DECLARE);
        pConfig.addKeyword("make", TokenType.LOCAL_DECLARE);
        pConfig.addKeyword("create", TokenType.FUNCTION_DECLARE);
        pConfig.addKeyword("equals", TokenType.ASSIGNMENT);
        pConfig.addKeyword("equal", TokenType.ASSIGNMENT);
        pConfig.addKeyword("plus", TokenType.ADD);
        pConfig.addKeyword("minus", TokenType.SUBTRACT);
        pConfig.addKeyword("times", TokenType.MULTIPLY);
        pConfig.addKeyword("say", TokenType.ECHO);
        pConfig.addKeyword("speak", TokenType.ECHO);
        pConfig.addKeyword("els", TokenType.ELSE);
        pConfig.addKeyword("eli", TokenType.ELIF);
        pConfig.addKeyword("el", TokenType.ELIF);

        return pConfig;
    }

    public static String cleanCode(String text){

        while(true){

            int returnIndex = text.indexOf("\r");
            int tabIndex = text.indexOf("\t");

            int index;
            if(returnIndex != -1){
                index = returnIndex;
            }else if(tabIndex != -1){
                index = tabIndex;
            }else{
                break;
            }

            text = text.substring(0, index) + "\n" + text.substring(index + "\r".length());
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
