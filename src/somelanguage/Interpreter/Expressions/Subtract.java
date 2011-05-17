package somelanguage.Interpreter.Expressions;

import somelanguage.Interpreter.TypeException;
import somelanguage.Parser.Token.Token;
import somelanguage.Parser.Token.TokenType;
import somelanguage.Value.IntegerValue;
import somelanguage.Variables.ComplexScope;

/**
 *
 * @author Tyler(Chacha) chacha@chacha102.com
 */
public class Subtract extends MathOperation {

    public Subtract(ExpressionProcessor engine){
        super(engine);
    }

    @Override
    protected TokenType getSearchToken() {
        return TokenType.SUBTRACT;
    }

    @Override
    protected Token evaluateExpression(Expression expression, ComplexScope scope) throws Exception{

        IntegerValue numerator, denominator;

        try{
            numerator = (IntegerValue) expression.getNumerator();
        }catch(ClassCastException ex){
            throw new TypeException("Could not convert " + expression.getNumerator().getType() + " to IntegerValue.", expression.getNumerator());
        }

        try{
            denominator = (IntegerValue) expression.getDenominator();
        }catch(ClassCastException ex){
            throw new TypeException("Could not convert " + expression.getDenominator().getType() + " to IntegerValue.", expression.getDenominator());
        }

        int sum = (numerator.getValue() - denominator.getValue());

        return new Token(TokenType.INTEGER, new IntegerValue(sum));

    }

}
