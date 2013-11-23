package bc_listnode;

import bc_binarysearchtree.BC_BSTNode;

/**
 *
 * @author Brett Crawford (Brett.Crawford@temple.edu)
 */
public class BC_ListNode {

    private int depth;
    private int treeIndex;
    private Integer data;
    private int screenX;
    private int screenY;
    private String imageName;
    
    public BC_ListNode(BC_BSTNode treeNode) {
        
        depth = treeNode.getDepth();
        treeIndex = treeNode.getTreeIndex();
        data = treeNode.getData();
        screenX = treeNode.getScreenX();
        screenY = treeNode.getScreenY();
        imageName = treeNode.getImageName();
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
    
    public void setScreenX(int screenX) {
        
        this.screenX = screenX;
    }
    
    public void setScreenY(int screenY) {
        
        this.screenY = screenY;
    }
    
    @Override
    public String toString() {
        
        return "Data: " + data + " | Image: " + imageName + " | Depth: " + depth + 
               " | Index: " + treeIndex + " | x: " + screenX + " | y: " + screenY;
    }

}
