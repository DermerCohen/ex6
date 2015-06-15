package ex6.parsing;

import ex6.blocks.MainBlock;
import ex6.exceptions.invalidSyntax;
import ex6.manager.sjavac;
import ex6.method.Method;
import ex6.variable.Variable;
import ex6.variable.VariableChecks;
import org.omg.Dynamic.Parameter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by amircohen on 6/14/15.
 */
public class ComponentsParser {

    private final static String METHOD_BEGIN = "^\\s*void.*\\{$";
    private final static String COMMENT = "^\\s*[/]{2}";
    private final static String EMPTY_LINE = "^\\s*$";
    private final static String VARIABLE = "^\\s*(final )?\\s*(int |boolean |String |char ).*;";
    private final static String VAR_MOD = "^\\s*(\\w+)\\s*(=)\\s*(.+)(;)$";
    private final static int NAME_VAR_MODE = 1;
    private final static int TYPE_VAR_MODE = 3;
    private final static String INT = "int";
    private final static String DOUBLE = "double";
    private final static String STRING = "String";
    private final static String BOOLEAN = "boolean";
    private final static String CHAR = "char";



    public void createExpressions (ArrayList<String> lines) throws invalidSyntax {
        MainBlock mainBlock = new MainBlock();
        VariableChecks checker = new VariableChecks();

        Pattern methodBeginPattern = Pattern.compile(METHOD_BEGIN);
        Pattern commentPattern = Pattern.compile(COMMENT);
        Pattern emptyLinePattern = Pattern.compile(EMPTY_LINE);
        Pattern variablePattern = Pattern.compile(VARIABLE);
        Pattern varModPattern = Pattern.compile(VAR_MOD);
        for (int i=0 ; i<lines.size(); i++){
            String curLine = lines.get(i);
            Matcher methodBeginMatcher = methodBeginPattern.matcher(curLine);
            Matcher commentMatcher = commentPattern.matcher(curLine);
            Matcher emptyLineMatcher = emptyLinePattern.matcher(curLine);
            Matcher variableMatcher = variablePattern.matcher(curLine);
            Matcher varModMatcher = varModPattern.matcher(curLine);

            if (commentMatcher.matches() || emptyLineMatcher.matches()){
                continue;
            }

            else if (variableMatcher.matches() || varModMatcher.matches()){
//                mainBlock.variables.a
            }

            else if (methodBeginMatcher.matches()){
//                sjavac.methods.add(new Method());
            }

            else if (varModMatcher.matches()){
                String name = varModMatcher.group(NAME_VAR_MODE);
                String type = varModMatcher.group(TYPE_VAR_MODE);
                if (mainBlock.variables.contains(name)){
                    boolean origFinal = mainBlock.variables.get(name).isFinal;
                    if (!origFinal) {
                        String typeOrig = mainBlock.variables.get(name).type;
                        if (typeOrig.equals(INT))
                            checker.valueValidityCheck(type, checker.VALID_INT);
                        else if (typeOrig.equals(STRING))
                            checker.valueValidityCheck(type, checker.VALID_STRING);
                        else if (typeOrig.equals(BOOLEAN))
                            checker.valueValidityCheck(type, checker.VALID_BOOLEAN);
                        else if (typeOrig.equals(CHAR))
                            checker.valueValidityCheck(type, checker.VALID_CHAR);
                        else if (typeOrig.equals(DOUBLE))
                            checker.valueValidityCheck(type, checker.VALID_DOUBLE);
                        else throw new invalidSyntax();
                    }
                    else throw new invalidSyntax();
                }
                throw new invalidSyntax();
                }
            }

        }
    }


