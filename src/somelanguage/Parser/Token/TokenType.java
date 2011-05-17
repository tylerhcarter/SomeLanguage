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
    COLON,
    DOT,
    
    INTEGER,
    STRING,
    ENCAPSULATED_STRING,

    QUOTE,

    END_STATEMENT,
    UNDEFINED,

    NULL,
    BOOLEAN,
    RETURN,   
    USERFUNC,
    OBJECT,

    AND,
    OR,
    IF,
    ELIF,
    ELSE,

    ECHO
};
