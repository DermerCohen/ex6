package oop.ex6.parsing;

import com.sun.org.apache.bcel.internal.classfile.Code;
import oop.ex6.blocks.*;
import oop.ex6.exceptions.CodeException;
import oop.ex6.blocks.Method;
import oop.ex6.blocks.MethodChecks;
import oop.ex6.variable.Variable;
import oop.ex6.variable.VariableChecks;
import oop.ex6.variable.VariableFactory;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *this class represent the parsers of the programs.
 * the class include two parsers - one for the main scope and one for the inner scopes.
 */
public class ComponentsParser {
    //set regexes:
    private final static String METHOD_BEGIN =
            "^\\s*(void)\\s+([a-zA-Z]{1}\\w*)\\s*(\\()(.*)(\\))\\s*(\\{)\\s*$";
    private final static String COMMENT = "^\\s*[/]{2}";
    private final static String EMPTY_LINE = "^\\s*$";
    private final static String VARIABLE = "^\\s*(final )?\\s*(int |boolean |String |char |double ).*;\\s*$";
    private final static String VAR_MOD = "^\\s*(\\w+)\\s*(=)\\s*(.+)(;)$";
    private final static String METHOD_CALL = "^(.*)(\\(.*\\))(;)\\s*$";
    private final static String IF_OR_WHILE = "^\\s*(if|while)\\s*(\\()(.*)(\\))\\s*(\\{)\\s*$";
    private final static String IF_WHILE_SPLIT = "&&|\\|\\|";
    private final static String RETURN_LINE = "^\\s*return\\s*;\\s*$";

    private final static int NAME_VAR_MODE = 1;
    private final static int VALUE_VAR_MODE = 3;
    private final static int METHOD_PARAM = 4;
    private final static int METHOD_NAME = 2;
    private final static int UNBALANCED_CODE = -1;
    private final static int IF_WHILE_BOOLEAN_EXPRESSION = 3;
    private final static int METHOD_CALL_NAME = 1;
    private final static int METHOD_CALL_PARAM = 2;
    private final static int END_OF_FILE = 1;

    private final static String BOOLEAN = "boolean";
    private final static String METHOD = "method";

    //set patterns from the regex:
    private static Pattern methodBeginPattern = Pattern.compile(METHOD_BEGIN);
    private static Pattern commentPattern = Pattern.compile(COMMENT);
    private static Pattern emptyLinePattern = Pattern.compile(EMPTY_LINE);
    private static Pattern variablePattern = Pattern.compile(VARIABLE);
    private static Pattern varModPattern = Pattern.compile(VAR_MOD);
    private static Pattern methodCalling = Pattern.compile(METHOD_CALL);
    private static Pattern ifOrWhile = Pattern.compile(IF_OR_WHILE);
    private static Pattern returnPattern = Pattern.compile(RETURN_LINE);
    private static VariableChecks varChecker = new VariableChecks();
    private static BlockSlice slicer = new BlockSlice();

