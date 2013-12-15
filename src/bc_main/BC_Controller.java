package bc_main;

import bc_binarysearchtree.BC_BSTNode;
import bc_gamenode.BC_GameNode;
import simplegui.*;

/**
 *
 * @author Brett Crawford (Brett.Crawford@temple.edu)
 */
public class BC_Controller implements SGMouseListener  {
    
    private BC_Model model;
    private BC_View view;
    
    public BC_Controller(BC_Model model, BC_View view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void reactToMouseClick(int x, int y) {
        if(BC_BSTPuzzle.DEBUG)
            System.out.println("Mouse x,y: (" + x + "," + y + ")");
        
        if(model.isStartMode()) {
            handleStartModeClick(x, y);
        }
        else if(model.isGameMode()) {
            handleGameModeClick(x, y);
        }
        else if(model.isEndMode()) {
            handleEndModeClick(x, y);
        }
        
    }
    
    private void handleStartModeClick(int x, int y) {
         
        if(x >= 79 && x <= 249) {
            if(y >= 247 && y <= 302) {
                model.setDifficulty(1);
                model.setStartMode(false);
            }
            else if(y >= 312 && y <= 365) {
                model.setDifficulty(2);
                model.setStartMode(false);
            }
            else if(y >= 374 && y <= 427) {
                model.setDifficulty(3);
                model.setStartMode(false);
            }
        }

        view.clearStartScreen();
    }
    
    private void handleGameModeClick(int x, int y) {
        BC_GameNode gameNode = model.getNodeByScreenPos(x, y, view.getNodeSize());
            
        if(gameNode != null) { // Game node mouse click

            if(gameNode.getClass() == BC_BSTNode.class) { // Game board mouse click

                if(model.isPieceSelected()) { // Input game piece
                    
                    BC_GameNode selectedNode = model.getPieceSelected();
                    String guiLabelIns = "INS" + gameNode.getNodeIndex();
                    view.drawNode(selectedNode.getData(), gameNode.getScreenX(), gameNode.getScreenY(), guiLabelIns);
                    model.getGuiLabelList().push(guiLabelIns);
                    model.getGameBoard()[gameNode.getNodeIndex()] = selectedNode.getNodeIndex();
                    model.getGameSpaces()[gameNode.getNodeIndex()] = true;
                    int nodeX = selectedNode.getScreenX();
                    int nodeY = selectedNode.getScreenY();
                    String guiLabelClr = "CLR" + gameNode.getNodeIndex();
                    view.drawBlankNode(nodeX, nodeY, guiLabelClr);
                    model.getGuiLabelList().push(guiLabelClr);
                    model.getGameSpaces()[selectedNode.getNodeIndex() + 31] = false;
                    view.clearSelection();
                    model.setPieceSelected(false);
                    model.setPieceSelectedNode(null);
                    
                    if(BC_BSTPuzzle.DEBUG) {
                        
                        System.out.println("input game piece");
                        System.out.println("BST: " + gameNode);
                        System.out.println("X: " + nodeX + " | Y: " + nodeY);
                        System.out.println(guiLabelIns + " | " + guiLabelClr);
                    }

                }
                else { // Clear game board spot
                    
                    if(model.getGameBoard()[gameNode.getNodeIndex()] != -1) {
                        
                        view.clearMove("INS" + gameNode.getNodeIndex(), "CLR" + gameNode.getNodeIndex());
                        model.getGameSpaces()[model.getGameBoard()[gameNode.getNodeIndex()]] = false;
                        model.getGameSpaces()[model.getGameBoard()[gameNode.getNodeIndex()] + 31] = true;
                        model.getGameBoard()[gameNode.getNodeIndex()] = -1;
                        
                        if(BC_BSTPuzzle.DEBUG) {
                            
                            System.out.println("filled node");
                            System.out.println("INS" + gameNode.getNodeIndex() + " | CLR" + gameNode.getNodeIndex());
                        }
                    }
                }
            }
            else { // Game piece mouse click

                if(model.isPieceSelected()) { // Clear game piece selection
                    
                    view.clearSelection();
                    model.setPieceSelected(false);
                    model.setPieceSelectedNode(null);
                }
                else { // Select game piece 
                    
                    if(model.getGameSpaces()[gameNode.getNodeIndex() + 31]) {
                        view.drawSelection(gameNode);
                        model.setPieceSelected(true);
                        model.setPieceSelectedNode(gameNode);
                    }
                    
                    if(BC_BSTPuzzle.DEBUG)
                        System.out.println("gameSpaces[" + (gameNode.getNodeIndex() + 31) + "]: " + model.getGameSpaces()[gameNode.getNodeIndex() + 31]);
                }
                
                if(BC_BSTPuzzle.DEBUG) {
                    System.out.println("SHF: " + gameNode);
                }
            }
        }
        else { // Blank space mouse click
            
            view.clearSelection();
            model.setPieceSelected(false);
            model.setPieceSelectedNode(null);

            // Check for button click
            if(x >= 455 && x <= 624 && y >= 346 && y <= 399) { // Reset 
                model.resetGameBoard(view.getGui());
                view.resetGameBoard(model.getGuiLabelList());
            }
            else if(x >= 455 && x <= 624 && y >= 410 && y <= 462) { // Quit
                view.clearAll();
                model.setGameMode(false);
                model.setGameOver(true);
            }

        }

        if(model.checkForWin()) {
            view.drawWinScreen();
        }
    }
    
    private void handleEndModeClick(int x, int y) {
        
        model.setEndMode(false);
        model.setGameOver(true);
        view.clearAll();
    }

}
