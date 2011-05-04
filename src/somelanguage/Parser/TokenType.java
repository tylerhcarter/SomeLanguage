package somelanguage.Parser;

/**
 *
 * @author tylercarter
 */
public enum TokenType {

    // BASIC VARIABLES
    LOCAL_DECLARE,
    GLOBAL_DECLARE,
    ASSIGNMENT,

    ADD,
    SUBTRACT,
    DIVIDE,
    MULTIPLY,

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

    FUNCTION,
    RETURN
};
