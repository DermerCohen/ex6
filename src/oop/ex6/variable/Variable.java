package oop.ex6.variable;


import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oop.ex6.blocks.BasicBlock;
import oop.ex6.exceptions.*;
import oop.ex6.parsing.*;

/**
 *
 */
public class Variable
 {

//     public static final int NOT_FINAL = -1;
     public boolean initialized = false;
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
     private static final String STRING_TYPE = "String";
     private static final String INT_TYPE = "int";
     private static final String CHAR_TYPE = "char";
     private static final String DOUBLE_TYPE = "double";
     private static final String BOOLEAN = "boolean";





     public Variable (boolean dontCheckValue, String givenString,BasicBlock block) throws CodeException {
         Pattern getGroups = Pattern.compile(VARIABLE_COMPONENTS);
         Matcher getGroupsMatcher = getGroups.matcher(givenString);
         boolean search = getGroupsMatcher.find();
         if (search == false){
             throw new CodeException(CodeException.INVALID_VARIABLE_DECLARE);
         }
         if (getGroupsMatcher.group(GROUP_FINAL) != null){
             if (getGroupsMatcher.group(GROUP_EQUAL) == null){
                 throw new CodeException(CodeException.INVALID_VARIABLE_DECLARE);
             }
             isFinal = true;
         }
         type = getGroupsMatcher.group(GROUP_TYPE);
         name = nameCheck(getGroupsMatcher.group(GROUP_NAME));
         //if there is an equal sign
         if (getGroupsMatcher.group(GROUP_EQUAL) != null && !dontCheckValue){
             String value = getGroupsMatcher.group(GROUP_VALUE);
            boolean valueValidity = valueCheck(value, type);
             if (valueValidity){
                 initialized = true;
             } else {
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


     private String nameCheck (String givenString) throws CodeException {
        String name = givenString.trim();
         Pattern nameCheck = Pattern.compile(INVALID_NAME);
         Matcher nameCheckMatcher = nameCheck.matcher(name);
         if (nameCheckMatcher.find()) {
             throw new CodeException(CodeException.INVALID_VARIABLE_DECLARE);
         }
         else return name;
     }

     private boolean valueCheck(String givenString, String type) throws CodeException {
         String value = givenString;
         value = value.trim();
         emptyValueCheck(value, EMPTY_VALUE);
         value = value.substring(0,value.length()-1);
         boolean valueCheck = VariableChecks.valueValidityCheck(value,type);
         if (valueCheck == false){
             return false;
         }
         return true;
     }

     private void emptyValueCheck(String givenString,String emptyValueRegex) throws CodeException {
         Pattern valueCheck = Pattern.compile(emptyValueRegex);
         Matcher valueCheackMatcher = valueCheck.matcher(givenString);
         boolean search = valueCheackMatcher.find();
         if (search){
             throw new CodeException(CodeException.INVALID_VARIABLE_DECLARE);
         }
     }

 }