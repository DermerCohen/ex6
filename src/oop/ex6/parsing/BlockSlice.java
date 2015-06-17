package oop.ex6.parsing;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by amircohen on 6/15/15.
 */
public class BlockSlice {

    private static final int BALANCED_CODE = 0;
    private static final int UNBALANCED_CODE = -1;
    private int bracketsCounter = 0;
    private int linesCounter;
    private static final String ENDS_WITH_BRACKET = "\\{\\s*$";
    private static final String CLOSING_BRACKET = "^\\s*}\\s*$";

    public int findBlockEnd(ArrayList<String> lineArray, int startIndex){
        linesCounter = startIndex;
        Pattern openBracketPattern = Pattern.compile(ENDS_WITH_BRACKET);
        Pattern closingBracketPattern = Pattern.compile(CLOSING_BRACKET);
        while (linesCounter<= lineArray.size()){
            String line = lineArray.get(linesCounter);
            Matcher openBracketsMatcher = openBracketPattern.matcher(line);
            Matcher closingBracketsMatcher = closingBracketPattern.matcher(line);
            boolean foundOpenBracket = openBracketsMatcher.find();
            boolean foundClosingBracket = closingBracketsMatcher.find();
            if (foundOpenBracket){
                bracketsCounter++;
            }
            if (foundClosingBracket){
                bracketsCounter--;
                if (bracketsCounter == BALANCED_CODE){
                    return linesCounter;
                }
            }
            linesCounter++;
        }
        return UNBALANCED_CODE;
    }
}
