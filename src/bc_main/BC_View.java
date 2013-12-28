package bc_main;

import bc_binarysearchtree.BC_BSTNode;
import bc_gamenode.BC_GameNode;
import bc_listnode.BC_ListNode;
import java.awt.Color;
import java.util.LinkedList;
import simplegui.*;

/**
 *
 * @author Brett Crawford (Brett.Crawford@temple.edu)
 */
public class BC_View {
    
    /* The width and height of the GUI window */
    private final int WIDTH = 640;
    private final int HEIGHT = 480;
    
    /* The node size in pixels */
    private final int NODESIZE = 24;;
    
    private BC_Model model;
    private SimpleGUI gui;
    
    public BC_View(BC_Model model) {
        this.model = model;
        gui = new SimpleGUI(WIDTH, HEIGHT, false);
    }
    
    public int getNodeSize() {
        
        return NODESIZE;
    }
    
    public SimpleGUI getGui() {
        
        return gui;
    }
    
    protected void drawStartScreen() {
        model.setStartMode(true);
        gui.drawImage("res/title.png", 5, 0, 630, 240, "start");
        gui.drawImage("res/buttons.png", 5, 240, 315, 240, "start");
        gui.drawImage("res/rules.png", 320, 220, 315, 240, "start");
    }
    
    protected void clearStartScreen() {
        gui.eraseAllDrawables("start");
    }
    
    protected void drawUI(int numberOfNodes, LinkedList<BC_ListNode> shuffledNodeList, BC_BSTNode treeRootNode) {
        
        visualizeTree(WIDTH / 2, 30, WIDTH / 4, treeRootNode);
        drawShuffledNodes(numberOfNodes, shuffledNodeList);
        gui.drawImage("res/instructions.png", 10, 335, 430, 140, "instuctions");
        gui.drawImage("res/buttons2.png", 445, 340, 188, 130, "buttons2");
    }
    
    private void visualizeTree(int x, int y, int childOffset, BC_BSTNode node) {
        
        node.setScreenX(x - NODESIZE / 2);
        node.setScreenY(y);
        
        gui.drawImage("res/" + node.getImageName(), x - NODESIZE / 2, y, NODESIZE, NODESIZE, "BST" + node.getNodeIndex());
        
        if(node.getLeft() != null) {
            int leftX = x - childOffset;
            int leftY = y + NODESIZE * 2;
            gui.drawLine(x, y + NODESIZE, leftX, leftY);
            visualizeTree(leftX, leftY, childOffset / 2, node.getLeft());
        }
        if(node.getRight() != null) {
            int rightX = x + childOffset;
            int rightY = y + NODESIZE * 2;
            gui.drawLine(x, y + NODESIZE, rightX, rightY);
            visualizeTree(rightX, rightY, childOffset / 2, node.getRight());
        }
    }
    
    private void drawShuffledNodes(int numberOfNodes, LinkedList<BC_ListNode> shuffledNodeList) {
        
        int offset = 10;
        int x = offset;
        int y = 275;
        int spacing = ( WIDTH - (2 * offset) - (24 * numberOfNodes) ) / (numberOfNodes - 1);
        
        for(BC_ListNode listNode : shuffledNodeList) {
            listNode.setScreenX(x);
            listNode.setScreenY(y);
            
            gui.drawImage("res/" + listNode.getImageName(), x, y, NODESIZE, NODESIZE, "SHF" + listNode.getNodeIndex());
            x += NODESIZE + spacing;
        }
    }
    
    protected void drawNode(int data, int x, int y, int boardIndex) {
        String file = "" + (data <= 9 ? "0" : "") + data + ".png"; 
        gui.drawImage("res/" + file, x, y, NODESIZE, NODESIZE, "INS" + boardIndex);
    }
    
    protected void drawBlankNode(int x, int y, int boardIndex) {
        gui.drawImage("res/blank.png", x, y, NODESIZE, NODESIZE, "CLR" + boardIndex);
    }
    protected void drawSelection(BC_GameNode gameNode) {
        gui.drawFilledEllipse(gameNode.getScreenX(), gameNode.getScreenY(), NODESIZE, NODESIZE, Color.ORANGE, 0.5, "selection");
    }
    
    protected void clearAll() {
        gui.eraseAllDrawables();
    }
    
    protected void clearSelection() {
        gui.eraseAllDrawables("selection");
    }
    
    protected void clearMove(String ins, String clr) {
        gui.eraseAllDrawables(ins);
        gui.eraseAllDrawables(clr);
    }
    
    protected void resetGameBoard(LinkedList<String> guiLabelList) {
        while(!guiLabelList.isEmpty()) {
            gui.eraseAllDrawables(guiLabelList.pop());
        }
    }
        
    protected void drawWinScreen() {
        gui.eraseAllDrawables();
        gui.drawImage("res/win.png", 0, 0, 640, 480, "win");
    }

}
