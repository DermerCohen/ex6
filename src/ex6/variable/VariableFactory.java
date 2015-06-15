package ex6.variable;

import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by amircohen on 6/15/15.
 */
public class VariableFactory {

    private static final String COMPONENTS =
            "^\\s*(final)?\\s*(int|boolean|char|double|String)?\\s*([^;|^=]*)(=)?(.*;)";//TODO code repetition?? with variable constructor
    private static final int GROUP_FINAL = 1;//TODO code repetition?? with variable constructor
    private static final int GROUP_TYPE = 2;//TODO code repetition?? with variable constructor
    private static final int GROUP_NAME = 3;//TODO code repetition?? with variable constructor
    private static final int GROUP_EQUAL = 4;//TODO code repetition?? with variable constructor
    private static final int GROUP_VALUE = 5;//TODO code repetition?? with variable constructor


    public Hashtable<String,Variable> (String givenString){
        Pattern convertString = Pattern.compile(COMPONENTS);
        Matcher convertStringMatcher = convertString.matcher(givenString);
        boolean find = convertStringMatcher.find();
        String bool = convertStringMatcher.group(GROUP_FINAL);
        String type = convertStringMatcher.group(GROUP_TYPE);
        String names = convertStringMatcher.group(GROUP_NAME);
        String equal = convertStringMatcher.group(GROUP_EQUAL);
        String values = convertStringMatcher.group(GROUP_VALUE);

    }
}
