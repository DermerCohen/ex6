package oop.ex6.blocks;

import oop.ex6.exceptions.CodeException;
import oop.ex6.variable.Variable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dermersean on 15/06/2015.
 */
public class MethodChecks {
    private static final String COMMAS_EDGES = "^,|,$";//TODO code repetition?? with factory
    private static final String END_LINE = ";";
    private static final String EMPTY_PARAMETER = "^\\s*$";


    public static Method methodParamValidityCheck(String givenString, Method method, BasicBlock block) throws
            CodeException {
        Pattern empty = Pattern.compile(EMPTY_PARAMETER);
        Matcher emptyParam = empty.matcher(givenString);
        boolean search = emptyParam.find();
        if (search){
            return method;
        }
        String [] params = valueTranslator(givenString);
        for (String value: params){
            Variable var = new Variable(true,value+END_LINE,block);
            for (Variable existsParam : method.initialParam){
                if (existsParam.name.equals(var.name)){
                    throw new CodeException(CodeException.MULTIPLE_PARAMETES_NAME);
                }
            }
            method.initialParam.add(var);
            var.initialized = true;
            method.variables.put(var.name,var);
        }
        return method;
        }

    private static String[] valueTranslator(String givenString) throws CodeException {//TODO code repetition?? with factory
        // look for commas in the edges
        String toCheck = givenString.trim();
        Pattern commasCheck = Pattern.compile(COMMAS_EDGES);
        Matcher commasChecksMatcher = commasCheck.matcher(toCheck);
        boolean commasSearch = commasChecksMatcher.find();
        if (commasSearch) {
            throw new CodeException(CodeException.INVALID_PARAMS_DECLARE);
        }
        String[] variables = givenString.split(",");
        return variables;
    }
}
