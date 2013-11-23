package bc_main;

import bc_gamenode.BC_GameNode;
import bc_binarysearchtree.*;
import bc_listnode.BC_ListNode;
import java.awt.Color;
import java.util.LinkedList;
import java.util.Random;
import simplegui.*;

/**
 *
 * @author Brett Crawford (brett.crawford@temple.edu)
 */
public class BC_Main implements SGMouseListener {
    
    static final boolean DEBUG = true;
    
    /* The width and height of the GUI window */
    static final int WIDTH = 640;
    static final int HEIGHT = 480;
    
    /* The difficulty level: 1-EASY, 2-MEDIUM, 3-HARD */
    static int difficulty = 2;
    
    /* The node size in pixels */
    static int nodeSize;
    
    /* The number of nodes to be used in the game */
    static int numberOfNodes;
    
    /* The BST for which the game board is layed out */
    static BC_BST gameBoardModel;
    
    /* A randomized, ordered list used to create the tree */
    static LinkedList<Integer> originalIntList;
    static LinkedList<BC_BSTNode> originalNodeList;
    
    /* A shuffled version of the original list */
    static LinkedList<BC_ListNode> shuffledNodeList;
    
    static Random rand;
    static SimpleGUI gui;
    
    private BC_Main() {
        
        showStartScreen();
        // The show start screen will launch user into the main game
        // in the finished project
        
        initialize();
        
        mainGame();
        
        debugInfo();
    }
    
    private void initialize() {
        nodeSize = 24;
        
        if(difficulty == 3) {
            numberOfNodes = 23;
        }
        else if(difficulty == 2) {
            numberOfNodes = 17;
        }
        else {
            numberOfNodes = 11;
        }
        
        rand = new Random();
        gui = new SimpleGUI(WIDTH, HEIGHT, false);
        gui.registerToMouse(this);
        
        prepareDataStructures();
    }
    
    private void prepareDataStructures() {
        
        // This ensures the random tree is never more than
        // 4 levels in height. This is to prevent drawing issues.
        do {
            createOriginalIntList(numberOfNodes);
            createTree();
        } while(gameBoardModel.getMaxDepth() > 4);
        
        createOriginalNodeList();
        createShuffledList();
    }
    
    private void createOriginalIntList(int length) {
        
        int toInsert;
        originalIntList = new LinkedList<Integer>();
        
        for(int i = 0; i < length;) {
            toInsert = rand.nextInt(100) + 1;
            if(!originalIntList.contains(new Integer(toInsert))) {
                originalIntList.add(toInsert);
                i++;
            }
        }
    }
    private void createOriginalNodeList() {
        
        originalNodeList = new LinkedList<BC_BSTNode>();
        
        for(Integer i : originalIntList) {
            originalNodeList.add(gameBoardModel.getNode(i));
        }
    }
    
    private void createShuffledList() {
        
        int nodeDepth;
        int nodeIndex;
        int data;
        BC_BSTNode bstNode;
        BC_ListNode listNode;
        shuffledNodeList = new LinkedList<BC_ListNode>();
        
        boolean[] arr = new boolean[numberOfNodes];
        for(int i = 0; i < numberOfNodes; i ++)
            arr[i] = false;
        while(!isBooleanArrayTrue(arr)) {
            int pos = rand.nextInt(numberOfNodes);
            if(arr[pos] == false) {
                bstNode = gameBoardModel.getNode(originalIntList.get(pos));
                nodeDepth = bstNode.getNodeDepth();
                nodeIndex = bstNode.getNodeIndex();
                data = bstNode.getData();
                listNode = new BC_ListNode(nodeDepth, nodeIndex, data);
                listNode.setIsImageOn(true);
                shuffledNodeList.add(listNode);
                arr[pos] = true;
            }
        }
    }
    
    private boolean isBooleanArrayTrue(boolean[] arr) {
        boolean result = true;
        for(int i = 0; i < arr.length; i++) {
            if(arr[i] == false) {
                result = false;
                break;
            }
        }
        return result;
    }
    
    private void createTree() {
        
        gameBoardModel = new BC_BST();
        
        for(Integer i : originalIntList) {
            gameBoardModel.insertIterative(i);
        }
    }
    
    private void showStartScreen() {
        // This will include 5 images
        
        // The first will be a title and a description 
        // and maybe a picture of a BST
        
        // The second, third, and fourth will be clickable 
        // images for easy, medium, and hard difficulty levels
        
        // The fifth will be my logo and credits
    }
    
    private void mainGame() {
        drawUI();
        // The main game logic will go here
    }
    
    private void drawUI() {
        // The UI will include the empty tree at the top of the screen
        
        visualizeTree(WIDTH / 2, 50, WIDTH / 4, gameBoardModel.getRoot());
        
        // At the bottom of the screen will be the node images
        // Ideally, you should be able to click and drag the nodes 
        // into a position on the tree.
        
        drawNodes();
    }
    
    private void visualizeTree(int x, int y, int childOffset, BC_BSTNode node) {
        
        
        node.setScreenX(x - nodeSize / 2);
        node.setScreenY(y);
        //gui.drawEllipse(x - nodeSize / 2, y, nodeSize, nodeSize, Color.BLACK, 1.0, 1, "");
        gui.drawImage("res/" + node.getImageName(), x - nodeSize / 2, y, nodeSize, nodeSize, node.getImageName());
        
        if(node.getLeft() != null) {
            int leftX = x - childOffset;
            int leftY = y + nodeSize * 2;
            gui.drawLine(x, y + nodeSize, leftX, leftY);
            visualizeTree(leftX, leftY, childOffset / 2, node.getLeft());
        }
        if(node.getRight() != null) {
            int rightX = x + childOffset;
            int rightY = y + nodeSize * 2;
            gui.drawLine(x, y + nodeSize, rightX, rightY);
            visualizeTree(rightX, rightY, childOffset / 2, node.getRight());
        }
            
    }
    
    private void drawNodes() {
        
    }
    
    private BC_BSTNode getNodeByScreenPos(int screenX, int screenY) {
        
        BC_BSTNode foundNode = null;
        
        for(BC_BSTNode bstNode : originalNodeList) {
            if(screenX >= bstNode.getScreenX() && screenX <= bstNode.getScreenX() + nodeSize &&
               screenY >= bstNode.getScreenY() && screenY <= bstNode.getScreenY() + nodeSize) {
                foundNode = bstNode;
                break;
            }
        }
        
        
        return foundNode;
    }

    public static void main(String[] args) {
        new BC_Main();
    }
    
    private void debugInfo() {
        
        if(DEBUG) {
            System.out.println("\n\nTree Inorder Traversal: ");
            gameBoardModel.printNodeTraverse("inorder");
            System.out.println("\n\nOriginal Int List: ");
            for(Integer i : originalIntList) 
                System.out.print(i + ", ");
            System.out.println("\n\nOriginal Node List: ");
            for(BC_BSTNode bstNode : originalNodeList) {
                System.out.println(bstNode.toString());
            }
            System.out.println("\n\nShuffled Node List: ");
            for(BC_GameNode listNode : shuffledNodeList) {
                System.out.println(listNode.toString());
            }
            
        }
    }

    @Override
    public void reactToMouseClick(int x, int y) {
        
        System.out.println("Mouse x,y: (" + x + "," + y + ")");
        System.out.println(getNodeByScreenPos(x, y));
    }
    
}
