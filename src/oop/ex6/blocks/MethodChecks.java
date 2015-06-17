package oop.ex6.blocks;

import oop.ex6.exceptions.CodeException;
import oop.ex6.variable.Variable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *this class check the validity of a method. check the validity of the parameters that
 * given.
 */
public class MethodChecks {
    private static final String COMMAS_EDGES = "^,|,$";
    private static final String END_LINE = ";";
    private static final String EMPTY_PARAMETER = "^\\s*$";


    /**
     * this method check the validity of the parameters of the method.
     * @param givenString represent the parameter
     * @param method
     * @param block
     * @return a update Method object
     * @throws CodeException
     */
    public static Method methodParamValidityCheck(String givenString, Method method,
                                                  BasicBlock block) throws
            CodeException {

        Pattern empty = Pattern.compile(EMPTY_PARAMETER);
        Matcher emptyParam = empty.matcher(givenString);
        //check if there are no parameters
        boolean search = emptyParam.find();
        if (search){
            return method;
        }

        String [] params = valueTranslator(givenString);
        //for each parameter:
        for (String value: params){
            //create a new variable
            Variable var = new Variable(true,value+END_LINE,block);
            //check if this parameter already declered in the method parameters:
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

    /**
     * this method check if a given string has comma in the beginning of the strin or in
     * the end. if it does- throw exception.
     * if not, slice the given string to string array by commas
     * @param givenString
     * @return String [] after slicing
     * @throws CodeException
     */
    private static String[] valueTranslator(String givenString) throws CodeException {
        // look for commas in the edges
        String toCheck = givenString.trim();
        Pattern commasCheck = Pattern.compile(COMMAS_EDGES);
        Matcher commasChecksMatcher = commasCheck.matcher(toCheck);
        //check if the string start or end with commas
        boolean commasSearch = commasChecksMatcher.find();
        if (commasSearch) {
            throw new CodeException(CodeException.INVALID_PARAMS_DECLARE);
        }
        String[] variables = givenString.split(",");
        return variables;
    }

}
