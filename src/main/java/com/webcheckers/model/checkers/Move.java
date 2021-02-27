package com.webcheckers.model.checkers;
/**
 * This class contains the state and behavior associated with a move in a checkers game
 * Last Revision: 10/19/2020
 * @author Payton Burak, Michael Canning, John Davidson, Gerrit Krot, Evan Ruttenberg
 */
public class Move {

    /**
     * Holds coordinates for Starting point of the Move
     */
    private final Position start;

    /**
     * Holds coordinates for the Ending point of the Move
     */
    private final Position end;

    /**
     * Determines if move is a jump or not
     */
    private boolean isJump = false;

    /**
     *Creates a Move object to hold a Player's attempted move
     * on the board via coordinates
     * @param A The starting point of the piece
     * @param B The point to move it into
     */
    public Move(Position A, Position B) {
        start = A;
        end = B;
    }
    /**
     * obtain the starting point of this move
     * @return the starting point of the current move instance
     */
    public Position getStart() {
        return start;
    }


    /**
     * obtains the ending point of this move
      * @return the ending point of the current move instance
     */
    public Position getEnd() {
        return end;
    }

    public Move getOpponentsMove() {
        Move opponentMove =new Move(new Position(7 - start.getCell(), 7 - start.getRow()), new Position(7 - end.getCell(), 7 - end.getRow()));
        if (isJump) {
            opponentMove.setJump();
        }
        return opponentMove;
    }

    /**
     * checks if the current move is a jump move
     * @return whether this move is a jump
     */
    public boolean isJump() {
        return isJump;
    }

    /**
     * set the current move to be a jump move
     */
    public void setJump() {
        isJump = true;
    }
}
