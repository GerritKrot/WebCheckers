package com.webcheckers.model.checkers;

import java.util.ArrayList;
/**
 * This class contains the state and behavior associated with a MoveValidator object,
 * which serves as a validator for checkers moves
 * Last Revision: 9/30/2020
 * @author Payton Burak, Michael Canning, John Davidson, Gerrit Krot, Evan Ruttenberg
 */
public class MoveValidator {

    /**
     * Standard position for specifying no jump was recently made
     */
    private final Position noPos = new Position(-1,-1);

    /**
     * Holds the Position of the last jump made. Defaults to no position.
     */
    private Position  lJump = noPos;

    /**
     * Records if the piece being verified is a Singles or King
     */
    private CheckerPieceType SingleType = null;

    /**
     * List to record jumps made
     */
    private ArrayList<Position> jumpsMade = new ArrayList<>();

    /**
     * Records what color is being focused on
     */
    private CheckerPieceColor activeColor;

    /**
     * Holds the Red Player Board
     */
    private BoardView redBoard;

    /**
     * Holds the White Player Board
     */
    private BoardView whiteBoard;

    /**
     * Holds the current Board being focused on
     */
    private BoardView curBoard;

    /**
     * constructor initializes a MoveValidator object with passed redBoard
     * and whiteBoard
     * @param redBoard the board from the view of the red player
     * @param whiteBoard the board from the view of the white player
     */
    public MoveValidator(BoardView redBoard, BoardView whiteBoard){
        this.redBoard = redBoard;
        this.whiteBoard = whiteBoard;
        this.curBoard = this.redBoard;
        activeColor = CheckerPieceColor.RED;
    }

    /**
     * Redirect given move to the second JumpCheckVal() given the current active Board and Color
     * @param move
     * @return Boolean of the results of the second JumpCheckVal()
     */
    protected boolean jumpCheckVal(Move move) {
        return jumpCheckVal(move,curBoard,activeColor);
    }

    /**
     * check if any jumps are available
     * @param move the proposed jump move that was made
     * @return whether the jump was a valid jump or not
     */
    protected boolean jumpCheckVal(Move move, BoardView board, CheckerPieceColor color) {
        if (move == null) {
            return false;
        }
        Position start = move.getStart();
        Position end = move.getEnd();
        if (!board.getSpace(end).isValid()) {
            return false;
        }
        if (board.getPiece(start)==null && color == activeColor) {
            if (!start.equals(lJump)) {
                return false;
            }
        }
        if (Math.abs(start.getCell() - end.getCell()) == 2) {
            if (Math.abs(start.getRow() - end.getRow()) == 2) {
                int midCell = (start.getCell() + end.getCell()) / 2;
                int midRow = (start.getRow() + end.getRow()) / 2;
                Position midpoint = new Position(midCell, midRow);
                Piece midPiece = board.getPiece(midpoint);
                if (midPiece != null) {
                    return !(midPiece.getColor() == color) && !(jumpsMade.contains(midpoint));
                }
            }
        }
        return false;
    }

