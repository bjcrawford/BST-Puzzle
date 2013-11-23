package bc_binarysearchtree;

import bc_gamenode.BC_GameNode;
import java.util.LinkedList;

/**
 *
 * @author Brett Crawford (brett.crawford@temple.edu)
 */
public class BC_BSTNode extends BC_GameNode {

    private BC_BSTNode left;
    private BC_BSTNode right;
    
    public BC_BSTNode(int nodeDepth, int nodeIndex, int data) {
        
        super(nodeDepth, nodeIndex, data);
        left = null;
        right = null;
    }
    
    public BC_BSTNode getLeft() {
        
        return left;
    }
    
    public BC_BSTNode getRight() {
        
        return right;
    }
    
    public void setLeft(BC_BSTNode left) {
        
        this.left = left;
    }
    
    public void setRight(BC_BSTNode right) {
        
        this.right = right;
    }
    
    public void printNodeTraverse(String mode) {
        
        if(mode.matches("preorder"))
            System.out.println(this);
        if(left != null) 
            left.printNodeTraverse(mode);
        if(mode.matches("inorder"))
            System.out.println(this);
        if(right != null) 
            right.printNodeTraverse(mode);
        if(mode.matches("postorder"))
            System.out.println(this);
        
    }
    
    public void dataListTraverse(String mode, LinkedList<Integer> list) {
        
        if(mode.matches("preorder"))
            list.addLast(super.getData());
        if(left != null) 
            left.dataListTraverse(mode, list);
        if(mode.matches("inorder"))
            list.addLast(super.getData());
        if(right != null) 
            right.dataListTraverse(mode, list);
        if(mode.matches("postorder"))
            list.addLast(super.getData());
    }
    
    public BC_BSTNode getNode(int data) {
        
        BC_BSTNode foundNode = null;
        
        if(data < super.getData() && this.left != null) 
            foundNode = this.left.getNode(data);
        else if(data > super.getData() && this.right != null) 
            foundNode = this.right.getNode(data);
        else 
            foundNode = this;
        
        return foundNode;
    }
    
    public int maxDepthTraverse() {
        
        int max = 0, leftMax = 0, rightMax = 0;
        if(super.getNodeDepth() > max)
            max = super.getNodeDepth();
        if(left != null)
            leftMax = left.maxDepthTraverse();
        if(leftMax > max)
            max = leftMax;
        if(right != null)
            rightMax = right.maxDepthTraverse();
        if(rightMax > max)
            max = rightMax;
        
        return max;
        
    }
    
    @Override
    public String toString() {
        
        return "Data: " + super.getData() + " | Image: " + super.getImageName() + 
               " | Depth: " + super.getNodeDepth() + " | Index: " + super.getNodeIndex() + 
               " | x: " + super.getScreenX() + " | y: " + super.getScreenY();
    }
}
