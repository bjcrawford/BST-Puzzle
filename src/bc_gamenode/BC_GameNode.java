package bc_gamenode;

import bc_binarysearchtree.BC_BSTNode;

/**
 *
 * @author Brett Crawford (Brett.Crawford@temple.edu)
 */
public abstract class BC_GameNode {

    private int nodeDepth;
    private int nodeIndex;
    private int data;
    private int screenX;
    private int screenY;
    private boolean isImageOn;
    private String imageNameOn;
    private String imageNameOff = "blank.png";
    
    public BC_GameNode(int nodeDepth, int nodeIndex, int data) {
        
        this.nodeDepth = nodeDepth;
        this.nodeIndex = nodeIndex;
        this.data = data;
        imageNameOn = "" + (data <= 9 ? "0" : "") + data + ".png"; 
    }
    
    public int getNodeDepth() {
        
        return nodeDepth;
    }
    
    public int getNodeIndex() {
        
        return nodeIndex;
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
        
        return isImageOn ? imageNameOn : imageNameOff;
    }
    
    public void setIsImageOn(boolean isImageOn) {
        this.isImageOn = isImageOn;
    }
    
    public void setScreenX(int screenX) {
        
        this.screenX = screenX;
    }
    
    public void setScreenY(int screenY) {
        
        this.screenY = screenY;
    }
    
    @Override
    public String toString() {
        
        return "Data: " + data + " | Image: " + getImageName() + " | Depth: " + nodeDepth + 
               " | Index: " + nodeIndex + " | x: " + screenX + " | y: " + screenY;
    }

}
