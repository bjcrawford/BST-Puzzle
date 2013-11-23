package bc_binarysearchtree;

import java.util.LinkedList;

/**
 *
 * @author Brett Crawford (brett.crawford@temple.edu)
 */
public class BC_BSTNode {

    private int depth;
    private int treeIndex;
    private Integer data;
    private int screenX;
    private int screenY;
    private String imageName;
    private BC_BSTNode left;
    private BC_BSTNode right;
    
    public BC_BSTNode(int data, int depth, int treeIndex) {
        
        this.depth = depth;
        this.treeIndex = treeIndex;
        this.data = data;
        imageName = "" + (data <= 9 ? "0" : "") + data + ".png"; 
        left = null;
        right = null;
    }
    
    public int getDepth() {
        
        return depth;
    }
    
    public int getTreeIndex() {
        
        return treeIndex;
    }
    
    public Integer getData() {
        
        return data;
    }
    
    public Integer getScreenX() {
        
        return screenX;
    }
    
    public Integer getScreenY() {
        
        return screenY;
    }
    
    public String getImageName() {
        
        return imageName;
    }
    
    public BC_BSTNode getLeft() {
        
        return left;
    }
    
    public BC_BSTNode getRight() {
        
        return right;
    }
    
    public void setScreenX(int screenX) {
        
        this.screenX = screenX;
    }
    
    public void setScreenY(int screenY) {
        
        this.screenY = screenY;
    }
    
    public void setLeft(BC_BSTNode left) {
        
        this.left = left;
    }
    
    public void setRight(BC_BSTNode right) {
        
        this.right = right;
    }
    
    public boolean insertRecursive(int d) {
        
        boolean inserted;
        if(d == data)
            inserted = false;
        else if(d < data) {
            if(left != null) 
                inserted = left.insertRecursive(d);
            else {
                left = new BC_BSTNode(d, this.depth + 1, 2 * this.treeIndex + 1);
                inserted = true;
            }
        }
        else {
            if(right != null)
                inserted = right.insertRecursive(d);
            else {
                right = new BC_BSTNode(d, this.depth + 1, 2 * this.treeIndex + 2);
                inserted = true;
            }
        }
        
        return inserted;
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
            list.addLast(this.data);
        if(left != null) 
            left.dataListTraverse(mode, list);
        if(mode.matches("inorder"))
            list.addLast(this.data);
        if(right != null) 
            right.dataListTraverse(mode, list);
        if(mode.matches("postorder"))
            list.addLast(this.data);
    }
    
    public BC_BSTNode getNode(int data) {
        
        BC_BSTNode foundNode = null;
        
        if(data < this.data && this.left != null) 
            foundNode = this.left.getNode(data);
        else if(data > this.data && this.right != null) 
            foundNode = this.right.getNode(data);
        else 
            foundNode = this;
        
        return foundNode;
    }
    
    public int maxDepthTraverse() {
        
        int max = 0, leftMax = 0, rightMax = 0;
        if(depth > max)
            max = depth;
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
        
        return "\n" + "Data: " + data + " | Image: " + imageName + " | Depth: " + depth + 
               " | Index: " + treeIndex + " | x: " + screenX + " | y: " + screenY;
    }
}
