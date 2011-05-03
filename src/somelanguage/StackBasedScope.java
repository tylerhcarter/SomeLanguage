package somelanguage;

import java.util.ArrayList;
import java.util.Stack;

/**
 *
 * @author tylercarter
 */
public class StackBasedScope extends Scope{

    protected Stack<ArrayList<Variable>> scopeStack = new Stack();

    /*
     * Delves into a new stack
     */
    public void addStack(){

        this.scopeStack.push(this.variables);
        this.variables = new ArrayList<Variable>();

    }

    /*
     *  Returns one level up
     */
    public void removeStack(){

        this.variables = this.scopeStack.pop();

    }

}
