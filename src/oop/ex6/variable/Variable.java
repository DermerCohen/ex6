package oop.ex6.variable;


import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oop.ex6.blocks.BasicBlock;
import oop.ex6.exceptions.*;
import oop.ex6.parsing.*;

/**
 * This class represents a variable that was declared in the code.
 */
public class Variable
 {

     /** The variable's parameters*/
     public boolean initialized = false;
     public boolean isFinal = false;
     public String type;
     public String name;
     private static final String VARIABLE_COMPONENTS =
             "^\\s*(final )?\\s*(int|boolean|char|double|String)?\\s+([^;|^=]*)(=)?(.*;)";

     /** constants helping the methods in the class*/
     private static final int GROUP_FINAL = 1;
     private static final int GROUP_TYPE = 2;
     private static final int GROUP_NAME = 3;
     private static final int GROUP_EQUAL = 4;
     private static final int GROUP_VALUE = 5;
     private static final String INVALID_NAME = "\\b_\\b|^\\d|[^\\w]|\\s";
     private static final String EMPTY_VALUE = "^;";

     /**
      * The variable's constructor
      * @param dontCheckValue tells the constructor if the variable's value should be checked or not
      * @param givenString the line in the code that describes the variable
      * @param block the block in which the given line code is written
      * @throws CodeException
      */
     public Variable (boolean dontCheckValue, String givenString,BasicBlock block) throws CodeException {
         Pattern getGroups = Pattern.compile(VARIABLE_COMPONENTS);
         Matcher getGroupsMatcher = getGroups.matcher(givenString);
         boolean search = getGroupsMatcher.find();
         // If the pattern for a variable declaration wasn't found:
         if (search == false){
             throw new CodeException(CodeException.INVALID_VARIABLE_DECLARE);
         }
         // if the variable was declared to be final:
         if (getGroupsMatcher.group(GROUP_FINAL) != null){
             if (getGroupsMatcher.group(GROUP_EQUAL) == null){
                 throw new CodeException(CodeException.INVALID_VARIABLE_DECLARE);
             }
             isFinal = true;
         }
         type = getGroupsMatcher.group(GROUP_TYPE);
         name = nameCheck(getGroupsMatcher.group(GROUP_NAME));
         //if there is an equal sign in the declaration:
         if (getGroupsMatcher.group(GROUP_EQUAL) != null && !dontCheckValue){
             String value = getGroupsMatcher.group(GROUP_VALUE);
             // check the validity of the value:
             boolean valueValidity = valueCheck(value, type);
             if (valueValidity){
                 initialized = true;
             } else {
                 // try to look for an existing variable that the code tries to assign to the variable:
                 ArrayList<Variable> possibleVars = ComponentsParser.findVariable(value.substring(0, value
                         .length() - 1), block);
                 if (!possibleVars.isEmpty()){
                     for (Variable var : possibleVars){
                         if (VariableFactory.possiblePairs(type,var.type)){
                             if (var.initialized == true){
                                 return;
                             }
                         }
                     }
                     throw new CodeException(CodeException.INVALID_VARIABLE_DECLARE);
                     }
                 else {
                     throw new CodeException(CodeException.INVALID_VARIABLE_DECLARE);
                 }
             }
         }
    }

     /**
      * This method checks of the variable's name is valid according to the exercise definitions
      * @param givenName the name of the variable that is written in the code
      * @return the name of the variable if it is valid
      * @throws CodeException
      */
     private String nameCheck (String givenName) throws CodeException {
         String name = givenName.trim();
         Pattern nameCheck = Pattern.compile(INVALID_NAME);
         Matcher nameCheckMatcher = nameCheck.matcher(name);
         if (nameCheckMatcher.find()) {
             throw new CodeException(CodeException.INVALID_VARIABLE_DECLARE);
         }
         else return name;
     }

     /**
      * Checks if the value that is being assigned to the variable is valid
      * @param givenValue the value that is written in the code
      * @param type the type of the variable that is written in the code
      * @return true if the value is valid and false else
      * @throws CodeException
      */
     private boolean valueCheck(String givenValue, String type) throws CodeException {
         String value = givenValue;
         value = value.trim();
         emptyValueCheck(value, EMPTY_VALUE);
         value = value.substring(0,value.length()-1);
         boolean valueCheck = VariableChecks.valueValidityCheck(value,type);
         if (valueCheck == false){
             return false;
         }
         return true;
     }

     /**
      * Checks if the value that the code tries to assign to a variable is empty
      * @param givenValue the string with the value that the code tries to assign to the variable
      * @param emptyValueRegex the regex which identifies an empty string
      * @throws CodeException
      */
     private void emptyValueCheck(String givenValue,String emptyValueRegex) throws CodeException {
         Pattern valueCheck = Pattern.compile(emptyValueRegex);
         Matcher valueCheackMatcher = valueCheck.matcher(givenValue);
         boolean search = valueCheackMatcher.find();
         if (search){
             throw new CodeException(CodeException.INVALID_VARIABLE_DECLARE);
         }
     }

 }