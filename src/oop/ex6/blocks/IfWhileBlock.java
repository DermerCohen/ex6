package oop.ex6.blocks;

/**
 * this class represent a block from type if/while
 */
public class IfWhileBlock extends BasicBlock {

    /**
     * construct a new if/while block
     * @param givenParent the scope that the if/while scope are in him
     */
    public IfWhileBlock(BasicBlock givenParent){
        parent = givenParent;
    }
}
