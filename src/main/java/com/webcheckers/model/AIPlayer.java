package com.webcheckers.model;

import com.webcheckers.model.checkers.*;

import java.util.ArrayList;
import java.util.Random;

public abstract class AIPlayer extends Player {
    /**
     * Holds the AIMoveValidator object for the AI
     */
    private AIMoveValidator aiMoveVal;

    /**
     * Creates an AI with the basic logic needed to run and determine moves.
     * The AI logic depends on what level AI is chosen by the player.
     * The AI name changes based on the level as well.
     * @param aiName Name for the AI level
     */
    protected AIPlayer(String aiName) {
        super("AI_" + aiName);
        isAI = true;
    }

    /**
     * Method used to determine the AI Move based on the logic
     * The logic varies depending on the AI level
     * @param aiMoveVal AIMoveValidator used to work out the AI logic for the next move
     * @return The final move for the AI based on the logic
     */
    public abstract Move makeMove(AIMoveValidator aiMoveVal);

    /**
     * Determines if AI is able to continue a multi-jump.
     * Based on the last jump made, check and determine if another jump is
     * possible.
     * @param sequenceJump The last jump move made by the AI
     * @return Another jump move if another connecting jump was found, else
     * return null, specifying that another connecting jump is not found.
     */
    public Move nextJump(Move sequenceJump){
        //Get the AI Moves again
        int mRow = (sequenceJump.getStart().getRow() + sequenceJump.getEnd().getRow())/2;
        int mCell = (sequenceJump.getStart().getCell() + sequenceJump.getEnd().getCell())/2;
        Position midpoint = new Position(mCell, mRow);
        aiMoveVal.addJump(midpoint);
        aiMoveVal.setlJump(sequenceJump.getEnd());
        ArrayList<Move> possibleJumps = aiMoveVal.AIMoves(CheckerPieceColor.WHITE);

        //If the list of posible jumps is null, return null.
        if(possibleJumps==null){
            return null;
        }
        //If the list is empty/doesn't contain jumps, return null.
        //Jumps are needed for this verification
        if(!(possibleJumps.get(0).isJump())){
            return null;
        }

        //For all jumps found, compare the start of those jumps with the end of the last jump made.
        //If the start and end match, add that jump move to the jump options list.
        ArrayList<Move> jumpOptions = new ArrayList<>();
        for (Move move:possibleJumps){
            if(move.getStart().equals(sequenceJump.getEnd())){
                jumpOptions.add(move);
            }
        }

        //Assuming the list contains possible jumps moves, randomly
        //choose and return a move from that list.
        if(jumpOptions.size()>0){
            return randomMove(jumpOptions);
        }
        //If the list doesn't contain any connecting jumps in the end, just return null;
        return null;
    }

    /**
     * The main run method for the AI. Assuming it is the AI turn,
     * get the board and determine move based on respective AI logic.
     * If a jump was made, check for any possible multi-jumps.
     * Validate and pass each move to the game for board update and game progression.
     */
    public void performTurn(){

        if(!getGame().checkGameStatus().equals("NONE")) {
            return;
        }
        if(getGame().checkPlayerActive(this)){
            BoardView board = getGame().getBoard(getGame().getWhitePlayer());
            BoardView oppBoard = getGame().getBoard(getGame().getRedPlayer());
            aiMoveVal = new AIMoveValidator(oppBoard, board);
            Move AIMove = makeMove(aiMoveVal);
            getGame().validateMove(AIMove);

            //If a jump move was made, begin to loop and search for next
            //potential jump move(s).
            boolean jumping = AIMove.isJump();
            aiMoveVal.setType(board.getPiece(AIMove.getStart()).getType());
            while(jumping) {
                //If the last AI move was a jump move, look for next possible jump.
                //Otherwise, stop the loop and let the method end.
                if(AIMove!=null) {
                    if (AIMove.isJump()) {

                        //Get any potential jumps with nextJump();
                        Move sequenceJump = nextJump(AIMove);

                        //If the sequenceJump is not null, replace the last move (AIMove)
                        //with this next jump.
                        //Then, get the game to validate and check the turn.
                        if (sequenceJump != null) {
                            AIMove = sequenceJump;
                            getGame().validateMove(AIMove);
                        } else {
                            jumping = false;
                        }
                    } else {
                        jumping = false;
                    }
                }
            }
            getGame().checkAndSubmitTurn();
        }
    }

    /**
     * Given a list of Moves, assuming the list is not null,
     * select and return a random move from that list.
     * @param list List of Moves to choose from
     * @return Random Move from the list
     */
    public Move randomMove(ArrayList<Move> list){
        if (list != null) {
            if (list.size() > 0) {
                int choice = (new Random().nextInt(list.size()));
                return list.get(choice);
            }
        }
        return null;
    }
}
