package oop.ex6.blocks;

import java.util.Hashtable;

/**
 * this class represent a block from type main (the main scope)
 */
public class MainBlock extends BasicBlock {
    public Hashtable<String,Method> methods = new Hashtable<>();
}