    /**
     * this method represent the parser of the main scope (main block)
     * @param lines the lines of the file
     * @return a MainBlock
     * @throws CodeException
     */
    public static MainBlock createExpressions (ArrayList<String> lines) throws CodeException {
        int lineCounter = 0;
        MainBlock mainBlock = new MainBlock();
        //VariableChecks varChecker = new VariableChecks();
        MethodChecks methodChecker = new MethodChecks();
        while (lineCounter < lines.size()){ //'read' all the lines in the file
            String curLine = lines.get(lineCounter);
            lineCounter++;
            //create matcher from the regex patterns:
            Matcher methodBeginMatcher = methodBeginPattern.matcher(curLine);
            Matcher commentMatcher = commentPattern.matcher(curLine);
            Matcher emptyLineMatcher = emptyLinePattern.matcher(curLine);
            Matcher variableMatcher = variablePattern.matcher(curLine);
            Matcher varModMatcher = varModPattern.matcher(curLine);
            //if the line is a comment or an empty line, continue:
            if (commentMatcher.find() || emptyLineMatcher.find()){
                continue;
            }
            //if the line is decleration on a new value:
            else if (variableMatcher.find()){
                mainBlock.variables =
                        VariableFactory.createVariables(curLine,mainBlock); //add this
                //variable to the hashSet of variable (after checking him)
            }
            //if the line match a method declaration:
            else if (methodBeginMatcher.find()) {
                String methodName = methodBeginMatcher.group(METHOD_NAME);
                if (!mainBlock.methods.containsKey(methodName)) {//if there isnt another
                    //method with the same name:
                    int endOfMethod = slicer.findBlockEnd(lines, lineCounter - 1);//find
                    // the end of the method
                    if (endOfMethod == UNBALANCED_CODE){//if the method dont have a
                        //closing bracket
                        throw new CodeException(CodeException.MISSING_BRACKETS);
                    }
                    //create a new method
                    Method newMethod = new Method(lineCounter-1,endOfMethod,mainBlock);
                    String inputParam = methodBeginMatcher.group(METHOD_PARAM);
                    //send the method parameters to a check:
                    newMethod = methodChecker.methodParamValidityCheck(inputParam,newMethod,mainBlock);
                    lineCounter = endOfMethod;//continue to read the file from the point
                    //where the method end
                    lineCounter++;
                    mainBlock.methods.put(methodName,newMethod);//add the method to the
                    //method hashTable that the block hold
                }
            }
            //the line match a placment variable:
            else if (varModMatcher.matches()) {
                varModeChecker(mainBlock, varChecker, varModMatcher);
            }
            //the line dont match any of the options:
            else {
                throw new CodeException(CodeException.UNSUPPORTED_LINE);
            }
            }
        return mainBlock;
        }

    /**
     * this method check variables placement
     * @param block the block that hold the variable
     * @param varChecker
     * @param varModMatcher
     * @throws CodeException
     */
    private static void varModeChecker(BasicBlock block, VariableChecks varChecker, Matcher varModMatcher)
            throws CodeException {
        String varName = varModMatcher.group(NAME_VAR_MODE);
        String value = varModMatcher.group(VALUE_VAR_MODE);
        //check if this variable already exist:
        ArrayList<Variable> foundFirstVariables = findVariable(varName, block);
        if (foundFirstVariables.isEmpty()) {//the variable didnt exist, so  its illeagel
            //to use him
            throw new CodeException(CodeException.VAR_MOD_FIRST_NOT_FOUND);
        } else {
            //check if the value already exist:
            ArrayList<Variable> foundSecondVariables = findVariable(value, block);
            //if the value exist (could be few old variables with this name):
            if (!(foundSecondVariables.isEmpty())) {
                //for each option that found (right side of the equal sign):
                for (Variable firstVar : foundFirstVariables) {
                    //for each option that found for the first variable (the left side of
                    //the equal sign):
                    for (Variable secondVar : foundSecondVariables) {
                        //get the type of the variables:
                        String firstType = firstVar.type;
                        String secondType = secondVar.type;
                        //check if thers a match:
                        if (VariableFactory.possiblePairs(firstType, secondType)) {
                            //check that the first value is not final and that the second
                            //value already initialized:
                            if (firstVar.isFinal == false && secondVar.initialized == true) {
                                return;
                            }
                        }
                    }
                }
                //if the second value didnt exist:
            } else {
                //check for every option that the type match the value:
                for (Variable foundFirstVariable : foundFirstVariables) {
                    boolean check = varChecker.valueValidityCheck(value, foundFirstVariable.type);
                    if (check == true) {
                        //check that the variable is not final:
                        if (foundFirstVariable.isFinal == false) {
                            foundFirstVariable.initialized = true;
                            return;
                        }
                    }
                }
            }
            throw new CodeException(CodeException.ILLEGAL_VAR_MOD);
        }
    }

