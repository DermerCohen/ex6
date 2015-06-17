package oop.ex6.blocks;

import oop.ex6.variable.Variable;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * this class represent the basic block. block represent a single scope.
 *
 */
public class BasicBlock {
    public String type;//the type of the block (main,method or if/while)
    public Hashtable<String,Variable> variables = new Hashtable<>();//variables that
    //the block hold
    public BasicBlock parent = null; //inside which scope this scope is
    public ArrayList<BasicBlock> myBlocks = new ArrayList<>();//the scopes that are inside
    //this scope
    public ArrayList<Variable> initialParam;



}
