package ex6.variable;

import ex6.exceptions.invalidSyntax;

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

    public static void valueValidityCheck(String givenString, String validValue) throws invalidSyntax {
        Pattern valueCheck = Pattern.compile(validValue);
        Matcher valueCheackMatcher = valueCheck.matcher(givenString);
        boolean search = valueCheackMatcher.find();
        if (!search) {
            throw new invalidSyntax();
        }
    }
}
