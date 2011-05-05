package somelanguage.Parser.Token;

/**
 *
 * @author tylercarter
 */
public enum TokenType {

    // BASIC VARIABLES
    LOCAL_DECLARE,
    GLOBAL_DECLARE,
    FUNCTION_DECLARE,
    ASSIGNMENT,

    ADD,
    SUBTRACT,
    DIVIDE,
    MULTIPLY,

    EQUALITY,

    OPENBRACKET,
    CLOSEBRACKET,

    OPENBRACES,
    CLOSEBRACES,

    COMMA,

    INTEGER,
    STRING,

    QUOTE,

    END_STATEMENT,
    UNDEFINED,

    NULL,
    BOOLEAN,
    RETURN,   
    USERFUNC,

    AND,
    OR
};
