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
    private ArrayList<Symbol> symbols = new ArrayList<Symbol>();

    public Parser(Configuration config) {

        for(Keyword keyword:config.getKeywords()){
            this.keywords.add(keyword);
        }

        for(Symbol symbol:config.getSymbols()){
            this.symbols.add(symbol);
        }

    }

    public ArrayList<Token> parse(String text) {

        // Split by newlines
        String[] strings = text.split("\n");

        for(String string:strings){
            if(string.startsWith("//"))
                continue;
            else
                parseLine(string);
        }

        return this.tokens;
    }

    public ArrayList<Token> parseLine(String text) {

        // Add an end statement
        text += ";";

        // Remove Newlines
        text = text.replace("\n", "");

        // Split up everything
        String[] strings = text.split("");

        // Parse all tokens
        for(String string:strings){
            addToken(string);
        }

        // Search for Encapsulated strings
        parseEncapsulatedStrings();

        // Search for Equality Operators
        parseEquality();

        return this.tokens;
    }

    private String buffer = "";

    private void addToken(String string) {

        // Clear empty buffer
        if(buffer.equals(" ")){
            buffer = "";
        }

        // Check if it is a symbol
        if(isSymbol(string)){

            if(!buffer.equals("")){
                // Add buffer
                evaluateString(buffer);
                buffer = "";
            }

            // Add symbol
            this.tokens.add(convertSymbol(string));
        }

        else if(string.equals(" ")){

            if(!buffer.equals("")){
                // Add buffer
                evaluateString(buffer);
                buffer = "";
            }

        }

        // Otherwise add it to the buffer
        else{
            this.buffer += string;
        }

    }

    private void evaluateString(String string){

        // Check for a keyword
        if(isKeyword(string)){
            this.tokens.add(convertKeyword(string));
        }

        // Integer?
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
    }

    private Token convertKeyword(String string) {

        for(Keyword keyword:(Iterable<Keyword>)this.keywords){
            if(keyword.getKeyword().equals(string)){
                if(keyword.isBreaking()){
                    this.tokens.add(new Token(TokenType.END_STATEMENT));
                }
                return new Token(keyword.getTokenType());
            }
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

    private Token convertSymbol(String string) {

        for(Symbol symbol:(Iterable<Symbol>)this.symbols){
            if(symbol.getKeyword().equals(string))
                return new Token(symbol.getTokenType());
        }

        throw new IllegalArgumentException("Input is not a valid symbol.");

    }

    private boolean isSymbol(String string) {
        try{
            convertSymbol(string);
            return true;
        }catch(Exception ex){
            return false;
        }
    }

    private void parseEquality() {

        for(int i = 0; i < tokens.size(); i++){

            Token token = tokens.get(i);

            // Check for an assignment operator
            if(token.getTokenType() == TokenType.ASSIGNMENT){

                if(i + 1 < tokens.size()){

                    if(tokens.get(i + 1).getTokenType() ==  TokenType.ASSIGNMENT)
                    {

                        tokens.remove(i);
                        tokens.remove(i);
                        tokens.add(i, new Token(TokenType.EQUALITY));

                    }

                }

            }

        }

    }

    

}