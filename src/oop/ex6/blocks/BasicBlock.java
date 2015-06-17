package oop.ex6.blocks;

import oop.ex6.variable.Variable;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by amircohen on 6/15/15.
 */
public class BasicBlock {
    public String type;
    public Hashtable<String,Variable> variables = new Hashtable<>();
    public BasicBlock parent = null;
    public ArrayList<BasicBlock> myBlocks = new ArrayList<>();
    public static final String METHOD_TYPE = "method";
    public ArrayList<Variable> initialParam;


}
