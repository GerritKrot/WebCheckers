package com.webcheckers.appl;

import com.webcheckers.model.AIPlayer;
import com.webcheckers.model.checkers.*;

import java.util.ArrayList;

public class EasyAI extends AIPlayer {

    /**
     * Holds the AIMoveValidator object for the AI
     */
    private AIMoveValidator aiMoveVal;

    /**
     * Creates an Easy AI for the player to play against
     */
    public EasyAI() {
        super("Easy");
    }

    /**
     * Randomly determine a move for the Easy AI based on the current board.
     * @param aiMoveVal AIMoveValidator used to work out the AI logic for the next move
     * @return The final move for the AI based on the logic
     */
    @Override
    public Move makeMove(AIMoveValidator aiMoveVal) {
        this.aiMoveVal = aiMoveVal;
        ArrayList<Move> moves = aiMoveVal.AIMoves(CheckerPieceColor.WHITE);
        return randomMove(moves);
    }
}
