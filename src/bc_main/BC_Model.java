package bc_main;

import bc_gamenode.BC_GameNode;
import bc_binarysearchtree.*;
import bc_listnode.BC_ListNode;
import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author Brett Crawford (brett.crawford@temple.edu)
 */
public class BC_Model {
    
    // ---------------- Properties ----------------
    
    /* Flags to mark the game in various modes */
    private boolean startMode;
    private boolean gameMode;
    private boolean endMode;
    private boolean gameOver;
    
    /* The difficulty level: 1-EASY, 2-MEDIUM, 3-HARD */
    private int difficulty;
    
    /* The number of nodes to be used in the game */
    private int numberOfNodes;
    
    /* A flag to mark when a game piece is selected */
    private boolean pieceSelected;
    
    /* A reference to the selected game piece */
    private BC_GameNode pieceSelectedNode;
    
    /* The BST for which the game board is layed out */
    private BC_BST gameBoardModel;
    
    /* An array for representing the user's solution for the BST. 
       array[solutionNodeIndex]:guessNodeIndex */
    private int[] gameBoard;
    
    /* An array for representing the game spaces' image states 
       and piece availability
       array[nodeIndex + x]:imageState
       x is 0 for BST spaces, x is 31 for game piece spaces */
    private boolean[] gameSpaces;
    
    /* A list containing the string labels of all the images used
       to alter game spaces' image states. Used to reset the gameboard */
    private LinkedList<String> guiLabelList;
    
    /* A randomized, ordered list used to create the tree */
    private LinkedList<Integer> originalIntList;
    
    /* A list that holds references to the BT nodes */
    private LinkedList<BC_BSTNode> originalNodeList;
    
    /* A shuffled version of the original list */
    private LinkedList<BC_ListNode> shuffledNodeList;
    
    /* For generating random values */
    private Random rand;
    
    // ---------------- Constructor ----------------
    
    public BC_Model() {
        
        // gameBoardModel is instatiated in createTree method
        gameBoard = new int[31];
        gameSpaces = new boolean[62];
        guiLabelList = new LinkedList<String>();
        // originalIntList is instantiated in createOriginalIntList method
        originalNodeList = new LinkedList<BC_BSTNode>();
        shuffledNodeList = new LinkedList<BC_ListNode>();
        rand = new Random();
    }
    
    // ---------------- Accessors ----------------
    
    public boolean isStartMode() {
        
        return startMode;
    }
    
    public boolean isGameMode() {
        
        return gameMode;
    }
    
    public boolean isEndMode() {
        
        return endMode;
    }
    
    public boolean isGameOver() {
        
        return gameOver;
    }
    
    public boolean isPieceSelected() {
        
        return pieceSelected;
    }
    
    public boolean isBoardSpotAvailable(int pieceIndex) {
        
        return !gameSpaces[pieceIndex];
    }
    
    public boolean isGamePieceAvailable(int pieceIndex) {
        
        return gameSpaces[pieceIndex + 31];
    }
    
    public int getDifficulty() {
        
        return difficulty;
    }
    
    public int getNumbersOfNodes() {
        
        return numberOfNodes;
    }
    
    public BC_GameNode getPieceSelected() {
        
        return pieceSelectedNode;
    }
    
    public int[] getGameBoard() {
        
        return gameBoard;
    }
    
    public boolean[] getGameSpaces() {
        
        return gameSpaces;
    }
    
    public LinkedList<String> getGuiLabelList() {
        
        return guiLabelList;
    }
    
    public LinkedList<Integer> getOriginalIntList() {
        
        return originalIntList;
    }
    
    public LinkedList<BC_BSTNode> getOriginalNodeList() {
        
        return originalNodeList;
    }
    
    public LinkedList<BC_ListNode> getShuffledList() {
        
        return shuffledNodeList;
    }
    
    public BC_BST getBSTGameBoard() {
        
        return gameBoardModel;
    }
    
    // ---------------- Mutators ----------------
    
    public void setStartMode(boolean startMode) {
        this.startMode = startMode;
    }
    
    public void setGameMode(boolean gameMode) {
        this.gameMode = gameMode;
    }
    
    public void setEndMode(boolean endMode) {
        this.endMode = endMode;
    }
    
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
    
    public void setPieceSelected(boolean pieceSelected, BC_GameNode pieceSelectedNode) {
        this.pieceSelected = pieceSelected;
        this.pieceSelectedNode = pieceSelectedNode;
    }
    
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
    
    public void setNumberOfNodes(int numberOfNodes) {
        this.numberOfNodes = numberOfNodes;
    }
    
    // ---------------- Methods ----------------
    
    protected void prepareDataStructures() {
        
        // This ensures the random tree is never more than
        // 4 levels in height. This is to prevent drawing issues.
        do {
            createOriginalIntList(numberOfNodes);
            createTree();
        } while(gameBoardModel.getMaxDepth() > 4);
        
        createOriginalNodeList();
        createShuffledList();
        
        for(int i = 0; i < 31; i++)
            gameBoard[i] = -1;
    }
    
    private void createOriginalIntList(int length) {
        
        originalIntList = new LinkedList<Integer>();
        int toInsert;
        
        for(int i = 0; i < length;) {
            toInsert = rand.nextInt(100) + 1;
            if(!originalIntList.contains(new Integer(toInsert))) {
                originalIntList.add(toInsert);
                i++;
            }
        }
    }
    
    private void createTree() {
        
        gameBoardModel = new BC_BST();
        
        for(Integer i : originalIntList) {
            gameBoardModel.insertIterative(i);
        }
    }
    
    private void createOriginalNodeList() {
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
    
    protected void insertGuess(int solutionNodeIndex, int guessNodeIndex) {
        gameBoard[solutionNodeIndex] = guessNodeIndex;
    }
    
    protected void moveUpdate(int boardIndex, int pieceIndex) {
        guiLabelList.push("INS" + boardIndex);
        gameSpaces[boardIndex] = true;
        guiLabelList.push("CLR" + boardIndex);
        gameSpaces[pieceIndex + 31] = false;
    }
    
    protected void clearGameBoardSpot(int boardIndex) {
        gameSpaces[boardIndex] = false;
        gameSpaces[gameBoard[boardIndex] + 31] = true;
        gameBoard[boardIndex] = -1;
    }
    
    protected void resetGameBoard() {
        for(int i = 0; i < 31; i++) {
            gameBoard[i] = -1;
            gameSpaces[i] = false;
            gameSpaces[i + 31] = true;
        }
    }
    
    protected BC_GameNode getNodeByScreenPos(int screenX, int screenY, int nodeSize) {
        
        BC_GameNode foundNode = null;
        
        // Check for game board node
        for(BC_GameNode gameNode : originalNodeList) {
            if(screenX >= gameNode.getScreenX() && screenX <= gameNode.getScreenX() + nodeSize &&
               screenY >= gameNode.getScreenY() && screenY <= gameNode.getScreenY() + nodeSize) {
                foundNode = gameNode;
                break;
            }
        }
        
        // Check for game piece node
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
    
    protected boolean checkForWin() {
        
        boolean won = true;
        for(BC_GameNode gameNode : originalNodeList) {
            if(gameBoard[gameNode.getNodeIndex()] != gameNode.getNodeIndex()) {
                won = false;
                break;
            }
        }
        
        if(won) {
            gameMode = false;
            endMode = true;
        }
        
        return won;
    }
}
