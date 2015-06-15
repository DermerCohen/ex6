package ex6;

import ex6.exceptions.invalidSyntax;
import ex6.variable.Variable;
import ex6.variable.VariableFactory;

import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by amircohen on 6/14/15.
 */
public class test {


    public static void main (String[] args) throws invalidSyntax {
        Hashtable<String, Variable> testing = new Hashtable<>();
        String line = "     int sean =    ,5,a,b    =   5,;";
        testing = VariableFactory.createVariables(line, testing);
        System.out.println(testing.get("sean").type);
        System.out.println(testing.get("a").type);
    }
}