    /**
     * check if a given move was valid
     * @param move the move to be checked
     * @return whether the move was a valid move
     */
    public boolean validate(Move move) {

        if (move == null) {
            return false;
        }

        Position start = move.getStart();
        Position end = move.getEnd();

        if (curBoard.getPiece(start) != null) {
            if (curBoard.getPiece(start).getType() == CheckerPieceType.SINGLE) {
                SingleType = CheckerPieceType.SINGLE;
                if (start.getRow() < end.getRow()) {
                    return false;
                }
            } else {
                SingleType = CheckerPieceType.KING;
            }
        } else {
            if (!start.equals(lJump)) {
                return false;
            }
            if (SingleType == CheckerPieceType.SINGLE) {
                if (start.getRow() < end.getRow()) {
                    return false;
                }
            }
        }
        if (Math.abs(start.getCell() - end.getCell()) == 1) {
            if (!validJump()) {
                return Math.abs(start.getRow() - end.getRow()) == 1;
            }
        }
        if (Math.abs(start.getCell() - end.getCell()) == 2) {
            if (Math.abs(start.getRow() - end.getRow()) == 2) {
                int midCell = (start.getCell() + end.getCell()) / 2;
                int midRow = (start.getRow() + end.getRow()) / 2;
                Position midpoint = new Position(midCell, midRow);
                Piece midPiece = curBoard.getPiece(midpoint);
                if (midPiece != null) {
                    if (!(midPiece.getColor() == activeColor) && !(jumpsMade.contains(midpoint))) {
                        jumpsMade.add(midpoint);
                        lJump = end;
                        move.setJump();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * allow a piece to move back if possible
     * @param move the move to be backed up
     */
    public void backup(Move move) {
        if (!(jumpsMade.isEmpty())) {
            jumpsMade.remove(jumpsMade.size()-1);
            lJump = move.getStart();
            if (jumpsMade.isEmpty()) {
                SingleType = null;
            }
        }
    }

    /**
     * check if there are any valid jumps available on the board
     * @return whether there are any jumps
     */
    private boolean validJump() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Position p = new Position(i,j);
                Move[] moves = getPosJumps(p);
                for (int k = 0; k < 4; k++) {
                    if (jumpCheckVal(moves[k])) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     *Alternate the active color and boards and clear recorded data
     */
    public void changeTurn(CheckerPieceColor turnColor){
        activeColor = turnColor;
        if (activeColor == CheckerPieceColor.RED){
            curBoard = redBoard;
        }
        else{
            curBoard = whiteBoard;
        }
        lJump = noPos;
        jumpsMade.clear();
        SingleType = null;
    }

    /**
     * Finishes filling up the given array from the given index
     * @param m Array that must be filled out
     * @param i Index of where to fill out array
     * @return Filled out Array
     */
    private Move[] finishMove(Move[] m, int i) {
        for (int j = i; j < 4; j++) {
            m[j] = null;
        }
        return m;
    }

    /**
     *Redirect given move to the second posMultiJump() given the recorded
     * last jump and piece type
     * @return Array of Jump moves
     */
    protected Move[] posMultiJumps(){
        return posMultiJumps(lJump, SingleType);
    }

    /**
     * From the last jump made, search for any other possible connecting
     * jumps. If a piece is a king, search for the jump moves backwards
     * as well.
     * @param p Position of last jump move
     * @param type Type of piece
     * @return
     */
    protected Move[] posMultiJumps(Position p, CheckerPieceType type){
        Move[] m = new Move[4];
        int  i = 0;
        if (p.getRow() > 1) {
            if (p.getCell() > 1) {
                m[i] = new Move(p, new Position(p.getCell() - 2, p.getRow() - 2));
                i++;
            }
            if (p.getCell() < 6) {
                m[i] = new Move(p, new Position(p.getCell() + 2, p.getRow() - 2));
                i++;
            }
        }
        if (p.getRow() < 6 && type == CheckerPieceType.KING) {
            if (p.getCell() > 1) {
                m[i] = new Move(p, new Position(p.getCell() - 2, p.getRow() + 2));
                i++;
            }
            if (p.getCell() < 6) {
                m[i] = new Move(p, new Position(p.getCell() + 2, p.getRow() + 2));
                i++;
            }
        }
        return m;
    }

    /**
     *Determine if a jump has any possible connecting jumps
     * @return Boolean of whether there is another possible jump or not
     */
    public boolean getMoreJumps(){
        Move[] m = posMultiJumps();
        for(int j=0; j<4; j++){
            if(jumpCheckVal(m[j])){
                return true;
            }
        }
        return false;
    }

    /**
     *Redirect given position to getPosJumps() with the current active board and color
     * @param position
     * @return
     */
    public Move[] getPosJumps(Position position){
        return getPosJumps(position, curBoard, activeColor);
    }

    /**
     * Given a position and color, from the given board, return an array of jumps moves
     * relative to that position.
     * @param position Position used to check relative moves.
     * @param board Board to compare moves to.
     * @param color Color of expected Piece
     * @return
     */
    public Move[] getPosJumps(Position position, BoardView board, CheckerPieceColor color) {
        Move[] m = new Move[4];
        int  i = 0;
        Space space = board.getSpace(position);
        if ((space.getPiece() == null)) {
                return finishMove(m, i);
            } else if (space.getPiece().getColor() != color) {
                return finishMove(m, i);
            }
        if (position.getRow() > 1) {
            if (position.getCell() > 1) {
                m[i] = new Move(position, new Position(position.getCell() - 2, position.getRow() - 2));
                i++;
            }
            if (position.getCell() < 6) {
                m[i] = new Move(position, new Position(position.getCell() + 2, position.getRow() - 2));
                i++;
            }
        }
        CheckerPieceType myType;
        myType = space.getPiece().getType();
        if (position.getRow() < 6 && myType == CheckerPieceType.KING) {
            if (position.getCell() > 1) {
                m[i] = new Move(position, new Position(position.getCell() - 2, position.getRow() + 2));
                i++;
            }
            if (position.getCell() < 6) {
                m[i] = new Move(position, new Position(position.getCell() + 2, position.getRow() + 2));
                i++;
            }
        }
        return finishMove(m, i);
    }


    /**
     * Given a position and color, from the given board, return an array of walk moves
     * relative to that position.
     * @param position Position used to check relative moves.
     * @param board Board to compare moves to.
     * @param color Color of expected Piece
     * @return
     */
    public Move[] getPosWalks(Position position, BoardView board, CheckerPieceColor color) {
        Move[] m = new Move[4];
        int  i = 0;
        Space s = board.getSpace(position);
        if ((s.getPiece() == null)) {
            return finishMove(m, i);
        } else if (s.getPiece().getColor() != color) {
                return finishMove(m, i);
        }
        if (position.getRow() > 0) {
            if (position.getCell() > 0) {
                m[i] = new Move(position, new Position(position.getCell() - 1, position.getRow() - 1));
                i++;
            }
            if (position.getCell() < 7) {
                m[i] = new Move(position, new Position(position.getCell() + 1, position.getRow() - 1));
                i++;
            }
        }
        CheckerPieceType myType;
        myType = s.getPiece().getType();
        if (position.getRow() < 7 && myType == CheckerPieceType.KING) {
            if (position.getCell() > 0) {
                m[i] = new Move(position, new Position(position.getCell() - 1, position.getRow() + 1));
                i++;
            }
            if (position.getCell() < 7) {
                m[i] = new Move(position, new Position(position.getCell() + 1, position.getRow() + 1));
                i++;
            }
        }
        return finishMove(m, i);
    }

    /**
     *Returns Red Player Board
     * @return Board View of Red Player
     */
    protected BoardView getRedBoard() {
        return redBoard;
    }

    /**
     *Returns White Player Board
     * @return Board View of White Player
     */
    protected BoardView getWhiteBoard() {
        return whiteBoard;
    }
}