    /**
     * this class represent the parser for the smaller scope
     * @param mainBlock
     * @param block
     * @param lines the file
     * @param start the start of the block
     * @param end the end of the block
     * @throws CodeException
     */
    public static void blockParser(MainBlock mainBlock, BasicBlock block, ArrayList<String> lines, int
            start, int end) throws
            CodeException {
        boolean foundReturn = false;
        int innerLineCounter = start + 1;
        //check if the block of the method end with return statment:
        if (block.type != null && block.type.equals(METHOD)) {
            returnCheck(lines, start, end);
        }
        while (innerLineCounter < end) {
            String curLine = lines.get(innerLineCounter);
            Matcher commentMatcher = commentPattern.matcher(curLine);
            Matcher emptyLineMatcher = emptyLinePattern.matcher(curLine);
            Matcher variableMatcher = variablePattern.matcher(curLine);
            Matcher varModMatcher = varModPattern.matcher(curLine);
            Matcher methodCall = methodCalling.matcher(curLine);
            Matcher ifOrWhileMatcher = ifOrWhile.matcher(curLine);
            Matcher returnPatternMatcher = returnPattern.matcher(curLine);
            //if the current line is a comment or an empty line, continue reading:
            if (commentMatcher.find() || emptyLineMatcher.find()) {
                innerLineCounter++;
                continue;
                //if the current line is a decleration variable:
            } else if (variableMatcher.find()) {
                block.variables = VariableFactory.createVariables(curLine, block);
                //if the current line is a variable placement:
            } else if (varModMatcher.find()) {
                // Check the variable:
                varModeChecker(block, varChecker, varModMatcher);
                //if the current line is a beginning of a if/while block:
            } else if (ifOrWhileMatcher.find()) {
                String expression = ifOrWhileMatcher.group(IF_WHILE_BOOLEAN_EXPRESSION);
                String[] expressions = expression.split(IF_WHILE_SPLIT);
                //for each expression inside the if/while brackets:
                for (String exp : expressions) {
                    //if the expression is not exist already:
                    if (!(searchVariable(exp, block, BOOLEAN))) {
                        //check if its a validity boolean expression:
                        boolean valueCheck = VariableChecks.valueValidityCheck(exp, BOOLEAN);
                        if (valueCheck == false){
                            ArrayList<Variable> possibleExistingVars = findVariable(exp,block);
                            if (!possibleExistingVars.isEmpty()) {
                                for (Variable possibleVar : possibleExistingVars){
                                    boolean findVar = VariableFactory.possiblePairs
                                            (possibleVar.type,BOOLEAN);
                                    if (findVar == false || possibleVar.initialized == false){
                                        throw new CodeException(CodeException.INVALID_PARAMS_DECLARE);
                                    }
                                }
                            } else {
                                throw new CodeException(CodeException.INVALID_PARAMS_DECLARE);
                            }
                        }
                    }
                }
                IfWhileBlock newBlock = new IfWhileBlock(block);//create a new if/while
                //block
                int blockEnd = slicer.findBlockEnd(lines, innerLineCounter);
                //parse the block:
                ComponentsParser.blockParser(mainBlock, newBlock, lines, innerLineCounter, blockEnd);
                innerLineCounter = blockEnd;
                block.myBlocks.add(newBlock);
                //the current line is a call for a method:
            } else if (methodCall.find()) {
                String methodName = methodCall.group(METHOD_CALL_NAME);
                String methodParams = methodCall.group(METHOD_CALL_PARAM);
                methodParams = methodParams.substring(1, methodParams.length() - 1);
                //check if its a leagel method call:
                legalMethodCall(mainBlock, methodName, methodParams, block);
                //the current line is a return statment
            } else if (returnPatternMatcher.find()) {
                innerLineCounter++;
                continue;
                //the current line didnt match any of the legal options:
            } else {
                throw new CodeException(CodeException.UNSUPPORTED_LINE);
            }
            innerLineCounter++;
        }
    }

    /**
     * this method find if there are previous variables with the same name as the name
     * given
     * @param variableName
     * @param block
     * @return an ArrayList of the already variables existed with the same name
     */
        public static ArrayList<Variable> findVariable (String variableName,BasicBlock block){
            BasicBlock curBlock = block;
            variableName = variableName.trim();
            ArrayList<Variable> foundVariables = new ArrayList<>();
            while (curBlock != null){
                if (curBlock.variables.containsKey(variableName)){
                    foundVariables.add(curBlock.variables.get(variableName));
                } curBlock = curBlock.parent;//continue looking in the 'parent' block
            }
            return foundVariables;
        }

