package oop.ex6.blocks;

import java.util.ArrayList;

/**
 *this class represent block from type method
 */
public class Method
extends BasicBlock
{
    public int start;
    public int end;
    private static final String METHOD_TYPE = "method";

    /**
     * construct a new block that represent a method
     * @param startIndex the line that the method start from
     * @param endIndex the line that the method end
     * @param givenParent the scope that the method is inside him
     */
    public Method (int startIndex, int endIndex, MainBlock givenParent){
        start = startIndex;
        end = endIndex;
        parent = givenParent;
        type = METHOD_TYPE;
        initialParam = new ArrayList<>();
    }
}
