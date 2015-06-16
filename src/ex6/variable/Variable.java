package ex6.variable;


import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ex6.exceptions.*;

/**
 *
 */
public class Variable
 {

//     public static final int NOT_FINAL = -1;
     public boolean isFinal = false;
     public String type;
     public String name;
     private static final String VARIABLE_COMPONENTS =
             "^\\s*(final )?\\s*(int|boolean|char|double|String)?\\s+([^;|^=]*)(=)?(.*;)"; //TODO: try to
                                                                                            // minimize this!
     private static final int GROUP_FINAL = 1;
     private static final int GROUP_TYPE = 2;
     private static final int GROUP_NAME = 3;
     private static final int GROUP_EQUAL = 4;
     private static final int GROUP_VALUE = 5;
     private static final String INVALID_NAME = "\\b_\\b|^\\d|[^\\w]|\\s";
     private static final String EMPTY_VALUE = "^;";
//     private static final String VALID_INT = "^(-?)(\\d)+$"; //TODO change this!
//     private static final String VALID_DOUBLE = "^(-?)([0-9]+)(\\.[0-9]+)*$";
//     private static final String VALID_CHAR = "^'.'$";
//     private static final String VALID_STRING = "^\".*\"$";
//     private static final String VALID_BOOLEAN = "^true$|^false$|^\\d+\\.?\\d*$";
     private static final String STRING_TYPE = "String";
     private static final String INT_TYPE = "int";
     private static final String CHAR_TYPE = "char";
     private static final String DOUBLE_TYPE = "double";
     private static final String BOOLEAN = "boolean";





     public Variable(boolean dontCheckValue, String givenString) throws invalidSyntax {
         Pattern getGroups = Pattern.compile(VARIABLE_COMPONENTS);
         Matcher getGroupsMatcher = getGroups.matcher(givenString);
         boolean search = getGroupsMatcher.find();
         if (getGroupsMatcher.group(GROUP_FINAL) != null){
             if (getGroupsMatcher.group(GROUP_EQUAL) == null){
                 throw new invalidSyntax();
             }
             isFinal = true;
         }
         type = getGroupsMatcher.group(GROUP_TYPE);
         name = nameCheck(getGroupsMatcher.group(GROUP_NAME));
         //if there is an equal sign
         if (getGroupsMatcher.group(GROUP_EQUAL) != null && !dontCheckValue){
            valueCheck(getGroupsMatcher.group(GROUP_VALUE), type);
         }
    }


     private String nameCheck (String givenString) throws invalidSyntax {
        String name = givenString.trim();
         Pattern nameCheck = Pattern.compile(INVALID_NAME);
         Matcher nameCheckMatcher = nameCheck.matcher(name);
         if (nameCheckMatcher.find()) {
             throw new invalidSyntax();
         }
         else return name;
     }

     private void valueCheck(String givenString, String type) throws invalidSyntax {
         String value = givenString;
         value = value.trim();
         emptyValueCheck(value, EMPTY_VALUE);
         value = value.substring(0,value.length()-1);
        if (type.equals(INT_TYPE)){
            VariableChecks.valueValidityCheck(value, VariableChecks.VALID_INT);
        }
         else if (type.equals(DOUBLE_TYPE)){
            VariableChecks.valueValidityCheck(value, VariableChecks.VALID_DOUBLE);
        }
         else if (type.equals(CHAR_TYPE)){
            VariableChecks.valueValidityCheck(value, VariableChecks.VALID_CHAR);
        }
         else if (type.equals(STRING_TYPE)){
            VariableChecks.valueValidityCheck(value, VariableChecks.VALID_STRING);
        }
         else if (type.equals(BOOLEAN)){
            VariableChecks.valueValidityCheck(value, VariableChecks.VALID_BOOLEAN);
        }
     }

//     private void valueValidityCheck(String givenString, String validValue) throws invalidSyntax {
//         Pattern valueCheck = Pattern.compile(validValue);
//         Matcher valueCheackMatcher = valueCheck.matcher(givenString);
//         boolean search = valueCheackMatcher.find();
//         if (!search) {
//             throw new invalidSyntax();
//         }
//     }

     private void emptyValueCheck(String givenString,String emptyValueRegex) throws invalidSyntax {
         Pattern valueCheck = Pattern.compile(emptyValueRegex);
         Matcher valueCheackMatcher = valueCheck.matcher(givenString);
         boolean search = valueCheackMatcher.find();
         if (search){
             throw new invalidSyntax();
         }
     }

 }