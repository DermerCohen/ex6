package oop.ex6.exceptions;

/**
 * Created by amircohen on 6/14/15.
 */
public class CodeException extends Exception
{
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

    private String message;

    public CodeException(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
