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
        Pattern pat = Pattern.compile("^\\s*(final)?\\s*(int)?");
        String var = "   final   int    sean      =;    ";
        Variable myVar = new Variable(var);

//        Matcher mac = pat.matcher(var);
//        System.out.println(mac.find());
//        System.out.println(mac.group(1));

//        boolean a,b,c=true;
//        System.out.println(a);
//        System.out.println(b);
//        System.out.println(c);
//        System.out.println(mac.group(1));
//        int isFinal = VariableBuilder.isFinal(var);
//        System.out.println(isFinal);
//        System.out.println(var.substring(isFinal));
    }
}
