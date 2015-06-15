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
     private boolean isFinal = false;
     private String type;
     private String name;
     private String value;
     private static final String VARIABLE_COMPONENTS =
             "^\\s*(final)?\\s*(int|boolean|char|double|String)?\\s*([^;|^=]*)(=)?([\\w\\s]*;)";
     private static final int GROUP_FINAL = 1;
     private static final int GROUP_TYPE = 2;
     private static final int GROUP_NAME = 3;
     private static final int GROUP_EQUAL = 4;
     private static final int GROUP_VALUE = 5;
     private static final String INVALID_NAME = "\\b_\\b|^\\d|[^\\w]|\\s";
     private static final String EMPTY_VALUE = "\\s*;";
     private static final String INVALID_INT = "[^\\d]";
     private static final String INVALID_DOUBLE = "[^\\d*\\.?\\d*]";




     public Variable(String givenString) throws invalidSyntax {
        //TODO: check variable validity
//        description = givenString;
//        int isFinalCheck = VariableBuilder.isFinal(givenString);
//        if (!(isFinalCheck == NOT_FINAL)){
//            isFinal = true;
//            givenString = givenString.substring(isFinalCheck);
//        }
//        givenString.trim();
        //get type
        // get name or names
        // equal sign or not?
         Pattern getGroups = Pattern.compile(VARIABLE_COMPONENTS);
         Matcher getGroupsMatcher = getGroups.matcher(givenString);
         getGroupsMatcher.find();
         if (getGroupsMatcher.group(GROUP_FINAL) != null){
             if (getGroupsMatcher.group(GROUP_VALUE) == null){
                 throw new invalidSyntax();
             }
             isFinal = true;
         }

         type = getGroupsMatcher.group(GROUP_TYPE);
         name = nameCheck(getGroupsMatcher.group(GROUP_NAME));
         System.out.println(getGroupsMatcher.group(GROUP_VALUE));
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

     private String valueCheck(String givenString, String type) throws invalidSyntax {
         String name = givenString;
         Pattern emptyValue = Pattern.compile(EMPTY_VALUE);
         Matcher emptyValueMatcher = emptyValue.matcher(name);
         //if the value group doesn't contain any value, throw an exception
         if (emptyValueMatcher.find()) {
             throw new invalidSyntax();
         }
         name.trim();
         int nameL = name.length();
         name = name.substring(0,nameL-2);


     }

     private void intValueCheck (String givenString) throws invalidSyntax { //TODO code repetition!!!
         Pattern valueCheck = Pattern.compile(INVALID_INT);
         Matcher valueCheckMatcher = valueCheck.matcher(givenString);
         if (valueCheckMatcher.find()) {
             throw new invalidSyntax();
         }
     }

     private void doubleCheck (String givenString) throws invalidSyntax { //TODO code repetition!!!
        Pattern valueCheck = Pattern.compile(INVALID_DOUBLE);
         Matcher valueCheckMatcher = valueCheck.matcher(givenString);
         if (valueCheckMatcher.find()){
             throw new invalidSyntax();
         }
     }


}