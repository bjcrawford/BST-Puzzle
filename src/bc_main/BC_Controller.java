package bc_main;

import bc_binarysearchtree.BC_BSTNode;
import bc_gamenode.BC_GameNode;
import bc_listnode.BC_ListNode;
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

            if(gameNode.getClass() == BC_ListNode.class) { // Game piece mouse click

                if(!model.isPieceSelected()) { // No game piece selected? Select current game piece
                    
                    if(model.isGamePieceAvailable(gameNode.getNodeIndex())) {
                        view.drawSelection(gameNode);
                        model.setPieceSelected(true, gameNode);
                    }
                }
                else  // Game piece already selected? Clear game piece selection
                    clearSelection();
            }
            else { // Game board mouse click

                if(model.isPieceSelected()) { // Game piece placed
                    
                    if(model.isBoardSpotAvailable(gameNode.getNodeIndex())) {
                        BC_GameNode selectedNode = model.getPieceSelected();
                        model.insertGuess(gameNode.getNodeIndex(), selectedNode.getNodeIndex());
                        model.moveUpdate(gameNode.getNodeIndex(), selectedNode.getNodeIndex());
                        view.drawNode(selectedNode.getData(), gameNode.getScreenX(), gameNode.getScreenY(), gameNode.getNodeIndex());
                        view.drawBlankNode(selectedNode.getScreenX(), selectedNode.getScreenY(), gameNode.getNodeIndex());
                    }
                    clearSelection();
                }
                else { // Clear game board spot
                    
                    if(model.getGameBoard()[gameNode.getNodeIndex()] != -1) {
                        
                        view.clearMove("INS" + gameNode.getNodeIndex(), "CLR" + gameNode.getNodeIndex());
                        model.clearGameBoardSpot(gameNode.getNodeIndex());
                    }
                }
            }
        }
        else { // Blank space mouse click
            
            clearSelection();

            // Check for button click
            if(x >= 455 && x <= 624 && y >= 346 && y <= 399) { // Reset 
                model.resetGameBoard();
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
    
    private void clearSelection() {
        
        view.clearSelection();
        model.setPieceSelected(false, null);
    }

}
