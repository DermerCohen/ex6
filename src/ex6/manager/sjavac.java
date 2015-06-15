package ex6.manager;

import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import ex6.exceptions.invalidSyntax;
import ex6.method.Method;
import ex6.parsing.*;
import ex6.variable.Variable;

/**
 * The main method, responsible for activating all the relevant methods to the process.
 * This is the manager of the program.
 */
public class sjavac {


    public static ArrayList<Method> initialMethods;
    public static ArrayList<Method> validMethods;
    public static ArrayList<Variable> validVariables;
    static final int FILE_INDEX = 0;

    public static void main(String[] args) throws FileNotFoundException, invalidSyntax {
        String dir = "/Users/amircohen/Desktop/ex6_files/tests/test00";
        for (int i=1; i<10; i++){
            dir = dir+i+"sjava";
            ArrayList<String> lines = FileToArrayParser.convertFileToArray(args[FILE_INDEX]);
            ComponentsParser.createExpressions(lines);
        }
//        ArrayList<String> lines = FileToArrayParser.convertFileToArray(args[FILE_INDEX]);
//        ComponentsParser.createExpressions(lines);
    }
}
