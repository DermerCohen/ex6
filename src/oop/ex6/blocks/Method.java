package oop.ex6.blocks;

import java.util.ArrayList;

/**
 * Created by amircohen on 6/14/15.
 */
public class Method
extends BasicBlock
{
//    ArrayList<Variable> variables = new ArrayList<>();
    public int start;
    public int end;


    public Method (int startIndex, int endIndex, MainBlock givenParent){
        start = startIndex;
        end = endIndex;
        parent = givenParent;
        type = METHOD_TYPE;
        initialParam = new ArrayList<>();
    }
}
