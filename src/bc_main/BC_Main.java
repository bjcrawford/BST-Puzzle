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
    
    /* A flag to mark the app in game mode */
    static boolean gameMode;
    
    /* A flag to mark when game mode has been won */
    static boolean gameWon;
    
    /* A flag to mark when a game piece is selected */
    static boolean isPieceSelected;
    
    /* A reference to the selected game piece */
    static BC_GameNode pieceSelectedNode;
    
    /* The difficulty level: 1-EASY, 2-MEDIUM, 3-HARD */
    static int difficulty;
    
    /* The node size in pixels */
    static int nodeSize;
    
    /* The number of nodes to be used in the game */
    static int numberOfNodes;
    
    /* The BST for which the game board is layed out */
    static BC_BST gameBoardModel;
    
    /* An array for representing the user's solution for the BT. 
       array[solutionNodeIndex]:guessNodeIndex */
    static int[] gameBoard;
    
    /* An array for representing the game spaces' image states 
       array[nodeIndex + x]:imageState
       x is 0 for BT spaces, x is 31 for game piece spaces */
    static boolean[] gameSpaces;
    
    /* A randomized, ordered list used to create the tree */
    static LinkedList<Integer> originalIntList;
    static LinkedList<BC_BSTNode> originalNodeList;
    
    /* A shuffled version of the original list */
    static LinkedList<BC_ListNode> shuffledNodeList;
    
    static Random rand;
    static SimpleGUI gui;
    
    private BC_Main() {
        
        initialize();
        showStartScreen();
        mainGame();
        debugInfo();
    }
    
    private void initialize() {
        gameMode = false;
        gameWon = false;
        isPieceSelected = false;
        nodeSize = 24;
        rand = new Random();
        gui = new SimpleGUI(WIDTH, HEIGHT, false);
        gui.registerToMouse(this);
        
    }
    
    private void gameInitialize() {
        
        if(difficulty == 3)
            numberOfNodes = 23;
        else if(difficulty == 2) 
            numberOfNodes = 17;
        else 
            numberOfNodes = 11;
        
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
        
        gameBoard = new int[31];
        for(int i = 0; i < 31; i++)
            gameBoard[i] = -1;
        gameSpaces = new boolean[62];
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
        int[] mouse;
        boolean notClicked = true;
        gui.drawImage("res/title.png", 5, 0, 630, 240, "start");
        gui.drawImage("res/buttons.png", 5, 240, 315, 240, "start");
        gui.drawImage("res/rules.png", 320, 220, 315, 240, "start");
        while(notClicked) {
            mouse = gui.waitForMouseClick();
            if(mouse[0] >= 79 && mouse[0] <= 249) {
                if(mouse[1] >= 247 && mouse[1] <= 302) {
                    difficulty = 1;
                    notClicked = false;
                }
                else if(mouse[1] >= 312 && mouse[1] <= 365) {
                    difficulty = 2;
                    notClicked = false;
                }
                else if(mouse[1] >= 374 && mouse[1] <= 427) {
                    difficulty = 3;
                    notClicked = false;
                }
            }
        }
        gui.eraseAllDrawables("start");
    }
    
    private void mainGame() {
        gameMode = true;
        gameInitialize();
        drawUI();
        // The main game logic will go here
    }
    
    private void drawUI() {
        
        visualizeTree(WIDTH / 2, 50, WIDTH / 4, gameBoardModel.getRoot());
        drawShuffledNodes();
    }
    
    private void visualizeTree(int x, int y, int childOffset, BC_BSTNode node) {
        
        
        node.setScreenX(x - nodeSize / 2);
        node.setScreenY(y);
        gameSpaces[node.getNodeIndex()] = false;
        gui.drawImage("res/" + node.getImageName(), x - nodeSize / 2, y, nodeSize, nodeSize, "BST" + node.getNodeIndex());
        
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
    
    private void drawShuffledNodes() {
        
        int offset = 10;
        int x = offset;
        int y = 400;
        int spacing = ( WIDTH - (2 * offset) - (24 * numberOfNodes) ) / (numberOfNodes - 1);
        
        for(BC_ListNode listNode : shuffledNodeList) {
            listNode.setScreenX(x);
            listNode.setScreenY(y);
            gameSpaces[listNode.getNodeIndex() + 31] = true;
            gui.drawImage("res/" + listNode.getImageName(), x, y, nodeSize, nodeSize, "SHF" + listNode.getNodeIndex());
            x += nodeSize + spacing;
        }
        
    }
    
    private void drawNode(int data, int x, int y, String guiLabel) {
        String file = "" + (data <= 9 ? "0" : "") + data + ".png"; 
        gui.drawImage("res/" + file, x, y, nodeSize, nodeSize, guiLabel);
    }
    
    private void drawBlankNode(int x, int y, String guiLabel) {
        gui.drawImage("res/blank.png", x, y, nodeSize, nodeSize, guiLabel);
    }
    
    private BC_GameNode getNodeByScreenPos(int screenX, int screenY) {
        
        BC_GameNode foundNode = null;
        
        for(BC_GameNode gameNode : originalNodeList) {
            if(screenX >= gameNode.getScreenX() && screenX <= gameNode.getScreenX() + nodeSize &&
               screenY >= gameNode.getScreenY() && screenY <= gameNode.getScreenY() + nodeSize) {
                foundNode = gameNode;
                break;
            }
        }
        
        if(foundNode == null) {
            for(BC_GameNode gameNode : shuffledNodeList) {
                if(screenX >= gameNode.getScreenX() && screenX <= gameNode.getScreenX() + nodeSize &&
                   screenY >= gameNode.getScreenY() && screenY <= gameNode.getScreenY() + nodeSize) {
                    foundNode = gameNode;
                    break;
                }
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
        
        if(gameMode) {
            BC_GameNode gameNode = getNodeByScreenPos(x, y);
            
            if(gameNode != null) { // Game node mouse click
                
                if(gameNode.getClass() == BC_BSTNode.class) { // Game board mouse click
                    
                    System.out.println("BST: " + gameNode);
                    
                    if(isPieceSelected) { // Input game piece
                        System.out.println("input game piece");
                        drawNode(pieceSelectedNode.getData(), gameNode.getScreenX(), gameNode.getScreenY(), "INS" + gameNode.getNodeIndex());
                        gameBoard[gameNode.getNodeIndex()] = pieceSelectedNode.getNodeIndex();
                        gameSpaces[gameNode.getNodeIndex()] = true;
                        int nodeX = pieceSelectedNode.getScreenX();
                        int nodeY = pieceSelectedNode.getScreenY();
                        System.out.println("X: " + nodeX + " | Y: " + nodeY);
                        System.out.println("INS" + gameNode.getNodeIndex() + " | CLR" + gameNode.getNodeIndex());
                        drawBlankNode(nodeX, nodeY, "CLR" + gameNode.getNodeIndex());
                        gameSpaces[pieceSelectedNode.getNodeIndex() + 31] = false;
                        gui.eraseAllDrawables("selection");
                        isPieceSelected = false;
                        pieceSelectedNode = null;
                        
                    }
                    else { // Clear game board spot
                        if(gameBoard[gameNode.getNodeIndex()] != -1) {
                            System.out.println("filled node");
                            System.out.println("INS" + gameNode.getNodeIndex() + " | CLR" + gameNode.getNodeIndex());
                            gui.eraseAllDrawables("INS" + gameNode.getNodeIndex());
                            gameSpaces[gameBoard[gameNode.getNodeIndex()]] = false;
                            gui.eraseAllDrawables("CLR" + gameNode.getNodeIndex());
                            gameSpaces[gameNode.getNodeIndex() + 31] = true;
                            gameBoard[gameNode.getNodeIndex()] = -1;
                        }
                        else {
                            System.out.println("empty node");
                        }
                    }
                }
                else { // Game piece mouse click
                    
                    System.out.println("SHF: " + gameNode);
                    
                    if(isPieceSelected) { // Clear game piece selection
                        gui.eraseAllDrawables("selection");
                        isPieceSelected = false;
                        pieceSelectedNode = null;
                    }
                    else { // Select game piece 
                        if(gameSpaces[gameNode.getNodeIndex() + 31]) {
                            gui.drawFilledEllipse(gameNode.getScreenX(), gameNode.getScreenY(), nodeSize, nodeSize, Color.ORANGE, 0.5, "selection");
                            isPieceSelected = true;
                            pieceSelectedNode = gameNode;
                        }
                    }
                }
            }
            else { // Blank space mouse click
                gui.eraseAllDrawables("selection");
                isPieceSelected = false;
                pieceSelectedNode = null;
                System.out.println("blank space");
            }
            
            checkForWin();
        }
        
    }
    
    private void checkForWin() {
        
        boolean won = true;
        
        System.out.println("Checking for win:");
        
        for(BC_GameNode gameNode : originalNodeList) {
            System.out.println("gameBoard[" + gameNode.getNodeIndex() + "]: " + 
                               gameBoard[gameNode.getNodeIndex()] + " | gameNode.getNodeIndex(): " + 
                               gameNode.getNodeIndex());
            if(gameBoard[gameNode.getNodeIndex()] != gameNode.getNodeIndex()) {
                won = false;
                break;
            }
        }
        
        if(won) {
            System.out.println("You Win!");
            gameWon = true;
            gameMode = false;
        }
    }
    
}
