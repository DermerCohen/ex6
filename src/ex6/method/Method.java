package ex6.method;

import ex6.variable.Variable;
import sun.jvm.hotspot.opto.Block;

import java.util.ArrayList;

/**
 * Created by amircohen on 6/14/15.
 */
public class Method

{

    ArrayList<Block> myBlocks = new ArrayList<>();
    ArrayList<Variable> initialParam = new ArrayList<>();
    ArrayList<Variable> variables = new ArrayList<>();
    public int start;
    public int end;


    public Method (int startIndex, int endIndex){
        start = startIndex;
        end = endIndex;
    }
}
