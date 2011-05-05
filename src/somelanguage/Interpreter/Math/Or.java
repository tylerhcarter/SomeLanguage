package somelanguage.Interpreter.Math;

import somelanguage.Interpreter.ExpressionEngine;
import somelanguage.Parser.Token.Token;
import somelanguage.Parser.Token.TokenType;
import somelanguage.Value.BooleanValue;
import somelanguage.Value.IntegerValue;
import somelanguage.Variables.ComplexScope;

/**
 *
 * @author Tyler(Chacha) chacha@chacha102.com
 */
public class Or extends MathOperation {

    public Or(ExpressionEngine engine){
        super(engine);
    }

    @Override
    protected TokenType getSearchToken() {
        return TokenType.OR;
    }

    @Override
    protected Token evaluateExpression(Expression expression, ComplexScope scope) throws Exception{

        BooleanValue numerator, denominator;

        try{
            numerator = (BooleanValue) expression.getNumerator();
        }catch(ClassCastException ex){
            throw new Exception("Could not convert " + expression.getNumerator().getType() + " to BooleanValue.");
        }

        try{
            denominator = (BooleanValue) expression.getDenominator();
        }catch(ClassCastException ex){
            throw new Exception("Could not convert " + expression.getDenominator().getType() + " to BooleanValue.");
        }

        if(numerator.getValue() == true || denominator.getValue() == true){
            return new Token(TokenType.BOOLEAN, new BooleanValue("true"));
        }else{
            return new Token(TokenType.BOOLEAN, new BooleanValue("false"));
        }

    }

}
