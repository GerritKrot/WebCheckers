package com.webcheckers.model.checkers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * This class contains the state and behavior associated with a BoardView object,
 * which serves as a view of the checkers board state
 * Last Revision: 9/30/2020
 * @author Payton Burak, Michael Canning, John Davidson, Gerrit Krot, Evan Ruttenberg
 */
public class BoardView implements Iterable<Row>{

    /**
     * Integer representing final board length in both length & width
     */
    private final int BOARD_LIMIT = 8;

    /**
     * ArrayList that holds an iterable list of Rows;
     * Used to essentially create the board
     */
    private List<Row> board;

    private int red_pieces_count = 0;
    private int white_pieces_count = 0;

    /***
     * Creates new boardView object that creates a new board along with
     * assigning the Player's color
     * @param startPlayer Determines whether the player initiated the game
     *                    or not (determines the Player's assigned color)
     */
    public BoardView (boolean startPlayer) {
        if (startPlayer)
            makeBoard(CheckerPieceColor.RED, CheckerPieceColor.WHITE);

        else
            makeBoard(CheckerPieceColor.WHITE, CheckerPieceColor.RED);
    }

    /**
     * Returns an iterator representing the board list
     * @return Iterator of board list
     */
    public Iterator<Row> iterator() {
        return board.iterator();
    }


    /***
     * Instantiates the private board attribute to have an arrayList of rows
     * Every row is a list of iterable Space objects along with Red and White
     * Pieces where applicable
     * @param player1 A Player color assigned to one of the Piece sets
     * @param player2 Other Player color assigned to the other Piece sets
     */
    private void makeBoard(CheckerPieceColor player1, CheckerPieceColor player2) {
        board = new ArrayList<>();

        boolean blackSpace;
        for (int i = 0; i < BOARD_LIMIT; i++) {
            blackSpace= !(i % 2 == 0);
            ArrayList<Space> tempRow = new ArrayList<>();
            for (int j = 0; j < BOARD_LIMIT; j++) {
                if (blackSpace) {
                    if ((i == 0) || (i == 1) || (i==2)) {
                        if(player2.equals(CheckerPieceColor.RED)) {
                            red_pieces_count ++;
                        } else {
                            white_pieces_count ++;
                        }
                        tempRow.add(new Space(
                                new Position(j,i),
                                new Piece(CheckerPieceType.SINGLE, player2),
                                SpaceColor.BLACK));
                    } else {
                        if ((i==5) || (i == 6) || (i == 7)) {
                            if(player1.equals(CheckerPieceColor.RED)) {
                                red_pieces_count ++;
                            } else {
                                white_pieces_count ++;
                            }
                            tempRow.add(new Space(
                                    new Position(j,i),
                                    new Piece(CheckerPieceType.SINGLE, player1),
                                    SpaceColor.BLACK));
                        } else {
                            tempRow.add(new Space(
                                    new Position(j,i),
                                    null,
                                    SpaceColor.BLACK));
                        }
                    }
                } else {
                    tempRow.add(new Space(
                            new Position(j, i),
                            null,
                            SpaceColor.WHITE));
                }
                blackSpace = !(blackSpace);
            }
            board.add(new Row(i, tempRow));
        }
    }

    /***
     * Retrieves the Piece that resides in a Space on the board
     * @param pos The coordinates of where the Piece resides
     * @return Piece on the given spot if there is one
     */
    public Piece getPiece(Position pos) {
        return board.get(pos.getRow()).getSpace(pos.getCell()).getPiece();
    }

    private void setPiece(Position pos, Piece newPiece) {
        board.get(pos.getRow()).getSpace(pos.getCell()).setPiece(newPiece);
    }

    public void updateView(Move move) {
        if (move.isJump()) {
            Position start = move.getStart();
            Position endPos = move.getEnd();
            int midCell = (start.getCell() + endPos.getCell())/2;
            int midRow = (start.getRow() + endPos.getRow()) / 2;
            Position midpoint = new Position(midCell, midRow);
            try {
                if (getPiece(midpoint).getColor().equals(CheckerPieceColor.RED)) {
                    red_pieces_count--;
                } else {
                    white_pieces_count--;
                }
            } catch (Exception e) {
                // Do nothing, this only protects for some unit testing
            }
            setPiece(midpoint, null);
        }
        Piece pieceToMove = getPiece(move.getStart());
        setPiece(move.getStart(), null);
        if (move.getEnd().getRow() == 0 || move.getEnd().getRow() == 7) {
            pieceToMove.kingChecker();
        }
        setPiece(move.getEnd(), pieceToMove);
    }


    Space getSpace(Position pos) {
        return board.get(pos.getRow()).getSpace(pos.getCell());
    }



    /**
     * Checks to see if either player has no pieces left and returns the winner if there is one
     * @return null if no winner, checker piece color of winner if there is a winner.
     */
    public CheckerPieceColor check_winner() {
        if(red_pieces_count == 0) {
            return CheckerPieceColor.WHITE;
        } else if(white_pieces_count == 0) {
            return CheckerPieceColor.RED;
        } else {
            return null;
        }
    }
}
