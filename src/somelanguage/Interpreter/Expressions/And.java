package somelanguage.Interpreter.Expressions;

import somelanguage.Interpreter.TypeException;
import somelanguage.Parser.Token.Token;
import somelanguage.Parser.Token.TokenType;
import somelanguage.Value.BooleanValue;
import somelanguage.Variables.ComplexScope;

/**
 *
 * @author Tyler(Chacha) chacha@chacha102.com
 */
public class And extends MathOperation {

    public And(ExpressionProcessor engine){
        super(engine);
    }

    @Override
    protected TokenType getSearchToken() {
        return TokenType.AND;
    }

    @Override
    protected Token evaluateExpression(Expression expression, ComplexScope scope) throws Exception{

        BooleanValue numerator, denominator;

        try{
            numerator = (BooleanValue) expression.getNumerator();
        }catch(ClassCastException ex){
            throw new TypeException("Could not convert " + expression.getNumerator().getType() + " to IntegerValue.", expression.getNumerator());
        }

        try{
            denominator = (BooleanValue) expression.getDenominator();
        }catch(ClassCastException ex){
            throw new TypeException("Could not convert " + expression.getDenominator().getType() + " to IntegerValue.", expression.getDenominator());
        }

        if(numerator.getValue() == true && denominator.getValue() == true){
            return new Token(TokenType.BOOLEAN, new BooleanValue("true"));
        }else{
            return new Token(TokenType.BOOLEAN, new BooleanValue("false"));
        }

    }

}
