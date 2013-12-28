package bc_main;

import bc_binarysearchtree.BC_BSTNode;
import bc_gamenode.BC_GameNode;

/**
 *
 * @author Brett Crawford (Brett.Crawford@temple.edu)
 */
public class BC_BSTPuzzle {
    
    protected static final boolean DEBUG = true;
        
    private BC_Model model;
    private BC_View view;
    private BC_Controller controller;

    private BC_BSTPuzzle() {
        
        model = new BC_Model();
        view = new BC_View(model);
        controller = new BC_Controller(model, view);
        view.getGui().registerToMouse(controller);
        launchGame();
    } 
    
    private void launchGame() {
        
        while(true) {
            initialize();
            view.drawStartScreen();
            while(model.isStartMode()) {}
            mainGame();
            debugInfo();
            while(!model.isGameOver()) {}
        }
    }
    
    private void initialize() {
        
        model.setStartMode(false);
        model.setGameMode(false);
        model.setEndMode(false);
        model.setGameOver(false);
        model.setPieceSelected(false, null);
    }
    
    private void mainGame() {
        gameInitialize();
        view.drawUI(model.getNumbersOfNodes(), model.getShuffledList(), model.getBSTGameBoard().getRoot());
        model.resetGameBoard();
        model.setGameMode(true);
    }
    
    private void gameInitialize() {
        
        if(model.getDifficulty() == 3)
            model.setNumberOfNodes(23);
        else if(model.getDifficulty() == 2) 
            model.setNumberOfNodes(17);
        else 
            model.setNumberOfNodes(11);
        
        model.prepareDataStructures();
    }
    
    private void debugInfo() {
        
        if(DEBUG) {
            System.out.println("\n\nTree Inorder Traversal: ");
            model.getBSTGameBoard().printNodeTraverse("inorder");
            System.out.println("\n\nOriginal Int List: ");
            for(Integer i : model.getOriginalIntList()) 
                System.out.print(i + ", ");
            System.out.println("\n\nOriginal Node List: ");
            for(BC_BSTNode bstNode : model.getOriginalNodeList()) {
                System.out.println(bstNode.toString());
            }
            System.out.println("\n\nShuffled Node List: ");
            for(BC_GameNode listNode : model.getShuffledList()) {
                System.out.println(listNode.toString());
            }
            
        }
    }
    
    public static void main(String[] args) {
        new BC_BSTPuzzle();
    }
}
