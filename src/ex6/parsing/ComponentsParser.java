package ex6.parsing;

import ex6.manager.sjavac;
import ex6.method.Method;
import ex6.variable.Variable;
import org.omg.Dynamic.Parameter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by amircohen on 6/14/15.
 */
public class ComponentsParser {

    private final static String METHOD_BEGIN = "^\\s*void.*\\{";
    private final static String COMMENT = "^\\s*[/]{2}";
    private final static String EMPTY_LINE = "^\\s*$";
    private final static String VARIABLE = "^\\s*(final )?\\s*(int |boolean |String |char ).*;";
    private final static String VAR_MOD = "^\\s*\\w+\\s*=\\s*.+\\s*;";

    public void createExpressions (ArrayList<String> lines){
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
//                 sjavac.variables.add(new Variable(curLine));
            }

            else if (methodBeginMatcher.matches()){
//                sjavac.methods.add(new Method());
            }

        }
    }

}
