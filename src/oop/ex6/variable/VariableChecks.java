package oop.ex6.variable;

import oop.ex6.exceptions.invalidSyntax;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by amircohen on 6/15/15.
 */
public class VariableChecks {

    public static final String VALID_INT = "^(-?)(\\d)+$"; //TODO change this!
    public static final String VALID_DOUBLE = "^(-?)([0-9]+)(\\.[0-9]+)*$";
    public static final String VALID_CHAR = "^'.'$";
    public static final String VALID_STRING = "^\".*\"$";
    public static final String VALID_BOOLEAN = "^true$|^false$|^\\d+\\.?\\d*$";



    public static boolean valueValidityCheck(String givenString, String givenType) throws invalidSyntax {
        givenString = givenString.trim();
        String suitableRegex = chooseRegex(givenType);
        Pattern valueCheck = Pattern.compile(suitableRegex);
        Matcher valueCheackMatcher = valueCheck.matcher(givenString);
        boolean search = valueCheackMatcher.find();
        if (!search) {
            return false;
        }
        else {
            return true;
        }
    }

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
