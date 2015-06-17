package oop.ex6.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

import oop.ex6.blocks.MainBlock;
import oop.ex6.exceptions.CodeException;
import oop.ex6.blocks.Method;
import oop.ex6.parsing.*;
import oop.ex6.variable.Variable;

/**
 * The main method, responsible for activating all the relevant methods to the process.
 * This is the main of the program.
 */
public class Sjavac {


    public static ArrayList<Method> initialMethods;
    public static ArrayList<Method> validMethods;
    public static ArrayList<Variable> validVariables;
    static final int FILE_INDEX = 0;
    static final int ILLEGAL_CODE_EXCEPTION = 1;
    static final int IO_EXCEPTION = 2;
    static final int LEGAL_CODE = 0;


    public static void main(String[] args) throws IOException, CodeException {
            try {
                ArrayList<String> lines = FileToArrayParser.convertFileToArray(args[FILE_INDEX]);
                MainBlock mainBlock = ComponentsParser.createExpressions(lines);
                Set<String> methodsKeysSet = createIterationSet(mainBlock.methods);
                for (String methodKey : methodsKeysSet) {
                    Method curMethod = mainBlock.methods.get(methodKey);
                    ComponentsParser.blockParser(mainBlock, curMethod, lines, curMethod.start, curMethod.end);
                }
            } catch (CodeException e) {
                System.out.println(ILLEGAL_CODE_EXCEPTION);
                System.err.println(e.getMessage());
                return;
            } catch (IOException e) {
                System.out.println(IO_EXCEPTION);
                return;
            }
            System.out.println(LEGAL_CODE);
        }

    private static Set<String> createIterationSet (Hashtable<String,Method> methods){
        Set<String> myKeySet = methods.keySet();
        return myKeySet;
    }
}
