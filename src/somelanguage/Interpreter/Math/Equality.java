package somelanguage.Interpreter.Math;

import somelanguage.Interpreter.ExpressionEngine;
import somelanguage.Parser.Token.Token;
import somelanguage.Parser.Token.TokenType;
import somelanguage.Value.BooleanValue;
import somelanguage.Value.IntegerValue;
import somelanguage.Value.StringValue;
import somelanguage.Value.Value;
import somelanguage.Value.ValueType;
import somelanguage.Variables.ComplexScope;

/**
 *
 * @author Tyler(Chacha) chacha@chacha102.com
 */
public class Equality extends MathOperation {

    public Equality(ExpressionEngine engine){
        super(engine);
    }

    @Override
    protected TokenType getSearchToken() {
        return TokenType.EQUALITY;
    }

    @Override
    protected Token evaluateExpression(Expression expression, ComplexScope scope) throws Exception{

        Value numerator, denominator;

        try{
            numerator = (Value) expression.getNumerator();
        }catch(ClassCastException ex){
            throw new Exception("Could not convert " + expression.getNumerator().getType() + " to Value.");
        }

        try{
            denominator = (Value) expression.getDenominator();
        }catch(ClassCastException ex){
            throw new Exception("Could not convert " + expression.getDenominator().getType() + " to Value.");
        }

        // Check if they are the same type
        if(numerator.getType() != denominator.getType()){
            return getFalse();
        }

        // Strings, Booleans, and Integers are direct comparisons
        if(numerator.getType() == ValueType.BOOLEAN){
            if(((BooleanValue) numerator).getValue() == ((BooleanValue) denominator).getValue()){
                return getTrue();
            }else{
                return getFalse();
            }
        }

        if(numerator.getType() == ValueType.STRING){
            if(((StringValue) numerator).getValue().equals(((StringValue) denominator).getValue())){
                return getTrue();
            }else{
                return getFalse();
            }
        }

        if(numerator.getType() == ValueType.INTEGER){
            if(((IntegerValue) numerator).getValue() == ((IntegerValue) denominator).getValue()){
                return getTrue();
            }else{
                return getFalse();
            }
        }

        if(numerator.getType() == ValueType.NULL){
            return getTrue();
        }

        return getFalse();

    }

    private Token getFalse(){
        return new Token(TokenType.BOOLEAN, new BooleanValue("false"));
    }

    private Token getTrue(){
        return new Token(TokenType.BOOLEAN, new BooleanValue("true"));
    }

}
