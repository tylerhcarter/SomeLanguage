package somelanguage.Parser;

import somelanguage.Parser.Token.TokenType;
import somelanguage.Parser.Token.Token;
import java.util.ArrayList;
import somelanguage.Value.BooleanValue;
import somelanguage.Value.IntegerValue;
import somelanguage.Value.EncapsulatedString;
import somelanguage.Value.NullValue;
import somelanguage.Value.StringValue;

/**
 *
 * @author tylercarter
 */
public class Parser{

    ArrayList<Token> tokens = new ArrayList<Token>();
    private ArrayList<Keyword> keywords = new ArrayList<Keyword>();

    public Parser() {

        // Basic Keywords
        keywords.add(new Keyword("global", TokenType.GLOBAL_DECLARE));
        keywords.add(new Keyword("var", TokenType.LOCAL_DECLARE));
        
        keywords.add(new Keyword("=", TokenType.ASSIGNMENT));
        keywords.add(new Keyword(";", TokenType.END_STATEMENT));

        keywords.add(new Keyword("==", TokenType.EQUALITY));
        keywords.add(new Keyword("&&", TokenType.AND));
        keywords.add(new Keyword("||", TokenType.OR));

        keywords.add(new Keyword("+", TokenType.ADD));
        keywords.add(new Keyword("-", TokenType.SUBTRACT));
        keywords.add(new Keyword("/", TokenType.DIVIDE));
        keywords.add(new Keyword("*", TokenType.MULTIPLY));

        keywords.add(new Keyword(",", TokenType.COMMA));

        keywords.add(new Keyword("\"", TokenType.QUOTE));
        keywords.add(new Keyword("(", TokenType.OPENBRACKET));
        keywords.add(new Keyword(")", TokenType.CLOSEBRACKET));

        keywords.add(new Keyword("{", TokenType.OPENBRACES));
        keywords.add(new Keyword("}", TokenType.CLOSEBRACES));

        keywords.add(new Keyword("function", TokenType.FUNCTION_DECLARE));
        keywords.add(new Keyword("return", TokenType.RETURN));
        keywords.add(new Keyword("if", TokenType.IF));
    }

    public ArrayList<Token> parse(String text) {
        String[] strings = text.split(" ");

        // Parse the inital set of tokens
        for(String string:strings){
            addToken(string);
        }

        // Search for Encapsulated strings
        parseEncapsulatedStrings();


        return this.tokens;
    }

    private void addToken(String string) {
        ArrayList<Token> endBuffer = new ArrayList<Token>();

        // Check if a semicolon is at the end
        if(string.endsWith(";")){
            string = string.substring(0, string.length() - 1);
            endBuffer.add(new Token(TokenType.END_STATEMENT));
        }

        // Check for beginning or end quotes
        if(string.startsWith("\"")){
            string = string.substring(1);
            this.tokens.add(new Token(TokenType.QUOTE));
        }

        if(string.endsWith("\"")){
            string = string.substring(0, string.length() - 1);
            endBuffer.add(0, new Token(TokenType.QUOTE));
        }

        // Nothing left? We're done here
        if(string.equals("")){
            this.tokens.addAll(endBuffer);
            return;
        }

        // Try to convert to a keyword
        if(isKeyword(string)){
            this.tokens.add(convertKeyword(string));
        }

        // Check if it is an integer
        else if(isInteger(string)){
            this.tokens.add(new Token(TokenType.INTEGER, new IntegerValue(Integer.parseInt(string))));
        }

        // Try boolean values
        else if(string.equals("true")){
            this.tokens.add(new Token(TokenType.BOOLEAN, new BooleanValue("true")));
        }
        else if(string.equals("false")){
            this.tokens.add(new Token(TokenType.BOOLEAN, new BooleanValue("false")));
        }

        // Check for a null
        else if(string.equals("null")){
            this.tokens.add(new Token(TokenType.NULL, new NullValue()));
        }

        // Otherwise it is a string/variable name
        else{
            this.tokens.add(new Token(TokenType.STRING, new StringValue(string)));            
        }
        
        // Add end buffer
        this.tokens.addAll(endBuffer);
    }

    private Token convertKeyword(String string) {

        for(Keyword keyword:(Iterable<Keyword>)this.keywords){
            if(keyword.getKeyword().equals(string))
                return new Token(keyword.getTokenType());
        }
        
        throw new IllegalArgumentException("Input is not a valid keyword.");

    }

    private boolean isKeyword(String string) {
        try{
            convertKeyword(string);
            return true;
        }catch(Exception ex){
            return false;
        }
    }

    public boolean isInteger( String input )
    {
       try
       {
          Integer.parseInt( input );
          return true;
       }
       catch( Exception ex )
       {
          return false;
       }
    }

    private void parseEncapsulatedStrings() {

        for(int i = 0; i < tokens.size(); i++){

            Token token = tokens.get(i);

            // Check for a quote
            if(token.getTokenType() == TokenType.QUOTE){

                int endQuote = getCloseQuote(this.tokens, i);

                ArrayList<Token> stringTokens = slice(tokens, i, endQuote);

                // Turn Tokens into Strings
                ArrayList<StringValue> stringValues = new ArrayList<StringValue>();

                for(Token t:stringTokens){

                    stringValues.add((StringValue) t.getTokenValue());

                }

                Token string = new Token(TokenType.ENCAPSULATED_STRING, new EncapsulatedString(stringValues));

                tokens.add(i, string);

                i = 0;
            }

        }
    }

    /*
     * Returns the closest close bracket
     */
    private int getCloseQuote(ArrayList<Token> tokens, int openBracket) {

        for(int i = openBracket + 1; i < tokens.size(); i++){
            if(tokens.get(i).getTokenType() == TokenType.QUOTE){
                    return i;
            }
        }

        return -1;

    }

    /*
     * Removes elements between start and end from token array and returns them
     */
    private ArrayList<Token> slice(ArrayList<Token> tokens, int start, int end){

        ArrayList<Token> result = new ArrayList<Token>();

        for(int i = start; i < end - 1; i++){
            Token token = tokens.remove(start + 1);
            result.add(token);
        }

        // Get rid of old brackets
        tokens.remove(start);
        tokens.remove(start);

        return result;

    }

    

}