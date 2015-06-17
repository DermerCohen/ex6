package oop.ex6.parsing;

import oop.ex6.blocks.*;
import oop.ex6.exceptions.invalidSyntax;
import oop.ex6.blocks.Method;
import oop.ex6.blocks.MethodChecks;
import oop.ex6.variable.Variable;
import oop.ex6.variable.VariableChecks;
import oop.ex6.variable.VariableFactory;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class ComponentsParser {

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
    private final static int TYPE_VAR_MODE = 3;
    private final static int VALUE_VAR_MODE = 3;
    private final static int METHOD_PARAM = 4;
    private final static int METHOD_NAME = 2;
    private final static int UNBALANCED_CODE = -1;
    private final static int IF_WHILE_BOOLEAN_EXPRESSION = 3;
    private final static int METHOD_CALL_NAME = 1;
    private final static int METHOD_CALL_PARAM = 2;



    private final static String INT = "int";
    private final static String DOUBLE = "double";
    private final static String STRING = "String";
    private final static String BOOLEAN = "boolean";
    private final static String CHAR = "char";
    private final static String RETURN = "return";
    private final static String METHOD = "method";



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




    public static MainBlock createExpressions (ArrayList<String> lines) throws invalidSyntax {
        int lineCounter = 0;
        MainBlock mainBlock = new MainBlock();
//        VariableChecks varChecker = new VariableChecks();
        MethodChecks methodChecker = new MethodChecks();
        while (lineCounter < lines.size()){
            String curLine = lines.get(lineCounter);
            lineCounter++;
            Matcher methodBeginMatcher = methodBeginPattern.matcher(curLine);
            Matcher commentMatcher = commentPattern.matcher(curLine);
            Matcher emptyLineMatcher = emptyLinePattern.matcher(curLine);
            Matcher variableMatcher = variablePattern.matcher(curLine);
            Matcher varModMatcher = varModPattern.matcher(curLine);
            if (commentMatcher.find() || emptyLineMatcher.find()){
                continue;
            }

            else if (variableMatcher.find()){
                mainBlock.variables =
                        VariableFactory.createVariables(curLine,mainBlock);
            }

            else if (methodBeginMatcher.find()) {
                String methodName = methodBeginMatcher.group(METHOD_NAME);
                if (!mainBlock.methods.containsKey(methodName)) {
                    int endOfMethod = slicer.findBlockEnd(lines, lineCounter - 1);
                    if (endOfMethod == UNBALANCED_CODE){
                        throw new invalidSyntax();
                    }
                    Method newMethod = new Method(lineCounter-1,endOfMethod,mainBlock);
                    String inputParam = methodBeginMatcher.group(METHOD_PARAM);
                    newMethod = methodChecker.methodParamValidityCheck(inputParam,newMethod,mainBlock);//TODO
                    lineCounter = endOfMethod;
                    lineCounter++;
                    mainBlock.methods.put(methodName,newMethod);
                }
            }
            else if (varModMatcher.matches()) {
                varModeChecker(mainBlock, varChecker, varModMatcher);
            }
            else {
                throw new invalidSyntax();
            }
            }
        return mainBlock;
        }

    private static void varModeChecker(BasicBlock block, VariableChecks varChecker, Matcher varModMatcher)
            throws invalidSyntax {
        String varName = varModMatcher.group(NAME_VAR_MODE);
        String value = varModMatcher.group(VALUE_VAR_MODE);
        ArrayList<Variable> foundFirstVariables = findVariable(varName, block);
        if (foundFirstVariables.isEmpty()) {
            throw new invalidSyntax();
        } else {
            ArrayList<Variable> foundSecondVariables = findVariable(value, block);
            if (!(foundSecondVariables.isEmpty())) {
                for (Variable firstVar : foundFirstVariables) {
                    for (Variable secondVar : foundSecondVariables) {
                        String firstType = firstVar.type;
                        String secondType = secondVar.type;
                        if (VariableFactory.possiblePairs(firstType, secondType)) {
                            if (firstVar.isFinal == false && secondVar.initialized == true) {
                                return;
                            }
                        }
                    }
                }

            } else {
                for (Variable foundFirstVariable : foundFirstVariables) {
                    boolean check = varChecker.valueValidityCheck(value, foundFirstVariable.type);
                    if (check == true) {
                        if (foundFirstVariable.isFinal == false) {
                            foundFirstVariable.initialized = true;
                            return;
                        }
                    }
                }
            }
            throw new invalidSyntax();
        }
    }


    public static void blockParser(MainBlock mainBlock, BasicBlock block, ArrayList<String> lines, int
            start, int end) throws
            invalidSyntax {
        boolean foundReturn = false;
        int innerLineCounter = start + 1;
        if (block.type != null && block.type.equals(METHOD)) {
            returnCheck(lines, start, end);
        }
        while (innerLineCounter < end) {
            String curLine = lines.get(innerLineCounter);//TODO: count lines
            Matcher commentMatcher = commentPattern.matcher(curLine);
            Matcher emptyLineMatcher = emptyLinePattern.matcher(curLine);
            Matcher variableMatcher = variablePattern.matcher(curLine);
            Matcher varModMatcher = varModPattern.matcher(curLine);
            Matcher methodCall = methodCalling.matcher(curLine);
            Matcher ifOrWhileMatcher = ifOrWhile.matcher(curLine);
            Matcher returnPatternMatcher = returnPattern.matcher(curLine);
            if (commentMatcher.find() || emptyLineMatcher.find()) {
                innerLineCounter++;
                continue;
            } else if (variableMatcher.find()) {
                block.variables = VariableFactory.createVariables(curLine, block);
            } else if (varModMatcher.find()) {
                // Check if the variable is in the parameters
                varModeChecker(block, varChecker, varModMatcher);
            } else if (ifOrWhileMatcher.find()) {
                String expression = ifOrWhileMatcher.group(IF_WHILE_BOOLEAN_EXPRESSION);
                String[] expressions = expression.split(IF_WHILE_SPLIT);
                for (String exp : expressions) {
                    if (!(searchVariable(exp, block, BOOLEAN))) {
                        boolean valueCheck = VariableChecks.valueValidityCheck(exp, BOOLEAN);
                        if (valueCheck == false){
                            ArrayList<Variable> possibleExistingVars = findVariable(exp,block);
                            if (!possibleExistingVars.isEmpty()) {
                                for (Variable possibleVar : possibleExistingVars){
                                    boolean findVar = VariableFactory.possiblePairs(possibleVar.type,BOOLEAN);
                                    if (findVar == false || possibleVar.initialized == false){
                                        throw new invalidSyntax();
                                    }
                                }
                            } else {
                                throw new invalidSyntax();
                            }
                        }
                    }
                }
                IfWhileBlock newBlock = new IfWhileBlock(block);
                int blockEnd = slicer.findBlockEnd(lines, innerLineCounter);
                ComponentsParser.blockParser(mainBlock, newBlock, lines, innerLineCounter, blockEnd);
                innerLineCounter = blockEnd;
                block.myBlocks.add(newBlock);
            } else if (methodCall.find()) {
                String methodName = methodCall.group(METHOD_CALL_NAME);
                String methodParams = methodCall.group(METHOD_CALL_PARAM);
                methodParams = methodParams.substring(1, methodParams.length() - 1);
                legalMethodCall(mainBlock, methodName, methodParams, block);
            } else if (returnPatternMatcher.find()) {
                innerLineCounter++;
                continue;

            } else {
                throw new invalidSyntax();
            }
            innerLineCounter++;
        }
    }

        public static ArrayList<Variable> findVariable (String variableName,BasicBlock block){
            BasicBlock curBlock = block;
            variableName = variableName.trim();
            ArrayList<Variable> foundVariables = new ArrayList<>();
            while (curBlock != null){
                if (curBlock.variables.containsKey(variableName)){
                    foundVariables.add(curBlock.variables.get(variableName));
                } curBlock = curBlock.parent;
            }
            return foundVariables;
        }


        private static boolean searchVariable (String givenString, BasicBlock block, String givenType) throws
                invalidSyntax{
            BasicBlock curBlock = block;
            givenString = givenString.trim();
            while (curBlock != null) {
               if ( curBlock.variables.containsKey(givenString)){
                   Variable foundVar = curBlock.variables.get(givenString);
                   String type = foundVar.type;
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

    private static void legalMethodCall(MainBlock mainBlock, String methodName, String parametersUsage,
                                        BasicBlock curBlock) throws invalidSyntax {
        methodName = methodName.trim();
        if (mainBlock.methods.containsKey(methodName)){
            Method foundMethod = mainBlock.methods.get(methodName);
            if (parametersUsage.isEmpty() && foundMethod.initialParam.isEmpty()){
                return;
            }
            String[] paramUsage = VariableFactory.valueTranslator(parametersUsage);
            if (foundMethod.initialParam.size() != paramUsage.length){
                throw new invalidSyntax();
            } else {
                for (int i=0; i< foundMethod.initialParam.size(); i++){
                    String curParam = paramUsage[i];
                    ArrayList<Variable> exitingVariables = findVariable(curParam,curBlock);
                    if (!exitingVariables.isEmpty()){
                         for (Variable existingVar : exitingVariables){
                             if (existingVar.initialized){
                                 boolean pairCheck = VariableFactory.possiblePairs(existingVar.type,
                                         foundMethod.initialParam
                                         .get(i).type);
                                 if (pairCheck){
                                     break;
                                 } else {
                                     throw new invalidSyntax();
                                 }
                             }
                        }
                    } else {
                        boolean newParamCheck = VariableChecks.valueValidityCheck(curParam,foundMethod
                                .initialParam.get(i).type);
                        if (newParamCheck == false) {
                            throw new invalidSyntax();
                        }
                    }
                }
            }
        }
    }

    private static void returnCheck (ArrayList<String> lines, int beginIndex, int endIndex) throws
            invalidSyntax {
        boolean foundReturn = false;
        int lineCounter = endIndex-1;
        while (lineCounter > beginIndex){
            String line = lines.get(lineCounter);
            Matcher returnPatternMatcher = returnPattern.matcher(line);
            Matcher emptyLineMatcher = emptyLinePattern.matcher(line);
            boolean searchEmptyLine = emptyLineMatcher.find();
            boolean searchReturnLine = returnPatternMatcher.find();
            if (searchEmptyLine){
                lineCounter--;
            } else if (searchReturnLine){
                return;
            } else {
                throw new invalidSyntax();
            }
        }
    }
}
