package ex6;

import ex6.exceptions.invalidSyntax;
import ex6.variable.Variable;
import ex6.variable.VariableBuilder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by amircohen on 6/14/15.
 */
public class test {


    public static void main (String[] args) throws invalidSyntax {
        String a = "fal";
        Variable myVar = new Variable(a);

        System.out.println(myVar.type);
        System.out.println(myVar.name);
        System.out.println(myVar.isFinal);

    }
}
