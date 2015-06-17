package oop.ex6.exceptions;

/**
 * An exception that is thrown each time the program finds an invalid part in the code.
 * The class holds several static strings representing the different causes for the exception throw.
 *
 */
public class CodeException extends Exception
{
    // The different messages that represent the causes for the exception to be thrown:
    public static final String MULTIPLE_PARAMETES_NAME = "Wrong method call: multiple parameter names";
    public static final String INVALID_PARAMS_DECLARE = "Invalid parameters declaration";
    public static final String MISSING_BRACKETS = "Missing one or more closing brackets";
    public static final String UNSUPPORTED_LINE = "Illegal or unsupported code";
    public static final String VAR_MOD_FIRST_NOT_FOUND = "First variable in assigning doesn't exist";
    public static final String ILLEGAL_VAR_MOD = "Illegal variable assign";
    public static final String METHOD_CALL_WRONG_PARAMS_NUM = "Wrong number of parameters in method call";
    public static final String METHOD_RETURN_MISSING = "Method missing a valid 'return;' command";
    public static final String MULTIPLE_VAR_NAMES = "Multiple variables names or types";
    public static final String INVALID_VARIABLE_DECLARE = "Invalid variable assign or declaration";
    // The exception's message that will be printed:
    private String message;

    /**
     * The exception's constructor, gets one of the exception's built in messages.
     * @param message
     */
    public CodeException(String message){
        this.message = message;
    }

    /**
     * This method is used at the exception's catching, in order to print the cause for the exception's
     * throwing.
     * @return
     */
    public String getMessage(){
        return message;
    }
}
