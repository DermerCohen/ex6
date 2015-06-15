package ex6.method;

import ex6.exceptions.invalidSyntax;
import ex6.variable.Variable;
import ex6.variable.VariableFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dermersean on 15/06/2015.
 */
public class MethodChecks {
    private static final String COMMAS_EDGES = "^,|,$";//TODO code repetition?? with factory
    private static final String END_LINE = ";";

    public static void methodParamValidityCheck(String givenString) throws invalidSyntax {
        String name = givenString.substring(1,givenString.length()-1);
        String [] params = valueTranslator(name);
        for (String value: params){
            Variable var = new Variable(value+END_LINE);
        }
        }

    private static String[] valueTranslator(String givenString) throws invalidSyntax {//TODO code repetition?? with factory
        // look for commas in the edges
        String toCheck = givenString.trim();
        Pattern commasCheck = Pattern.compile(COMMAS_EDGES);
        Matcher commasChecksMatcher = commasCheck.matcher(toCheck);
        boolean commasSearch = commasChecksMatcher.find();
        if (commasSearch) {
            throw new invalidSyntax();
        }
        String[] variables = givenString.split(",");
        return variables;
    }
}