package oop.ex6.variable;

import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import oop.ex6.blocks.BasicBlock;
import oop.ex6.exceptions.*;

/**
 * this class represent the factory that creating variables
 */
public class VariableFactory {
    // set regex:
    private static final String COMPONENTS =
            "^\\s*(final)?\\s*(int |boolean |char |double |String )?\\s*(.*);\\s*$";
    private static final String VALUE_GETTER = "^(.*)=(.*);?\\s*$";
    private static final String COMMAS_EDGES = "^,|,$";
    private static final String END_OF_VAR = ";";
    private static final String BACK_SPACE = " ";
    //set 'magic' numbers:
    private static final int GROUP_VALUE = 2;
    private static final int GROUP_FINAL = 1;
    private static final int GROUP_TYPE = 2;
    private static final int GROUP_DESCRIPTION = 3;
    private static final boolean DONT_CHECK_VALUE = true;

    /**
     *this method create a new variables and put them in a hash table
     * @param givenString
     * @param block
     * @return the update hash table with the new variables
     * @throws CodeException
     */
    public static Hashtable<String,Variable> createVariables (String givenString,BasicBlock block) throws
            CodeException {

        Hashtable<String,Variable> finalTable = block.variables;
        Pattern convertString = Pattern.compile(COMPONENTS);
        Matcher convertStringMatcher = convertString.matcher(givenString);
        String finalValue = convertStringMatcher.group(GROUP_FINAL);
        //the variable is not final
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
                String variableName = getValueMatcher.group(1);
                consValueCheck = existanceChecker(type,finalTable,variableName);
            }
            //send it again to the constructor, after splitting between the variables
            String toTheConstructor = finalValue+BACK_SPACE+type+BACK_SPACE+variable
                    +END_OF_VAR;
            Variable newVariable = new Variable(consValueCheck, toTheConstructor,block);
            //the variable already exist
            if (finalTable.containsKey(newVariable.name)){
                throw new CodeException(CodeException.MULTIPLE_VAR_NAMES);
            }
            else {
                finalTable.put(newVariable.name, newVariable);
            }
        }
        return finalTable;
    }

    /**
     * this method check if a given string has comma in the beginning of the string or in
     * the end. if it does- throw exception.
     * if not, slice the given string to string array by commas
     * @param givenString
     * @return String [] after slicing
     * @throws CodeException
     */
    public static String[] valueTranslator(String givenString) throws CodeException {
        // look for commas in the edges
        String toCheck = givenString.trim();
        Pattern commasCheck = Pattern.compile(COMMAS_EDGES);
        Matcher commasChecksMatcher = commasCheck.matcher(toCheck);
        //check if the string start or end with commas
        boolean commasSearch = commasChecksMatcher.find();
        if (commasSearch) {
            throw new CodeException(CodeException.UNSUPPORTED_LINE);
        }
        String[] variables = toCheck.split(",");
        return variables;
    }

    /**
     * this method check if a given variable already exist and if it does, check if it is
     * from the same type
     * @param type
     * @param tableOFVariables
     * @param name
     * @return true if already exists a variable from this name and he has the same type
     * and then is value dont need to be checked, false otherwise
     * @throws CodeException
     */
    public static boolean existanceChecker(String type, Hashtable<String,Variable>
            tableOFVariables, String name) throws CodeException {
            type = type.trim();
            name = name.trim();
            //check if there is a varaiable with the same name:
            if (tableOFVariables.containsKey(name)) {
                String existType = tableOFVariables.get(name).type;
                if (possiblePairs(type, existType)) { //check if the types match
                    return DONT_CHECK_VALUE;
                } else {
                    throw new CodeException(CodeException.MULTIPLE_VAR_NAMES);
                }
            }
            return !DONT_CHECK_VALUE;
    }

    /**
     * this method check if two types are possible pairs. from the same type, or other
     * legal match (boolean can get int, for example)
     * @param firstType
     * @param secondType
     * @return true if it is a possible match, false otherwise
     * @throws CodeException
     */
    public static boolean possiblePairs (String firstType, String secondType) throws CodeException {
        if (firstType.equals("boolean")){ //boolean can get also int or double
            if (secondType.equals("String")||secondType.equals("char")){
                return false;
            } else {
                return true;
            }
        } else if (firstType.equals("int")){
            if (secondType.equals("int")){
                return true;
            } else {
                return false;
            }
        } else if (firstType.equals("String")){
            if (secondType.equals("String")){
                return true;
            } else {
                return false;
            }
        } else if (firstType.equals("char")){
            if (secondType.equals("char")){
                return true;
            } else {
                return false;
            }
        } else if (firstType.equals("double")){ //double can get also int
            if (secondType.equals("double") || secondType.equals("int")){
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}
