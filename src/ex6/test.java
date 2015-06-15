package ex6;

import ex6.exceptions.invalidSyntax;
import ex6.parsing.BlockSlice;
import ex6.variable.Variable;
import ex6.variable.VariableFactory;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by amircohen on 6/14/15.
 */
public class test {


    public static void main (String[] args) throws invalidSyntax {
        BlockSlice slicer = new BlockSlice();
        ArrayList<String> testing = new ArrayList<>();
        testing.add("void sean (){");
        testing.add("do this");
        testing.add("do that");
        testing.add("if (something)   {");
        testing.add("do something");
        testing.add("dgdsfh");

        testing.add("}");
        testing.add("");
        testing.add("}");
        testing.add("dgdsfh");
        System.out.println(slicer.findMethodEnd(testing,0));
    }
}
