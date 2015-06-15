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
            "^\\s*(final)?\\s*(int|boolean|char|double|String)?\\s*(.*);$";//TODO code repetition?? with variable constructor
    private static final int GROUP_FINAL = 1;//TODO code repetition?? with variable constructor
    private static final int GROUP_TYPE = 2;//TODO code repetition?? with variable constructor
    private static final int GROUP_DESCRIPTION = 3;//TODO code repetition?? with variable constructor
    private static final String COMMAS_EDGES = "^\\s*,?.*,?\\s*$";//TODO code repetition?? with variable
    private static final String END_OF_VAR = ";";



    public static Hashtable<String,Variable> createVariables (String givenString, Hashtable<String,
            Variable> blockVariables) throws invalidSyntax{
        Pattern convertString = Pattern.compile(COMPONENTS);
        Matcher convertStringMatcher = convertString.matcher(givenString);
        boolean find = convertStringMatcher.find();
        String finalValue = convertStringMatcher.group(GROUP_FINAL);
        String type = convertStringMatcher.group(GROUP_TYPE);
        String description = convertStringMatcher.group(GROUP_DESCRIPTION);
        String[] variables = valueTranslator(description);
        // now create all the variables!
        for (String variable : variables){
            String toTheConstructor = finalValue+type+variable+END_OF_VAR;
            Variable newVariable = new Variable(toTheConstructor);
            if (blockVariables.contains(newVariable.name)){
                throw new invalidSyntax();
            }
            else {
                blockVariables.put(newVariable.name,newVariable);
            }
        }
    }

    private static String[] valueTranslator(String givenString) throws invalidSyntax {
        // look for commas in the edges
        Pattern commasCheck = Pattern.compile(COMMAS_EDGES);
        Matcher commasChecksMatcher = commasCheck.matcher(givenString);
        boolean commasSearch = commasChecksMatcher.find();
        if (commasSearch) {
            throw new invalidSyntax();
        }
        String[] variables = givenString.split(",");
        return variables;
    }
}
