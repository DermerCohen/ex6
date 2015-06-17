package oop.ex6.parsing;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * this class get the lines of the file and a start point. from that start point, slice
 * the lines to block, the beginning of the block will set according to the start point
 * (open bracket {) and the end of the block is the matching closing bracket (}).
 */
public class BlockSlice {

    private static final int BALANCED_CODE = 0;
    private static final int UNBALANCED_CODE = -1;
    private int bracketsCounter = 0;
    private int linesCounter;
    private static final String ENDS_WITH_BRACKET = "\\{\\s*$";
    private static final String CLOSING_BRACKET = "^\\s*}\\s*$";

    /**
     * this method look for the matching closing bracket to the opening brackets in the
     * start point.
     * @param lineArray the lines of the file
     * @param startIndex the place in the file where the opening brackets are
     * @return 0 if the closing brackets found, 1 otherwise
     */
    public int findBlockEnd(ArrayList<String> lineArray, int startIndex){
        linesCounter = startIndex; //start from the starting point
        Pattern openBracketPattern = Pattern.compile(ENDS_WITH_BRACKET);
        Pattern closingBracketPattern = Pattern.compile(CLOSING_BRACKET);
        while (linesCounter<= lineArray.size()){ //run until you reach the ebd of the file
            String line = lineArray.get(linesCounter);
            Matcher openBracketsMatcher = openBracketPattern.matcher(line);
            Matcher closingBracketsMatcher = closingBracketPattern.matcher(line);
            boolean foundOpenBracket = openBracketsMatcher.find();
            boolean foundClosingBracket = closingBracketsMatcher.find();
            if (foundOpenBracket){ //the current line include another open bracket
                bracketsCounter++;
            }
            if (foundClosingBracket){ //the current line include closing bracket
                bracketsCounter--;
                if (bracketsCounter == BALANCED_CODE){ //there are even number of closing
                    //brackets and opening brackets, so we find the matching
                    // closing bracket that match the first opening one
                    return linesCounter; //this is the line where the block ends
                }
            }
            linesCounter++;
        }
        return UNBALANCED_CODE;
    }
}
