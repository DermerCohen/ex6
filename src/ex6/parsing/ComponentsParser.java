package ex6.parsing;

import ex6.blocks.MainBlock;
import ex6.exceptions.invalidSyntax;
import ex6.manager.sjavac;
import ex6.method.Method;
import ex6.method.MethodChecks;
import ex6.variable.Variable;
import ex6.variable.VariableChecks;
import ex6.variable.VariableFactory;
import org.omg.Dynamic.Parameter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class ComponentsParser {

    private final static String METHOD_BEGIN =
            "^\\s*(void)\\s+([a-zA-Z]{1}\\w*)\\s*(\\(.*\\))(\\{)$";
    private final static String COMMENT = "^\\s*[/]{2}";
    private final static String EMPTY_LINE = "^\\s*$";
    private final static String VARIABLE = "^\\s*(final )?\\s*(int |boolean |String |char ).*;";
    private final static String VAR_MOD = "^\\s*(\\w+)\\s*(=)\\s*(.+)(;)$";
    private final static int NAME_VAR_MODE = 1;
    private final static int TYPE_VAR_MODE = 3;
    private final static int METHOD_PARAM = 3;
    private final static int METHOD_NAME = 2;
    private final static int UNBALANCED_CODE = -1;

    private final static String INT = "int";
    private final static String DOUBLE = "double";
    private final static String STRING = "String";
    private final static String BOOLEAN = "boolean";
    private final static String CHAR = "char";
    private int lineCounter = 0;

    public void createExpressions (ArrayList<String> lines) throws invalidSyntax {
        BlockSlice slicer = new BlockSlice();
        MainBlock mainBlock = new MainBlock();
        VariableChecks varChecker = new VariableChecks();
        MethodChecks methodChecker = new MethodChecks();

        Pattern methodBeginPattern = Pattern.compile(METHOD_BEGIN);
        Pattern commentPattern = Pattern.compile(COMMENT);
        Pattern emptyLinePattern = Pattern.compile(EMPTY_LINE);
        Pattern variablePattern = Pattern.compile(VARIABLE);
        Pattern varModPattern = Pattern.compile(VAR_MOD);
        for (int i=0 ; i<lines.size(); i++){
            String curLine = lines.get(i);
            lineCounter++;
            Matcher methodBeginMatcher = methodBeginPattern.matcher(curLine);
            Matcher commentMatcher = commentPattern.matcher(curLine);
            Matcher emptyLineMatcher = emptyLinePattern.matcher(curLine);
            Matcher variableMatcher = variablePattern.matcher(curLine);
            Matcher varModMatcher = varModPattern.matcher(curLine);

            if (commentMatcher.matches() || emptyLineMatcher.matches()){
                continue;
            }

            else if (variableMatcher.matches()){
                mainBlock.variables =
                        VariableFactory.createVariables(curLine,mainBlock.variables);
            }

            else if (methodBeginMatcher.matches()) {
                String methodName = methodBeginMatcher.group(METHOD_NAME);
                if (!mainBlock.methods.containsKey(methodName)) {
                    String inputParam = methodBeginMatcher.group(METHOD_PARAM);
                    methodChecker.methodParamValidityCheck(inputParam);//TODO missing the part of adding the param to method
                    int endOfMethod = slicer.findMethodEnd(lines,lineCounter);
                    if (endOfMethod == UNBALANCED_CODE){
                        throw new invalidSyntax();
                    }
                    Method newMethod = new Method(lineCounter,endOfMethod);
                    lineCounter = endOfMethod;
                    mainBlock.methods.put(methodName,newMethod);
                }
            }

            else if (varModMatcher.matches()){
                String varName = varModMatcher.group(NAME_VAR_MODE);
                String type = varModMatcher.group(TYPE_VAR_MODE);
                if (mainBlock.variables.containsKey(varName)){
                    boolean origFinal = mainBlock.variables.get(varName).isFinal;
                    if (!origFinal) {
                        String typeOrig = mainBlock.variables.get(varName).type;
                        if (typeOrig.equals(INT))
                            varChecker.valueValidityCheck(type, varChecker.VALID_INT);
                        else if (typeOrig.equals(STRING))
                            varChecker.valueValidityCheck(type, varChecker.VALID_STRING);
                        else if (typeOrig.equals(BOOLEAN))
                            varChecker.valueValidityCheck(type, varChecker.VALID_BOOLEAN);
                        else if (typeOrig.equals(CHAR))
                            varChecker.valueValidityCheck(type, varChecker.VALID_CHAR);
                        else if (typeOrig.equals(DOUBLE))
                            varChecker.valueValidityCheck(type, varChecker.VALID_DOUBLE);
                        else throw new invalidSyntax();
                    }
                    else throw new invalidSyntax();
                }
                throw new invalidSyntax();
                }
            }

        }
    }