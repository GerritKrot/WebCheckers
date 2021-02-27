package com.webcheckers.appl;

import com.webcheckers.model.AIPlayer;
import com.webcheckers.model.checkers.*;

import java.util.ArrayList;

public class HardAI extends AIPlayer {

    /**
     * Holds the AIMoveValidator object for the AI
     */
    private AIMoveValidator aiMoveVal;

    /**
     * Creates a Hard AI for the player to play against
     */
    public HardAI() {
        super("Hard");
    }

    /**
     * Based on the current board, determine the best possible move for the
     * Hard AI to make.
     * @param aiMoveVal AIMoveValidator used to work out the AI logic for the next move
     * @return The final move for the AI based on the logic
     */
    @Override
    public Move makeMove(AIMoveValidator aiMoveVal) {

        //Get both Player's Moves (Regular Player and AI).
        ArrayList<Move> AIMoves = aiMoveVal.AIMoves(CheckerPieceColor.WHITE);
        ArrayList<Move> PlayerMovesInitial = aiMoveVal.AIMoves((CheckerPieceColor.RED));

        //If either players' move sets happen to be empty or null, return null.
        if(AIMoves==null || PlayerMovesInitial==null){
            return null;
        }
        if (AIMoves.size()==0 || PlayerMovesInitial.size()==0) {
            return null;
        }

        //Flip the Player's moves to match the AI's orientation.
        ArrayList<Move> PlayerMoves = new ArrayList<>();
        for (Move move: PlayerMovesInitial){
            Move opponentsMove = move.getOpponentsMove();
            PlayerMoves.add(opponentsMove);
        }

        /**
         * There are 2 rules being followed with the Hard AI:
         * 1. If the player is about to make a jump move, if possible, place another
         * piece in front of the jump in order to save that endangered piece.
         *
         * 2. If possible, try to avoid regular moves that will place your piece
         * in front of an opponent piece. That movement may or may not lead to a losing capture.
         */

        //Lists of Recommended and Not Recommended Moves
        ArrayList<Move> recommended = new ArrayList<>();
        ArrayList<Move> notRecommended = new ArrayList<>();

        //Priority Jumps
        //If the AI has jump move(s), just choose one of those moves.
        //Jumps moves always take priority before regular moves.
        if(AIMoves.get(0).isJump()){
            return randomMove(AIMoves);
        }

        //If the  AI doesn't have a possible jump move, look at Player's moves.
        if(PlayerMoves.get(0).isJump()){
            //*Player Has Jump(s)*
            //If the Player has jump(s), try to save your piece(s) by blocking jump.
            //Blocks moves are placed in recommended list.
            //If no Block Moves are found, just randomly choose from AI move set in the end.
            for (Move move:PlayerMoves){
                for(Move moveChoice:AIMoves){
                    if(move.getEnd().equals(moveChoice.getEnd())){
                        recommended.add(moveChoice);
                    }
                }
            }
        }
        else{
            //*Player Can Only Walk*
            //If neither Player nor AI has jump(s), the AI will try to anticipate a capture
            //If a movement leads to being in front of a player piece, add that
            // move to the not recommended list
            //However, if that move ends up being on the sides of the board, recommend it.
            //If a move doesn't lead to any risk, recommend it.
            //If for some reason no recommendations or non-recommendations have been made,
            //just randomly get move from the AI move set
            for(Move moveChoice:AIMoves){
                boolean goodMove = true;
                for (Move move:PlayerMoves) {
                    if (move.getEnd().equals(moveChoice.getEnd())) {
                        if (!(moveChoice.getEnd().getCell() == 0 || moveChoice.getEnd().getCell() == 7))
                            goodMove = false;
                    }
                }
                if (goodMove){
                    recommended.add(moveChoice);
                }
                else{
                    notRecommended.add(moveChoice);
                }
            }
        }

        //This last section is the final decision in determining what list of
        //moves the AI should choose from.
        //The AI will always follow the recommended list first, then the not recommended
        //list, and finally, if no list has been chosen yet, just randomly return a move
        //from the AI move set.
        if(recommended.size()>0) {
            return randomMove(recommended);
        }
        if(notRecommended.size()>0) {
            return randomMove(notRecommended);
        }
        return randomMove(AIMoves);
    }
}
