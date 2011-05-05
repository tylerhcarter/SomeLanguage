/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package somelanguage.Interpreter.Math;

import somelanguage.Value.Value;

/**
 *
 * @author tylercarter
 */
public class Expression {
    private Value numerator;
    private Value denominator;

    public Expression(Value numerator, Value denominator){
        this.numerator = numerator;
        this.denominator = denominator;
    }

    /**
     * @return the numerator
     */
    public Value getNumerator() {
        return numerator;
    }

    /**
     * @param numerator the numerator to set
     */
    public void setNumerator(Value numerator) {
        this.numerator = numerator;
    }

    /**
     * @return the denominator
     */
    public Value getDenominator() {
        return denominator;
    }

    /**
     * @param denominator the denominator to set
     */
    public void setDenominator(Value denominator) {
        this.denominator = denominator;
    }

}