    /**
     * this method check if there is existing variable with a given name and a given type
     * @param givenString
     * @param block
     * @param givenType
     * @return
     * @throws CodeException
     */
        private static boolean searchVariable (String givenString, BasicBlock block, String givenType)
                throws CodeException {
            BasicBlock curBlock = block;
            givenString = givenString.trim();
            while (curBlock != null) {
                //check if there is a existing variable with the same name
                if ( curBlock.variables.containsKey(givenString)){
                   Variable foundVar = curBlock.variables.get(givenString);
                   String type = foundVar.type;
                    //check if the type matches:
                    if (VariableFactory.possiblePairs(type,givenType)) {
                       if (foundVar.initialized == true){
                           return true;
                       }
                   }else {
                       curBlock = curBlock.parent;
                   }
               }
                curBlock = curBlock.parent;
            }
            return false;
    }

    /**
     * this method check if the method call. check the name and the parameters
     * @param mainBlock
     * @param methodName
     * @param parametersUsage
     * @param curBlock
     * @throws CodeException
     */
    private static void legalMethodCall(MainBlock mainBlock, String methodName, String parametersUsage,
                                        BasicBlock curBlock) throws CodeException {
        methodName = methodName.trim();
        //check if there is method with this name:
        if (mainBlock.methods.containsKey(methodName)){
            Method foundMethod = mainBlock.methods.get(methodName);
            //if the there are no parameters in the method definition and no parameters
            //in the method call:
            if (parametersUsage.isEmpty() && foundMethod.initialParam.isEmpty()){
                return;
            }
            String[] paramUsage = VariableFactory.valueTranslator(parametersUsage);
            //if the amount of parameters in the method call is different then the
            //amount of parameters the method demends
            if (foundMethod.initialParam.size() != paramUsage.length){
                throw new CodeException(CodeException.METHOD_CALL_WRONG_PARAMS_NUM);
            } else {
                for (int i=0; i< foundMethod.initialParam.size(); i++){
                    String curParam = paramUsage[i];
                    ArrayList<Variable> exitingVariables = findVariable(curParam,curBlock);
                    if (!exitingVariables.isEmpty()){//check if there is already variable
                        //like the parameter:
                         for (Variable existingVar : exitingVariables){
                             if (existingVar.initialized){
                                 boolean pairCheck = VariableFactory.possiblePairs(existingVar.type,
                                         foundMethod.initialParam
                                         .get(i).type);
                                 if (pairCheck){//check if there are from the same type
                                     break;
                                 } else {
                                     throw new CodeException(CodeException.INVALID_PARAMS_DECLARE);
                                 }
                             }
                        }
                    } else {//check if the value is from the correct type as the method required
                        boolean newParamCheck = VariableChecks.valueValidityCheck(curParam,foundMethod
                                .initialParam.get(i).type);
                        if (newParamCheck == false) {
                            throw new CodeException(CodeException.INVALID_PARAMS_DECLARE);
                        }
                    }
                }
            }
        }
    }

    /**
     * this method check if the method include return statment in the end of her block
     * @param lines
     * @param beginIndex
     * @param endIndex
     * @throws CodeException
     */
    private static void returnCheck (ArrayList<String> lines, int beginIndex, int endIndex) throws
            CodeException {
        boolean foundReturn = false;
        int lineCounter = endIndex-END_OF_FILE;//start looking from the end of the block
        while (lineCounter > beginIndex){
            String line = lines.get(lineCounter);
            Matcher returnPatternMatcher = returnPattern.matcher(line);
            Matcher emptyLineMatcher = emptyLinePattern.matcher(line);
            boolean searchEmptyLine = emptyLineMatcher.find();
            boolean searchReturnLine = returnPatternMatcher.find();
            if (searchEmptyLine){//continue looking
                lineCounter--;
            } else if (searchReturnLine){ //found a return
                return;
            } else {
                throw new CodeException(CodeException.METHOD_RETURN_MISSING);
            }
        }
    }
}
