package ex6.variable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ex6.exceptions.*;

/**
 * Created by amircohen on 6/15/15.
 */
public class VariableFactory {
    private static final String COMPONENTS =
            "^\\s*(final)?\\s*(int |boolean |char |double |String )?\\s*(.*);\\s*$";//TODO code repetition??
            // with variable constructor
    private static final String VALUE_GETTER = "^(.*)=(.*);?\\s*$";
    private static final int GROUP_VALUE = 2;//TODO code repetition?? with variable constructor
    private static final int GROUP_FINAL = 1;//TODO code repetition?? with variable constructor
    private static final int GROUP_TYPE = 2;//TODO code repetition?? with variable constructor
    private static final int GROUP_DESCRIPTION = 3;//TODO code repetition?? with variable constructor
    private static final String COMMAS_EDGES = "^,|,$";//TODO code repetition?? with variable
    private static final String END_OF_VAR = ";";
    private static final String BACK_SPACE = " ";
    private static final boolean DONT_CHECK_VALUE = true;




    public static Hashtable<String,Variable> createVariables (String givenString, Hashtable<String,
            Variable> blockVariables) throws invalidSyntax{
        Hashtable<String,Variable> finalTable = blockVariables;
        Pattern convertString = Pattern.compile(COMPONENTS);
        Matcher convertStringMatcher = convertString.matcher(givenString);
        boolean find = convertStringMatcher.find();
        String finalValue = convertStringMatcher.group(GROUP_FINAL);
        if (finalValue == null){
            finalValue = BACK_SPACE;
        }
        String type = convertStringMatcher.group(GROUP_TYPE);
        String description = convertStringMatcher.group(GROUP_DESCRIPTION);
        String[] variables = valueTranslator(description);
        // now create all the variables!
        for (String variable : variables){
            Pattern getValue = Pattern.compile(VALUE_GETTER);
            Matcher getValueMatcher = getValue.matcher(variable);
            boolean search = getValueMatcher.find();
            boolean consValueCheck = !DONT_CHECK_VALUE;
            if (search){
                String newValue = getValueMatcher.group(GROUP_VALUE);
                consValueCheck = existanceChecker(type,newValue,finalTable);
            }
            String toTheConstructor = finalValue+BACK_SPACE+type+BACK_SPACE+variable+END_OF_VAR;
            Variable newVariable = new Variable(consValueCheck, toTheConstructor);
            boolean checking = finalTable.containsKey(newVariable.name);
            if (finalTable.containsKey(newVariable.name)){
                throw new invalidSyntax();
            }
            else {
                finalTable.put(newVariable.name, newVariable);
            }
        }
        return finalTable;
    }


    private static String[] valueTranslator(String givenString) throws invalidSyntax {
        // look for commas in the edges
        String toCheck = givenString.trim();
        Pattern commasCheck = Pattern.compile(COMMAS_EDGES);
        Matcher commasChecksMatcher = commasCheck.matcher(toCheck);
        boolean commasSearch = commasChecksMatcher.find();
        if (commasSearch) {
            throw new invalidSyntax();
        }
        String[] variables = toCheck.split(",");
        return variables;
    }

    public static boolean existanceChecker(String type, String value, Hashtable<String,Variable>
            tableOFVariables) throws invalidSyntax {
            value = value.trim();
            type = type.trim();
            if (tableOFVariables.containsKey(value)) {
                String existType = tableOFVariables.get(value).type;
                if (possiblePairs(type, existType)) {
                    return DONT_CHECK_VALUE;
                } else {
                    throw new invalidSyntax();
                }
            }
            return !DONT_CHECK_VALUE;
    }

    private static boolean possiblePairs (String firstType, String secondType) throws invalidSyntax {
        if (firstType.equals("boolean")){
            if (secondType.equals("String")||secondType.equals("char")){
                throw new invalidSyntax();
            } else {
                return true;
            }
        } else if (firstType.equals("int")){
            if (secondType.equals("int")){
                return true;
            } else {
                throw new invalidSyntax();
            }
        } else if (firstType.equals("String")){
            if (secondType.equals("String")){
                return true;
            } else {
                throw new invalidSyntax();
            }
        } else if (firstType.equals("char")){
            if (secondType.equals("char")){
                return true;
            } else {
                throw new invalidSyntax();
            }
        } else if (firstType.equals("double")){
            if (secondType.equals("double") || secondType.equals("int")){
                return true;
            } else {
                throw new invalidSyntax();
            }
        }
        return false;
    }
}
