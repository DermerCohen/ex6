package ex6.blocks;

import ex6.method.Method;
import ex6.variable.Variable;

import java.util.HashSet;
import java.util.Hashtable;

/**
 * Created by dermersean on 15/06/2015.
 */
public class MainBlock implements Block {
    public Hashtable<String,Variable> variables = new Hashtable<String,Variable>();

    public Hashtable<String,Method> methods = new Hashtable<>();
}
