package bc_binarysearchtree;

import java.util.LinkedList;

/**
 *
 * @author Brett Crawford (brett.crawford@temple.edu)
 */
public class BC_BST {

    BC_BSTNode root;
    
    public BC_BST() {
        root = null;
    }
    
    public BC_BSTNode getRoot() {
        
        return root;
    }
    
    public BC_BSTNode getNode(int data) {
        
        return root.getNode(data);
    }
    
    public boolean insertIterative(int d) {
        BC_BSTNode current = root;
        boolean inserted = false;
        
        if(root == null) {
            root = new BC_BSTNode(d, 0, 0);
            inserted = true;
        }
        else {
            while(!inserted) {
                if(d == current.getData())
                    break;
                else if(d < current.getData()) {
                    if(current.getLeft() != null) 
                        current = current.getLeft();
                    else {
                        current.setLeft(new BC_BSTNode(d, current.getDepth() + 1, 2 * current.getTreeIndex() + 1));
                        inserted = true;
                    }
                }
                else {
                    if(current.getRight() != null)
                        current = current.getRight();
                    else {
                        current.setRight(new BC_BSTNode(d, current.getDepth() + 1, 2 * current.getTreeIndex() + 2));
                        inserted = true;
                    }
                }
            }
        }
        
        return inserted;
    }
    
    public boolean insertRecursive(int d) {
        boolean inserted;
        if(root == null) {
            root = new BC_BSTNode(d, 0, 0);
            inserted = true;
        }
        else 
            inserted = root.insertRecursive(d);
        
        return inserted;
    }
    
    public void printNodeTraverse(String mode) {
        
        root.printNodeTraverse(mode);
    }
    
    public LinkedList<Integer> dataListTraverse(String mode) {
        
        LinkedList<Integer> myList = new LinkedList<Integer>();
        root.dataListTraverse(mode, myList);
        
        return myList;
    }
    
    public int getMaxDepth() {
        
        return root.maxDepthTraverse();
    }
}
