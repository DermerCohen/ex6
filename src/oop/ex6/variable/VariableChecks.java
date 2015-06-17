package oop.ex6.variable;

import oop.ex6.exceptions.CodeException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * this class represents checks that occured on variables.
 */
public class VariableChecks {

    //set regex:
    public static final String VALID_INT = "^(-?)(\\d)+$";
    public static final String VALID_DOUBLE = "^(-?)([0-9]+)(\\.[0-9]+)*$";
    public static final String VALID_CHAR = "^'.'$";
    public static final String VALID_STRING = "^\".*\"$";
    public static final String VALID_BOOLEAN = "^true$|^false$|^\\d+\\.?\\d*$";


    /**
     *this method check if the given value match the given type, using regex.
     * @param givenString
     * @param givenType
     * @return true if the string is from the given type, false otherwise
     * @throws CodeException
     */
    public static boolean valueValidityCheck(String givenString, String givenType) throws CodeException {
        givenString = givenString.trim();
        String suitableRegex = chooseRegex(givenType);
        //create a pattern and a matcher for the regex
        Pattern valueCheck = Pattern.compile(suitableRegex);
        Matcher valueCheackMatcher = valueCheck.matcher(givenString);
        boolean search = valueCheackMatcher.find();
        //the string is not from the desired type:
        if (!search) {
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * this method choose the suitable regex according to a given string
     * @param givenType
     * @return the suitable regex
     */
    public static String chooseRegex (String givenType){
        if (givenType.equals("String")){
            return VALID_STRING;
        }
        else if (givenType.equals("char")){
            return VALID_CHAR;
        }
        else if (givenType.equals("int")){
            return VALID_INT;
        }
        else if (givenType.equals("double")){
            return VALID_DOUBLE;
        }
        else if (givenType.equals("boolean")){
            return VALID_BOOLEAN;
        }
        return null;
    }
}
